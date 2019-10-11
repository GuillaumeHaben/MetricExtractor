import java.util.Scanner;

public class Menu {

    public Menu() {
        // Welcome
        System.out.println("########################");
        System.out.println("### Metric Extractor ###");
        System.out.println("########################\n");

        // Menu Selection
        menuSelection();
    }

    public static void menuSelection() {
        Scanner input = new Scanner(System.in);
        System.out.println("Select a type of search");
        System.out.println("------------------------\n");
        System.out.println("1 - Project search");
        System.out.println("2 - Class search");
        System.out.println("3 - Method search");

        // Check if input is int
        if(!input.hasNextInt()) {
            System.out.println("Please select between [1-3]");
            menuSelection();
        }

        int selection = input.nextInt();
        // [Dev] Hard coded project path
        String projectPath = "/Users/guillaume.haben/Documents/Work/projects/TooTallNate/Java-WebSocket/";
        Search search = new Search(projectPath);
        System.out.println("Analyzing project: " + projectPath);
        switch (selection) {
            case 1:
                System.out.println("1 selected");
                System.out.println("Work in Progress");
                break;
            case 2:
                System.out.println("2 selected");
                System.out.println("Work in Progress");
                break;
            case 3:
                search.methodSearch();
                break;
            default:
                System.out.println("Please select between [1-3]");
                menuSelection();
        }
    }
}