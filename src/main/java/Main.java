import java.io.IOException;

public class Main {

    public static void main(String [] args) throws IOException {
        // ----- ARGS -----
        int i = 0;
        String arg;
        String projectPath = "";
        String listPath = "";
        String getAllTestMethods = "";

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
            else if (arg.equals("-getAllTestMethods")) {
                if (i < args.length) {
                    getAllTestMethods = args[i++];
                } else {
                    System.err.println("-getAllMethods requires a path");
                    System.exit(0);
                }
            }
        }

        if (!projectPath.equals("") && !listPath.equals("")) {
            Search search = new Search(projectPath);
            System.out.println("\nAnalyzing project: " + projectPath);
            search.listOfMethodSearch(listPath);
        }

        if (!getAllTestMethods.equals("")) {
            Search search = new Search(getAllTestMethods);
            System.out.println("\nAnalyzing project: " + getAllTestMethods);
            search.getAllTestMethods();
        }

        System.exit(0);

    }

}
