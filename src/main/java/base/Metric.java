package base;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Metric {

    // Arguments declaration
    private int nbLines;
    private int nbCyclo;
    private int nbAsyncWaits;
    private int nbAsserts;
    private int nbThreads;
    private int nbDates;
    private int nbRandoms;
    private int nbFiles;
    private int depthOfInheritance;
    private int hasTimeOutInAnnotation;
    private String methodBody;
    private ArrayList<CutMetric> staticCUT;
    private String methodName;
    private String className;

    public Metric() {
        // Arguments Initialization
        this.nbLines = 0;
        this.nbCyclo = 0;
        this.nbAsyncWaits = 0;
        this.nbAsserts = 0;
        this.nbThreads = 0;
        this.nbDates = 0;
        this.nbRandoms = 0;
        this.nbFiles = 0;
        this.depthOfInheritance = 0;
        this.hasTimeOutInAnnotation = 0;
        this.methodBody = "";
        this.staticCUT = new ArrayList<CutMetric>();
        this.methodName = "";
        this.className = "";
    }

    /**
     * Compute all metrics for currentClass.currentMethod
     * @param currentMethod the current method being analyzed
     * @param currentClass the current class being analyzed
     */
    public void computeMetrics(CtExecutable currentMethod, CtType currentClass) {
        // Info for CUT metrics
        this.methodName = currentMethod.getSimpleName();
        this.className = currentClass.getSimpleName();

        // Compute nbLines
        String[] lines = currentMethod.getBody().toString().split("\r\n|\r|\n");
        this.nbLines = lines.length;

        // Compute nbCyclo
        int nbCond = currentMethod.getElements(new TypeFilter(CtIf.class)).size();
        int nbLoop = currentMethod.getElements(new TypeFilter(CtLoop.class)).size();
        this.nbCyclo = nbCyclo + nbCond + nbLoop;

        // Get lists of objects to go through in the next for loops.
        List<CtInvocation> listInvocations = currentMethod.getBody().getElements(new TypeFilter(CtInvocation.class));
        List listTypeReferences = currentMethod.getBody().getElements(new TypeFilter(CtTypeReference.class));

        // Compute nbAsyncWaits and nbAsserts
        for (CtInvocation inv : listInvocations) {
            String invocation = inv.toString();

            if (invocation.contains("Thread.sleep(") || invocation.contains(".wait(")) this.nbAsyncWaits++;
            // List of methods coming from org.junit.Assert
            if (invocation.contains("org.junit.Assert") ) this.nbAsserts++;
        }
        // Compute nbThreads, nbDates, nbRandoms, nbFiles
        for (Object type : listTypeReferences) {
            String typeRef = type.toString();

            if (typeRef.contains("java.lang.Thread") || typeRef.contains("java.util.concurrent")) this.nbThreads++;
            if (typeRef.contains("java.util.Date") || typeRef.contains("java.util.TimeZone")) this.nbDates++;
            if (typeRef.contains("java.util.Random")) this.nbRandoms++;
            if (typeRef.contains("java.io.File")) this.nbFiles++;

        }
        // Compute hasTimeOutAnnotations
        if (currentMethod.getAnnotations().toString().contains("timeout")) {
            this.hasTimeOutInAnnotation = 1;
        }
        // Compute depthOfInheritance
        this.depthOfInheritance = getDepthOfInheritanceTree(currentClass.getReference());
        // Get method's body
        this.methodBody = currentMethod.getBody().toString();

    }

    /**
     * Find invoked methods and constructor calls for currentClass.currentMethod
     * Compute their metrics and add them to list of StaticCUT
     * @param currentMethod the current method being analyzed
     * @param currentClass the current class being analyzed
     */
    public void computeCUTMetrics(CtMethod currentMethod, CtType currentClass) {
        // Get lists of invocations and constructor calls to go through in the next for loops.
        List<CtInvocation> listInvocations = currentMethod.getBody().getElements(new TypeFilter(CtInvocation.class));
        List<CtConstructorCall> listConstructorCall = currentMethod.getBody().getElements(new TypeFilter(CtConstructorCall.class));

        for (CtConstructorCall currentConstructor : listConstructorCall) {
            Metric metric = new Metric();
            try {
                CtExecutable calledMethod = currentConstructor.getExecutable().getDeclaration();
                CtType classOfCalledMethod = currentConstructor.getExecutable().getDeclaringType().getDeclaration();
                if (calledMethod == null || classOfCalledMethod == null) continue;
                metric.computeMetrics(calledMethod, classOfCalledMethod);
                this.addCUTMetricsToMetrics(metric);
            } catch (NullPointerException E) {
                continue;
            }
        }
        for (CtInvocation currentInvocation : listInvocations) {
            Metric metric = new Metric();
            try {
                CtExecutable calledMethod = currentInvocation.getExecutable().getDeclaration();
                CtType classOfCalledMethod = currentInvocation.getExecutable().getDeclaringType().getDeclaration();
                if (calledMethod == null || classOfCalledMethod == null) continue;
                metric.computeMetrics(calledMethod, classOfCalledMethod);
                this.addCUTMetricsToMetrics(metric);
            } catch (NullPointerException E) {
                continue;
            }
        }
    }

    /**
     * Create metrics for CUT.
     * Add it to current Metric.
     * @param metric Metrics from the method (CUT)
     */
    public void addCUTMetricsToMetrics(Metric metric) {
        CutMetric cutMetric = new CutMetric(metric);
        this.staticCUT.add(cutMetric);
    }

    /**
     * Recursively find the number of inheritance for the analyzed class
     * @param type current class
     * @return number of level of inheritance
     */
    public int getDepthOfInheritanceTree(CtTypeReference<?> type) {
        if (type.isShadow() || type.getSuperclass() == null) return 0;
        else return 1 + getDepthOfInheritanceTree(type.getSuperclass());
    }

    /**
     * Create a JSON object for the current method containing all metrics, save it to a JSON file in results folder.
     * @param methodName current method being analyzed
     * @param className current class being analyzed
     * @param projectPath current project being analyzed
     * @throws IOException
     */
    public void generateReport(String methodName, String className, String projectPath) throws IOException {
        // Create JSON objects
        JSONObject methodObject = new JSONObject();

        // Populate JSON object
        methodObject.put("ProjectName", projectPath);
        methodObject.put("ClassName", className);
        methodObject.put("MethodName", methodName);
        methodObject.put("NumberOfLines", this.nbLines);
        methodObject.put("CyclomaticComplexity", this.nbCyclo);
        methodObject.put("NumberOfAsynchronousWaits", this.nbAsyncWaits);
        methodObject.put("NumberOfAsserts", this.nbAsserts);
        methodObject.put("NumberOfThreads", this.nbThreads);
        methodObject.put("NumberOfDates", this.nbDates);
        methodObject.put("NumberOfRandoms", this.nbRandoms);
        methodObject.put("NumberOfFiles", this.nbFiles);
        methodObject.put("DepthOfInheritance", this.depthOfInheritance);
        methodObject.put("HasTimeoutInAnnotations", this.hasTimeOutInAnnotation);
        methodObject.put("Body", this.methodBody);

        JSONArray array = new JSONArray();

        // If CUT metrics exist, we add the CUT to the JSON object.
        if (this.staticCUT != null && !this.staticCUT.isEmpty()) {

            for (CutMetric element : this.staticCUT) {
                JSONObject cutObject = new JSONObject();
                cutObject.put("ClassName", element.getClassName());
                cutObject.put("MethodName", element.getMethodName());
                cutObject.put("NumberOfLines", element.getNbLines());
                cutObject.put("CyclomaticComplexity", element.getNbCyclo());
                cutObject.put("NumberOfAsynchronousWaits", element.getNbAsyncWaits());
                cutObject.put("NumberOfThreads", element.getNbThreads());
                cutObject.put("NumberOfDates", element.getNbDates());
                cutObject.put("NumberOfRandoms", element.getNbRandoms());
                cutObject.put("NumberOfFiles", element.getNbFiles());
                cutObject.put("DepthOfInheritance", element.getDepthOfInheritance());
                cutObject.put("Body", element.getMethodBody());
                if (!array.contains(cutObject)) {
                    array.add(cutObject);
                }
            }

        }
        methodObject.put("StaticCUT", array);

        // Create Directory
        String[] arrayName = projectPath.split("/");
        String projectName = arrayName[arrayName.length - 1];
        new File("/Users/guillaume.haben/Documents/Work/projects/MetricExtractor/results/" + projectName).mkdirs();
        // Write JSON file into the newly created directory
        Files.write(Paths.get("/Users/guillaume.haben/Documents/Work/projects/MetricExtractor/results/" + projectName + "/" + className + "." + methodName + ".json"), methodObject.toJSONString().getBytes());
    }

    public int getNbLines() {
        return nbLines;
    }

    public int getNbCyclo() {
        return nbCyclo;
    }

    public int getNbAsyncWaits() {
        return nbAsyncWaits;
    }

    public int getNbThreads() {
        return nbThreads;
    }

    public int getNbDates() {
        return nbDates;
    }

    public int getNbRandoms() {
        return nbRandoms;
    }

    public int getNbFiles() {
        return nbFiles;
    }

    public int getDepthOfInheritance() {
        return depthOfInheritance;
    }

    public String getMethodBody() {
        return methodBody;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
