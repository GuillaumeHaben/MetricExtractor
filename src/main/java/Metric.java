import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.List;

public class Metric {

    private int nbLines;
    private Boolean hasTimeOutInAnnotation;
    private int nbCyclo;
    private int nbAsyncWaits;
    private int nbAsserts;
    private int depthOfInheritance;

    public Metric() {
        this.nbLines = 0;
        this.nbCyclo = 0;
        this.nbAsyncWaits = 0;
        this.nbAsserts = 0;
        this.depthOfInheritance = 0;
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

    public void showDepthOfInheritance() {
        System.out.println("Depth of Inheritance: " + this.depthOfInheritance);
    }

    public void computeMetrics(CtMethod method, CtType myClass) {
        // Compute nbLines
        String[] lines = method.getBody().toString().split("\r\n|\r|\n");
        this.nbLines = lines.length;

        // Compute nbCyclo
        int nbCond = method.getElements(new TypeFilter(CtIf.class)).size();
        int nbLoop = method.getElements(new TypeFilter(CtLoop.class)).size();
        this.nbCyclo = nbCyclo + nbCond + nbLoop;

        // Compute nbAsyncWaits and nbAsserts
        List listInvocations = method.getBody().getElements(new TypeFilter(CtInvocation.class));


        for (Object inv : listInvocations) {
            String invocation = inv.toString();
            if (invocation.contains("Thread.sleep(") || invocation.contains(".wait(")) {
                this.nbAsyncWaits++;
            }
            // List of methods coming from org.junit.Assert
            if (    invocation.contains("assertArrayEquals(") ||
                    invocation.contains("assertEquals(") ||
                    invocation.contains("assertTrue(") ||
                    invocation.contains("assertFalse(") ||
                    invocation.contains("assertNotNull(") ||
                    invocation.contains("assertNull(") ||
                    invocation.contains("assertSame(") ||
                    invocation.contains("assertNotSame(") ||
                    invocation.contains("assertThat(") ||
                    invocation.contains("fail(") ) {
                this.nbAsserts++;
            }
        }

        // Compute annotations
        if (method.getAnnotations().toString().contains("timeout")) {
            this.hasTimeOutInAnnotation = true;
        }

        // Compute depth of inheritance tree
        this.depthOfInheritance = getDepthOfInheritanceTree(myClass.getReference());


        // Incoming metrics
        // System.out.println("\n*** Debug ***");
        // System.out.println("Method" + method.toString());
        // System.out.println("*** Debug ***\n");
    }

    public static int getDepthOfInheritanceTree(CtTypeReference<?> type) {
        if (type.isShadow() || type.getSuperclass() == null) {
            return 0;
        } else {
            return 1 + getDepthOfInheritanceTree(type.getSuperclass());
        }
    }

}
