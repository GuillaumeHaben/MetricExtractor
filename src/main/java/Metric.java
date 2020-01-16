import org.json.simple.JSONObject;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        List listInvocations = method.getBody().getElements(new TypeFilter(CtInvocation.class));
        List listTypeReferences = method.getBody().getElements(new TypeFilter(CtTypeReference.class));

        // Compute nbAsyncWaits and nbAsserts
        for (Object inv : listInvocations) {
            String invocation = inv.toString();
            if (invocation.contains("Thread.sleep(") || invocation.contains(".wait(")) this.nbAsyncWaits++;
            // List of methods coming from org.junit.Assert
            if (invocation.contains("org.junit.Assert") ) this.nbAsserts++;
        }

        // Compute nbThreads, nbDates, nbRandoms, nbFiles
        for (Object inv : listTypeReferences) {
            String invocation = inv.toString();
            if (invocation.contains("java.lang.Thread") || invocation.contains("java.util.concurrent")) this.nbThreads++;
            if (invocation.contains("java.util.Date") || invocation.contains("java.util.TimeZone")) this.nbDates++;
            if (invocation.contains("java.util.Random")) this.nbRandoms++;
            if (invocation.contains("java.io.File")) this.nbFiles++;
        }

        // Compute hasTimeOutAnnotations
        if (method.getAnnotations().toString().contains("timeout")) {
            this.hasTimeOutInAnnotation = 1;
        }

        // Compute depthOfInheritance
        this.depthOfInheritance = getDepthOfInheritanceTree(myClass.getReference());
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
        JSONObject sampleObject = new JSONObject();

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

        String[] arrayName = projectPath.split("/");
        String projectName = arrayName[arrayName.length - 1];
        new File("/Users/guillaume.haben/Documents/Work/projects/MetricExtractor/results/" + projectName).mkdirs();

        Files.write(Paths.get("/Users/guillaume.haben/Documents/Work/projects/MetricExtractor/results/" + projectName + "/" + className + "." + methodName + ".json"), sampleObject.toJSONString().getBytes());
    }

    public void showNbLines() {
        System.out.println("Number of lines: " +  this.nbLines);
    }

    public void showAnnotations() {
        System.out.println("Has timeout in Annotations: " + this.hasTimeOutInAnnotation);
    }

    public void showNbCyclo() {
        System.out.println("Cyclomatic complexity: " + this.nbCyclo);
    }

    public void showNbAsyncWaits() {
        System.out.println("Number of Asynchronous waits: " + this.nbAsyncWaits);
    }

    public void showNbAsserts() {
        System.out.println("Number of Asserts: " + this.nbAsserts);
    }

    public void showNbThreads() {
        System.out.println("Number of Threads: " + this.nbThreads);
    }

    public void showNbDates() {
        System.out.println("Number of Dates: " + this.nbDates);
    }

    public void showNbRandoms() {
        System.out.println("Number of Randoms: " + this.nbRandoms);
    }

    public void showNbFiles() {
        System.out.println("Number of Files: " + this.nbFiles);
    }

    public void showDepthOfInheritance() {
        System.out.println("Depth of Inheritance: " + this.depthOfInheritance);
    }
}
