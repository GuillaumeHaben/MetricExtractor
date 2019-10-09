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

public class Main {

    public static void main(String [] args){

        // Input project - folder path
        String projectPath = "/Users/guillaume.haben/Documents/Work/projects/TooTallNate/Java-WebSocket/";

        // Get Flaky Test
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter flaky method name");

        String fullName = myObj.nextLine();
        String[] arrayName = fullName.split("\\.");
        String methodName = arrayName[arrayName.length - 1];
        String className = arrayName[arrayName.length - 2];

        System.out.println("Full flaky test case name: " + fullName);
        System.out.println("methodName: " + methodName);
        System.out.println("className: " + className);

        // Creating launcher
        Launcher launcher = new Launcher();
        launcher.addInputResource(projectPath);

        // Creating model
        launcher.buildModel();
        CtModel model = launcher.getModel();

        System.out.println("Analyzing project: " + projectPath);

        // For all classes
        for(CtType<?> s : model.getAllTypes()) {
            // Match class name
            if (s.getSimpleName().equals(className)) {
                // For all methods
                for(CtMethod<?> t : s.getMethods()) {
                    // Match method name
                    if (t.getSimpleName().equals(methodName)) {
                        String[] lines = t.getBody().toString().split("\r\n|\r|\n");
                        System.out.println("Method found: " + t.getSimpleName());
                        System.out.println("Class found: " + s.getSimpleName());
                        // Metric: Method size
                        System.out.println("Number of lines: " + lines.length);
                        // Metric: Has timeout annotation
                        System.out.println("Annotation: " + t.getAnnotations());

                        int nbCyclo = 0;
                        int nbCond = t.getElements(new TypeFilter(CtIf.class)).size();
                        int nbLoop = t.getElements(new TypeFilter(CtLoop.class)).size();

                        nbCyclo = nbCyclo + nbCond + nbLoop;
                        // Metric: Cyclomatic complexity
                        System.out.println("Cyclomatic complexity: " + nbCyclo);

                        List listInvocations = t.getBody().getElements(new TypeFilter(CtInvocation.class));
                        for (Object inv : listInvocations) {
                            if (inv.toString().contains(".sleep(") || inv.toString().contains("wait(")) {
                                System.out.println("Called methods: " + inv.toString());
                            }
                        }
                        // Find methods
                        //System.out.println("Called methods: " + listInvocations.get(0).toString());
                    }
                }
            }
        }

    }
}
