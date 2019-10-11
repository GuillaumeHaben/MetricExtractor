import org.apache.commons.io.filefilter.FalseFileFilter;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;
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

        // Creating model
        launcher.buildModel();
        CtModel model = launcher.getModel();
        return model;
    }

    public void getMethodFromUser() {
        // ToDo Add checkers
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter flaky method name");
        String fullName = myObj.nextLine();
        String[] arrayName = fullName.split("\\.");

        this.methodName = arrayName[arrayName.length - 1];
        this.className = arrayName[arrayName.length - 2];

        System.out.println("methodName: " + this.methodName);
        System.out.println("className: " + this.className);

    }

    public void methodSearch() {

        Boolean classFound = false;
        Boolean methodFound = false;
        Metric metric = new Metric();

        getMethodFromUser();

        // For all classes
        for(CtType<?> s : this.model.getAllTypes()) {
            // Match class name
            if (s.getSimpleName().equals(this.className)) {
                classFound = true;
                // For all methods
                for(CtMethod t : s.getMethods()) {
                    // Match method name
                    if (t.getSimpleName().equals(this.methodName)) {
                        methodFound = true;

                        System.out.println("Class found: " + s.getSimpleName());
                        System.out.println("Method found: " + t.getSimpleName());

                        metric.computeMetrics(t);

                        metric.showNbLines();
                        metric.showAnnotations();
                        metric.showNbCyclo();
                        metric.showAsyncWaits();

                    }
                }
            }
        }
        // Error handlers
        if (!classFound) {
            System.out.println("No class \"" + this.className + "\" found.");
            methodSearch();
        }
        if (classFound && !methodFound) {
            System.out.println("No Method \"" + this.methodName + "\" found in class \"" + this.className + "\".");
            methodSearch();
        }
    }

    public static void classSearch() {

    }

    public static void projectSearch() {

    }

}
