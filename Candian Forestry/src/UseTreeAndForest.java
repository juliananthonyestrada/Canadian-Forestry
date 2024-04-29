import java.util.Scanner;

/**
 * This class serves as the main entry point for a forestry simulation program,
 * managing forests through an interactive menu-driven interface. It allows users
 * to perform various operations on a forest such as adding, cutting, and simulating
 * growth of trees, as well as loading and saving forests.
 */
public class UseTreeAndForest {

    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Main method that starts the forestry simulation. It processes command line
     * arguments as names of forests to be loaded and manipulated.
     *
     * @param args Command line arguments, expected to be names of forests.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");

        Forest myForest;
        boolean continueSimulation = true; // flag to control simulation loop
        int currArgIndex = 0;

        while (continueSimulation && currArgIndex < args.length) {
            String forestName = args[currArgIndex];
            System.out.println("Initializing forest from: " + forestName + "\n");
            myForest = Forest.nextForest(forestName); // Attempt to load the forest

            if (myForest != null) {
                continueSimulation = executeMenu(myForest); // If the forest is successfully loaded then we will run the menu
            } else {
                System.out.println("Failed to load forest: " + forestName);
            }
            currArgIndex++; // Move to the next forest when X is entered or if the forest is not loaded right
        }
        System.out.println("\nExiting the simulation");
    }

    /**
     * Executes the menu-driven interface for interacting with a forest. Allows
     * users to perform various operations based on their menu selection.
     *
     * @param myForest The forest on which operations will be performed.
     * @return true to move to the next forest if any, false to end the simulation.
     */
    public static boolean executeMenu(Forest myForest) {
        Forest nextForest;
        boolean running = true; // Flag to control loop
        while (running) {
            printMenu();
            String choice = menuChoice();
            switch (choice) {
                case "P":
                    myForest.printForest();
                    break;
                case "A":
                    myForest.addRandomTree();
                    break;
                case "C":
                    int indexToCut = validateTreeIndex(myForest); // Read and validate index
                    myForest.cutTreeByIndex(indexToCut);
                    break;
                case "G":
                    myForest.simulateTreeGrowth();
                    break;
                case "R":
                    int heightToReap = validateHeightToReapFrom(); // Read and validate height to reap from
                    myForest.reapForest(heightToReap);
                    break;
                case "S":
                    Forest.saveForest(myForest);
                    break;
                case "L":
                    System.out.println("Enter forest name:");
                    String forestName = keyboard.nextLine() + ".db";
                    if ((nextForest = Forest.loadForest(forestName)) != null) {
                        myForest = nextForest;
                    } else {
                        System.out.println("Old forest retained");
                    }
                    break;
                case "N":
                    System.out.println("Moving to the next forest");
                    return true;
                case "X":
                    running = false;
                    return running;
                default:
                    System.out.println("Invalid menu option, try again.");
                    break;
            }
        }
        return true; // After running becomes false, the method ends, indicating the simulation should end
    }

    /**
     * Displays the menu of operations that can be performed on the forest.
     */
    public static void printMenu() {
        System.out.println("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it : ");
    }

    /**
     * Reads the user's choice from the console, ensuring it is a valid menu option.
     *
     * @return The user's menu choice as an uppercase string.
     */
    public static String menuChoice() {
        String choice = keyboard.nextLine().toUpperCase();
        while (!choice.equals("P") && !choice.equals("A") &&
                !choice.equals("C") && !choice.equals("G") &&
                !choice.equals("R") && !choice.equals("S") &&
                !choice.equals("L") && !choice.equals("N") &&
                !choice.equals("X")) {
            System.out.println("Invalid menu option, try again");
            printMenu();
            choice = keyboard.nextLine().toUpperCase();
        }
        return choice;
    }

    /**
     * Validates the tree index input by the user, ensuring it corresponds to an existing tree.
     *
     * @param myForest The forest from which a tree index is to be validated.
     * @return A valid tree index as an integer.
     */
    public static int validateTreeIndex(Forest myForest) {
        int indexToCutDown = -1; // Initialize to an invalid value to enter the loop
        boolean valid = false;   // Flag to monitor valid input

        while (!valid) {
            try {
                System.out.println("Tree to cut down:");
                indexToCutDown = keyboard.nextInt();
                keyboard.nextLine(); // Consume the leftover newline
                if (indexToCutDown >= 0 && indexToCutDown < myForest.trees.size()) {
                    valid = true; // Set flag to true if index is valid
                } else {
                    System.out.println("Error. Tree " + indexToCutDown + " does not exist. Tree to cut down:");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("That is not an integer.");
                keyboard.nextLine(); // Consume the bad input to reset scanner
            }
        }

        return indexToCutDown; // Return the validated index
    }

    /**
     * Validates the height input by the user for reaping trees, ensuring it is a non-negative integer.
     *
     * @return A valid height from which trees will be reaped.
     */
    public static int validateHeightToReapFrom() {
        int heightToReapFrom = -1; // Initialize to an invalid value to enter the loop
        boolean valid = false;     // Flag to monitor valid input

        while (!valid) {
            try {
                System.out.println("Height to reap from:");
                heightToReapFrom = keyboard.nextInt(); // Attempt to read the integer
                keyboard.nextLine(); // Consume the leftover newline
                if (heightToReapFrom >= 0) {
                    valid = true; // Set flag to true if height is valid
                } else {
                    System.out.println("Error. That is not a valid height.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("That is not an integer.");
                keyboard.nextLine(); // Consume the bad input to reset scanner
            }
        }

        return heightToReapFrom; // Return the validated height
    }
}
