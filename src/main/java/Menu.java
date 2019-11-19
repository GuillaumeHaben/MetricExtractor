import java.io.IOException;
import java.util.Scanner;

public class Menu {
    // [Dev] Hard coded project path
    String projectPath = "/Users/guillaume.haben/Documents/Work/projects/alibaba/fastjson/";
    // String projectPath = "/Users/guillaume.haben/Documents/Work/projects/TooTallNate/Java-WebSocket/";
    // String projectPath = "/Users/guillaume.haben/Documents/Work/projects/http-request/";

    public Menu() throws IOException {
        // Welcome
        System.out.println("########################");
        System.out.println("### Metric Extractor ###");
        System.out.println("########################\n");

        // Menu Selection
        menuSelection();
    }

    public void menuSelection() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Select a type of search");
        System.out.println("------------------------\n");
        System.out.println("1 - List of Methods search");
        System.out.println("2 - Method search");

        // Check if input is int
        if(!input.hasNextInt()) {
            System.out.println("Please select between [1-2]");
            menuSelection();
        }

        int selection = input.nextInt();
        Search search = new Search(this.projectPath);
        System.out.println("\nAnalyzing project: " + this.projectPath);
        switch (selection) {
            case 1:
                search.listOfMethodSearch("");
                break;
            case 2:
                search.singleMethodSearch();
                break;
            default:
                System.out.println("Please select between [1-2]");
                menuSelection();
        }
    }
}
