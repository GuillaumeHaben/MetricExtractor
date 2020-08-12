package base;

import org.json.simple.JSONObject;
import spoon.reflect.code.*;
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
    private ArrayList<String> listInvocationsString;

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
        this.listInvocationsString = new ArrayList<String>();
    }

    /**
     * Compute all metrics for myClass.method
     * @param method the current method being analyzed
     * @param myClass the current class being analyzed
     */
    public void computeMetrics(CtMethod method, CtType myClass) {
        // Compute nbLines
        String[] lines = method.getBody().toString().split("\r\n|\r|\n");
        this.nbLines = lines.length;

        // Compute nbCyclo
        int nbCond = method.getElements(new TypeFilter(CtIf.class)).size();
        int nbLoop = method.getElements(new TypeFilter(CtLoop.class)).size();
        this.nbCyclo = nbCyclo + nbCond + nbLoop;

        // Get lists of objects to go through in the next for loops.
        List<CtInvocation> listInvocations = method.getBody().getElements(new TypeFilter(CtInvocation.class));
        List<CtConstructorCall> listConstructorCall = method.getBody().getElements(new TypeFilter(CtConstructorCall.class));
        List listTypeReferences = method.getBody().getElements(new TypeFilter(CtTypeReference.class));

        // Compute nbAsyncWaits and nbAsserts
        for (CtInvocation inv : listInvocations) {
            String invocation = inv.toString();
            String className = inv.getExecutable().getDeclaringType().getSimpleName();
            String methodName = inv.getExecutable().getSimpleName();
            String saveName = className + "." + methodName;

            // List of invocations
            if (!this.listInvocationsString.contains(saveName)) {
                this.listInvocationsString.add(saveName);
            }

            if (invocation.contains("Thread.sleep(") || invocation.contains(".wait(")) this.nbAsyncWaits++;
            // List of methods coming from org.junit.Assert
            if (invocation.contains("org.junit.Assert") ) this.nbAsserts++;

        }
        for (CtConstructorCall constr : listConstructorCall) {
            // List of invocations, for constructors
            String className = constr.getExecutable().getDeclaringType().getSimpleName();
            String methodName = constr.getExecutable().getSimpleName();
            String saveName = className + "." + methodName;

            // List of invocations
            if (!this.listInvocationsString.contains(saveName)) {
                this.listInvocationsString.add(saveName);
            }
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
        if (method.getAnnotations().toString().contains("timeout")) {
            this.hasTimeOutInAnnotation = 1;
        }
        // Compute depthOfInheritance
        this.depthOfInheritance = getDepthOfInheritanceTree(myClass.getReference());
        // Get method's body
        this.methodBody = method.getBody().toString();

    }

    /**
     * Recursively find the number of inheritance for the analyzed class
     * @param type current class
     * @return number of level of inheritance
     */
    public static int getDepthOfInheritanceTree(CtTypeReference<?> type) {
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
        // Create JSON object
        JSONObject sampleObject = new JSONObject();
        // Populate JSON object
        sampleObject.put("ProjectName", projectPath);
        sampleObject.put("ClassName", className);
        sampleObject.put("MethodName", methodName);
        sampleObject.put("NumberOfLines", this.nbLines);
        sampleObject.put("CyclomaticComplexity", this.nbCyclo);
        sampleObject.put("NumberOfAsynchronousWaits", this.nbAsyncWaits);
        sampleObject.put("NumberOfAsserts", this.nbAsserts);
        sampleObject.put("NumberOfThreads", this.nbThreads);
        sampleObject.put("NumberOfDates", this.nbDates);
        sampleObject.put("NumberOfRandoms", this.nbRandoms);
        sampleObject.put("NumberOfFiles", this.nbFiles);
        sampleObject.put("DepthOfInheritance", this.depthOfInheritance);
        sampleObject.put("HasTimeoutInAnnotations", this.hasTimeOutInAnnotation);
        sampleObject.put("Body", this.methodBody);
        sampleObject.put("ListInvocations", this.listInvocationsString);

        // Create Directory
        String[] arrayName = projectPath.split("/");
        String projectName = arrayName[arrayName.length - 1];
        new File("/Users/guillaume.haben/Documents/Work/projects/MetricExtractor/results/" + projectName).mkdirs();
        // Write JSON file into the newly created directory
        Files.write(Paths.get("/Users/guillaume.haben/Documents/Work/projects/MetricExtractor/results/" + projectName + "/" + className + "." + methodName + ".json"), sampleObject.toJSONString().getBytes());
    }
}
