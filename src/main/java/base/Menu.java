package base;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
    // [Dev] Hard coded project path
    String projectPath;
    // = "/Users/guillaume.haben/Documents/Work/datasets/DeFlaker/projects/jackrabbit-oak/oak-jcr";
    // String projectPath = "/Users/guillaume.haben/Documents/Work/projects/TooTallNate/Java-WebSocket/";
    // String projectPath = "/Users/guillaume.haben/Documents/Work/projects/http-request/";

    public Menu() throws IOException {
        // Welcome
        System.out.println("########################");
        System.out.println("### Metric Extractor ###");
        System.out.println("########################\n");
        // Project Selection
        setProjectPath();
        // base.Menu Selection
        menuSelection();
    }
    public void setProjectPath() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter project sources (absolute path):");
        this.projectPath = myObj.nextLine();
        File f = new File(this.projectPath);
        if (!f.exists() || !f.isDirectory()) {
            System.out.println("Project path not found.");
            setProjectPath();
        }
    }

    public void menuSelection() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Select a type of search");
        System.out.println("------------------------\n");
        System.out.println("1 - List of Methods search");
        System.out.println("2 - Method search");
        System.out.println("3 - Get All Test Methods");
        System.out.println("4 - Get All Methods");

        // Check if input is int
        if(!input.hasNextInt()) {
            System.out.println("Please select between [1-4]");
            menuSelection();
        }

        int selection = input.nextInt();
        Search search = new Search(this.projectPath);
        System.out.println("\nAnalyzing project: " + this.projectPath);
        switch (selection) {
            case 1:
                search.listOfMethodSearch();
                break;
            case 2:
                search.singleMethodSearch();
                break;
            case 3:
                search.getAllTestMethods();
                break;
            case 4:
                search.getAllMethods();
                break;
            default:
                System.out.println("Please select between [1-4]");
                menuSelection();
        }
    }
}
