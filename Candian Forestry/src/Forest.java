import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * Represents a forest which manages a collection of trees. This class supports operations
 * such as adding, removing, simulating growth, and replacing trees based on certain conditions.
 * It also supports serialization for saving and loading forest data.
 */
public class Forest implements Serializable {

    protected ArrayList<Tree> trees;
    private String forestName;

    /**
     * Constructs a new, empty forest with no name and no trees.
     */
    public Forest() {
        this.forestName = null;
        this.trees = new ArrayList<>();
    }

    /**
     * Prints detailed information about all trees in the forest including each tree's index,
     * species, year planted, height, and growth rate. It also calculates and displays the total
     * number of trees and the average height of these trees.
     */
    public void printForest() {
        System.out.println("Forest name: " + this.forestName);
        int totalHeight = 0;
        for (int index = 0; index < trees.size(); index++) {
            Tree tree = trees.get(index);
            System.out.printf("%5d %-6s %4d %6.2f' %5.1f%%\n",
                    index,
                    tree.getTreeSpecies(),
                    tree.getYearPlanted(),
                    tree.getTreeHeight(),
                    tree.getTreeGrowthRate());
            totalHeight += tree.getTreeHeight(); // Continuously update the total height
        }
        double averageHeight = (double) totalHeight / trees.size(); // Calculate average height
        System.out.println("There are " + trees.size() + " trees, with an average height of " + String.format("%.2f", averageHeight) + " feet.\n");
    }

    /**
     * Adds a randomly generated tree to the forest. The properties of the tree are determined by
     * the static method {@link Tree#createRandomTree()}.
     */
    public void addRandomTree() {
        trees.add(Tree.createRandomTree()); // Create a random tree and add it to the ArrayList
    }

    /**
     * Removes a tree from the forest at the specified index.
     *
     * @param treeIndex The index of the tree to be removed within the list of trees.
     * @throws IndexOutOfBoundsException if the provided index is out of the range of tree list indices.
     */
    public void cutTreeByIndex(int treeIndex) {
        if (treeIndex >= 0 && treeIndex < trees.size()) {
            trees.remove(treeIndex); // Cut down the tree at the given index
        } else {
            throw new IndexOutOfBoundsException("Tree number " + treeIndex + " does not exist."); // Handles invalid indexes
        }
    }

    /**
     * Simulates the growth of all trees in the forest over one year. Each tree's height
     * is updated based on its individual growth rate.
     */
    public void simulateTreeGrowth() {
        for (Tree tree : trees) {
            tree.individualTreeGrowth(); // Simulates 1 year of tree growth for each tree in forest
        }
    }

    /**
     * Removes and replaces trees that exceed a specified height limit. Each tree that
     * exceeds the height limit is replaced with a new, randomly generated tree.
     *
     * @param treeHeightLimit The height limit above which trees are considered for reaping.
     */
    public void reapForest(int treeHeightLimit) {
        int index;
        for (index = 0; index < trees.size(); index++) {
            if (trees.get(index).getTreeHeight() >= treeHeightLimit) {
                System.out.print("Reaping the tall tree: " + trees.get(index));
                trees.set(index, Tree.createRandomTree()); // Replace the tall tree with a new random tree
                System.out.print("Replaced with a new tree: " + trees.get(index));
            }
        }
    }

    /**
     * Loads a forest from a CSV file containing tree data. Each line in the file
     * represents a single tree's data, which is converted back into a Tree object.
     *
     * @param filename The base name of the file from which to load the forest data.
     * @return A new Forest instance populated with trees from the file, or null if the file cannot be found.
     */
    public static Forest nextForest(String filename) {

        String csvFilename = filename + ".csv"; // Append ".csv" to filename to specify expected file format.

        ArrayList<Tree> treeData = new ArrayList<>(); // Create an ArrayList to hold the trees

        Forest newForest; // Declare a variable to hold the newly created Forest instance.

        try {

            Scanner scanner = new Scanner(new File(csvFilename)); // Open a scanner on the file specified by csvFilename

            // Read each line in the file
            while (scanner.hasNextLine()) {

                String[] data = scanner.nextLine().split(","); // Split the line by commas

                // Extract and convert the tree species, year planted, height, and growth rate from the string array.
                // Trim whitespace and convert to appropriate types.
                Tree.Species species = Tree.Species.valueOf(data[0].trim().toUpperCase());
                int yearPlanted = Integer.parseInt(data[1].trim());
                double height = Double.parseDouble(data[2].trim());
                double growthRate = Double.parseDouble(data[3].trim());

                treeData.add(new Tree(species, yearPlanted, height, growthRate)); // Create new tree object with respective values
            }

            // Instantiate a new Forest object and set its name and list of trees.
            newForest = new Forest();
            newForest.forestName = filename;
            newForest.trees = treeData;

            scanner.close(); // Close the scanner to free up resources.

            return newForest; // Return the newly created and populated Forest object.
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + csvFilename); // Handle non-existent file

            return null; // Return null to indicate that the forest could not be loaded due to the missing file.
        }
    }


    /**
     * Loads a forest from a binary file using serialization.
     *
     * @param fileName The file name from which to load the forest.
     * @return The loaded Forest object, or null if an error occurs during file operations or object deserialization.
     */
    public static Forest loadForest(String fileName) {
        // Use try-with-resources statement to automatically manage the ObjectInputStream resource.
        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Read the object from the file and cast it to a Forest type.
            return (Forest) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Catch both IOException and ClassNotFoundException.
            // IOException can occur if file is non-existent
            // ClassNotFoundException can occur if the JVM does not find the Forest class in the classpath during deserialization

            // Print an error message indicating what went wrong during the attempt to read and deserialize the object.
            System.out.println("Error opening/reading " + fileName + ": " + e.getMessage());

            // Return null to indicate that loading the Forest was unsuccessful due to the exceptions caught.
            return null;
        }
    }


    /**
     * Saves the current forest to a file using serialization.
     *
     * @param currForest The forest to be saved.
     * @return true if the forest is successfully saved, false otherwise.
     */
    public static boolean saveForest(Forest currForest) {
        String fileName = currForest.forestName + ".db"; // Rename the file properly

        // Try-with-resources statement ensures that the ObjectOutputStream is closed automatically
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Serialize the Forest object and write it to a file.
            outStream.writeObject(currForest);
            return true;
        } catch (IOException e) {
            // Catch IOException that could be thrown if there's a problem accessing the file, writing to the file,
            // or opening the file.
            System.out.println("Error in saving to " + fileName + ": " + e.getMessage());
            return false;
        }
    }


} // end of Forest class
