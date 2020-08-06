package base;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Search {

    private String projectPath;
    private CtModel model;
    private String methodName;
    private String className;

    public Search(String path) {
        projectPath = path;
        model = createSpoonModel();
    }

    public CtModel createSpoonModel() {
        // Creating launcher
        Launcher launcher = new Launcher();
        launcher.addInputResource(this.projectPath);
        // Escape comments
        launcher.getEnvironment().setCommentEnabled(false);
        // Creating model from project
        launcher.buildModel();
        CtModel model = launcher.getModel();
        return model;
    }

    /**
     * -INTERACTIVE- Find a particular method in the project based on a className.methodName
     * Call methodSearch to compute metrics and generate reports
     */
    public void singleMethodSearch() throws IOException {
        // ToDo Add checkers
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter method you want to analyze [format: className.methodName]");
        String fullName = myObj.nextLine();
        String[] arrayName = fullName.split("\\.");
        this.methodName = arrayName[arrayName.length - 1];
        this.className = arrayName[arrayName.length - 2];
        methodSearch();
    }

    /**
     * -INTERACTIVE- Find particular methods in the project based on a file
     * Call methodSearch to compute metrics and generate reports
     */
    public void listOfMethodSearch() throws IOException {
        // Ask for file path
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter path to file with methods list inside");
        String path = myObj.nextLine();
        File file = new File(path);
        // Check if file exists
        if (!file.exists()) {
            System.out.println("File not found, please enter absolute path.");
            listOfMethodSearch();
        }
        else {
            String st;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                this.getMethodFromFile(st);
                this.methodSearch();
            }
        }
    }

    /**
     * Find particular methods in the project based on a file
     * Call methodSearch to compute metrics and generate reports
     * @param listPath the file path
     */
    public void listOfMethodSearch(String listPath) throws IOException {
        File file = new File(listPath);
        // Check if file exists
        if (!file.exists()) {
            System.out.println("-listPath: File not found, please enter absolute path.");
            System.exit(0);
        }
        else {
            String st;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null) {
                this.getMethodFromFile(st);
                this.methodSearch();
            }
        }
    }

    /**
     * Find a particular method in the project
     * Compute its metrics
     * Generate JSON report in ./results/projectName/
     */
    public void methodSearch() throws IOException {
        Boolean classFound = false;
        Boolean methodFound = false;
        Metric metric = new Metric();

        // For all classes
        for(CtType<?> classes : this.model.getAllTypes()) {
            // Match class name
            if (classes.getSimpleName().equals(this.className)) {
                classFound = true;
                // For all methods
                for(CtMethod methods : classes.getMethods()) {
                    // Match method name
                    if (methods.getSimpleName().equals(this.methodName)) {
                        methodFound = true;

                        metric.computeMetrics(methods, classes);
                        metric.generateReport(this.methodName, this.className.toString(), this.projectPath);
                        break;
                    }
                }
                break;
            }
        }
        // Error handlers
        if (!classFound) {
            System.out.println("No class \"" + this.className + "\" found.");
        }
        if (classFound && !methodFound) {
            System.out.println("No Method \"" + this.methodName + "\" found in class \"" + this.className + "\".");
        }
    }

    /**
     * Find all test methods in the project, not empty and starting with @Test
     * Compute their metrics
     * Generate JSON reports in ./results/projectName/
     */
    public void getAllTestMethods() throws IOException {
        // For all classes
        for(CtType<?> classes : this.model.getAllTypes()) {
            // For all methods
            for (CtMethod methods : classes.getMethods()) {
                Metric metric = new Metric();
                /*
                Looking for Code Under Test, I want
                Method's body not empty
                A @test annotation
                 */
                if (methods.getBody() != null && methods.getAnnotations().toString().contains("@org.junit.Test")) {
                    metric.computeMetrics(methods, classes);
                    metric.generateReport(methods.getSimpleName(), classes.getSimpleName(), this.projectPath);
                }
            }
        }
    }

    /**
     * Find all methods in the project, not empty and not starting with @Test
     * Compute their metrics
     * Generate JSON reports in ./results/projectName/
     */
    public void getAllMethods() throws IOException {
        // For all classes
        for(CtType<?> classes : this.model.getAllTypes()) {
            // For all methods
            for (CtMethod methods : classes.getMethods()) {
                Metric metric = new Metric();
                /*
                Looking for Code Under Test, I want
                Method's body not empty
                No @test annotation
                No ClassName starting or ending with "Test"
                 */
                if (methods.getBody() != null && !methods.getAnnotations().toString().contains("@org.junit.Test") && !classes.getSimpleName().startsWith("Test") && !classes.getSimpleName().endsWith("Test")) {
                    metric.computeMetrics(methods, classes);
                    metric.generateReport(methods.getSimpleName(), classes.getSimpleName(), this.projectPath);
                }
            }
        }
    }

    /**
     * Helper, Extract Method Name and Class Name from File path
     * Save them in Search attributes
     */
    public void getMethodFromFile(String fullName) {
        // ToDo Add checkers
        String[] arrayName = fullName.split("\\.");
        this.methodName = arrayName[arrayName.length - 1];
        this.className = arrayName[arrayName.length - 2];
    }

}
