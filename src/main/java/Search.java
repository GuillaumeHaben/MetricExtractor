import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.*;
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

        // Creating model from project
        launcher.buildModel();
        CtModel model = launcher.getModel();
        return model;
    }

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

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                Search currentSearch = new Search(this.projectPath);
                currentSearch.getMethodFromFile(st);
                currentSearch.methodSearch();
            }
        }
    }

    public void listOfMethodSearch(String listPath) throws IOException {

        File file = new File(listPath);

        // Check if file exists
        if (!file.exists()) {
            System.out.println("-listPath: File not found, please enter absolute path.");
            System.exit(0);
        }
        else {

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                Search currentSearch = new Search(this.projectPath);
                currentSearch.getMethodFromFile(st);
                currentSearch.methodSearch();
            }
        }
    }

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

                        System.out.println("\nClass found: " + classes.getSimpleName());
                        System.out.println("Method found: " + methods.getSimpleName());

                        metric.computeMetrics(methods, classes);

                        metric.generateReport(this.methodName, this.className.toString(), this.projectPath);

                        metric.showNbLines();
                        metric.showNbCyclo();
                        metric.showNbAsyncWaits();
                        metric.showNbAsserts();
                        metric.showNbThreads();
                        metric.showNbDates();
                        metric.showNbRandoms();
                        metric.showNbFiles();
                        metric.showDepthOfInheritance();
                        metric.showAnnotations();

                    }
                }
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

    public void getMethodFromFile(String fullName) {
        // ToDo Add checkers
        String[] arrayName = fullName.split("\\.");
        this.methodName = arrayName[arrayName.length - 1];
        this.className = arrayName[arrayName.length - 2];
    }

    public void getAllTestMethods() {
        // For all classes
        for(CtType<?> classes : this.model.getAllTypes()) {
            // For all methods
            for (CtMethod methods : classes.getMethods()) {
                if (methods.getAnnotations().toString().contains("@org.junit.Test")) {
                    System.out.println(classes.getSimpleName() + "." + methods.getSimpleName());
                }
            }
        }
    }
}
