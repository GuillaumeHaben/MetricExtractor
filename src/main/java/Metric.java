import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.List;

public class Metric {

    private int nbLines;
    private List<CtAnnotation<? extends Annotation>> annotations;
    private int nbCyclo;
    private int nbAsyncWaits;

    public Metric() {
        this.nbLines = 0;
        this.nbCyclo = 0;
        this.nbAsyncWaits = 0;
    }

    public void showNbLines() {
        System.out.println("Number of lines: " +  this.nbLines);
    }

    public void showAnnotations() {
        System.out.println("Annotations: " + this.annotations);
    }

    public void showNbCyclo() {
        System.out.println("Cyclomatic complexity: " + this.nbCyclo);
    }

    public void showAsyncWaits() {
        System.out.println("Number of Asynchronous waits: " + this.nbAsyncWaits);
    }

    public void computeMetrics(CtMethod method) {
        // Compute nbLines
        String[] lines = method.getBody().toString().split("\r\n|\r|\n");
        this.nbLines = lines.length;

        // Compute nbCyclo
        int nbCond = method.getElements(new TypeFilter(CtIf.class)).size();
        int nbLoop = method.getElements(new TypeFilter(CtLoop.class)).size();
        this.nbCyclo = nbCyclo + nbCond + nbLoop;

        // Compute nbAsyncWaits
        List listInvocations = method.getBody().getElements(new TypeFilter(CtInvocation.class));
        for (Object inv : listInvocations) {
            if (inv.toString().contains("Thread.sleep(") || inv.toString().contains(".wait(")) {
                this.nbAsyncWaits++;
            }
        }

        // Compute annotations
        this.annotations = method.getAnnotations();
    }

}
