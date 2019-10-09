import spoon.Launcher;
import spoon.reflect.CtModel;

public class Main {

    public static void main(String [] args){

        // Input project - folder path
        String projectPath = "/Users/guillaume.haben/Documents/Work/projects/TooTallNate/Java-WebSocket/";

        // Creating launcher
        Launcher launcher = new Launcher();
        launcher.addInputResource(projectPath);

        // Creating model
        launcher.buildModel();
        CtModel model = launcher.getModel();

        System.out.println("Analyzing project: " + projectPath);

    }
}
