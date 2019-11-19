import java.io.IOException;

public class Main {

    public static void main(String [] args) throws IOException {
        // ----- MENU -----
        // Menu menu = new Menu();

        // ----- ARGS -----
        int i = 0;
        String arg;
        String projectPath = "";
        String listPath = "";

        if (args.length != 4) {
            System.out.println("Usage: MetricExtractor [-projectPath] /project/sources/to/analyze/ [-listPath] /flaky/tests/list.json");
            System.exit(0);
        }

        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];
            if (arg.equals("-projectPath")) {
                if (i < args.length) {
                    projectPath = args[i++];
                } else {
                    System.err.println("-projectPath requires a path");
                    System.exit(0);
                }
            }
            else if (arg.equals("-listPath")) {
                if (i < args.length) {
                    listPath = args[i++];
                } else {
                    System.err.println("-listPath requires a path");
                    System.exit(0);
                }
            }
        }

        Search search = new Search(projectPath);
        System.out.println("\nAnalyzing project: " + projectPath);
        search.listOfMethodSearch(listPath);


    }

}
