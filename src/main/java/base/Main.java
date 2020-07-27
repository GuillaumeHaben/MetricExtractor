package base;

import java.io.IOException;

public class Main {

    public static void main(String [] args) throws IOException {
        // ----- ARGS -----
        int i = 0;
        String arg;
        String projectPath = "";
        String listPath = "";

        // Usage
        if (args.length == 0) {
            System.out.println("Usage: ./MetricExtractor.sh\n");
            System.out.println("* INTERACTIVE *");
            System.out.println("-interactive : Open an interactive menu\n");
            System.out.println("* OPTION BASED *");
            System.out.println("-projectPath : Path to the project sources. ex: -projectPath /sample/path\n");
            System.out.println("Following options all require projectPath.");
            System.out.println("-listPath : Path to a file.txt containing className.methodName per line. ex: -listPath /sample/list.txt");
            System.out.println("-getAllMethods : Get Metrics for all Methods (CUT)");
            System.out.println("-getAllTestMethods : Get Metrics for all Test Methods");
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
            else if (arg.equals("-interactive")) {
                Menu menu = new Menu();
                System.exit(0);
            }
            else if (arg.equals("-getAllMethods")) {
                Search search = new Search(projectPath);
                System.out.println("\nAnalyzing project: " + projectPath);
                search.getAllMethods();
                System.exit(0);
            }
            else if (arg.equals("-getAllTestMethods")) {
                Search search = new Search(projectPath);
                System.out.println("\nAnalyzing project: " + projectPath);
                search.getAllTestMethods();
                System.exit(0);
            }
        }

        if (!projectPath.equals("") && !listPath.equals("")) {
            Search search = new Search(projectPath);
            System.out.println("\nAnalyzing project: " + projectPath);
            search.listOfMethodSearch(listPath);
        }

        System.exit(0);
    }
}
