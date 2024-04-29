import java.util.Random;
import java.io.Serializable;

/**
 * Represents an individual tree characterized by species, year of planting,
 * height, and annual growth rate. The class implements Serializable to allow
 * tree objects to be saved to a file system for persistent storage.
 */
public class Tree implements Serializable {

    private static final int MIN_YEAR_PLANTED = 2000;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_NEW_HEIGHT = 20;
    private static final int MIN_GROWTH_RATE = 10;
    private int yearPlanted;
    private double treeHeight;
    private double treeGrowthRate;
    private Species treeSpecies;

    /**
     * Enum representing various tree species. Provides a list of commonly
     * known species plus an UNKNOWN category for unspecified types.
     */
    public enum Species {
        BIRCH, MAPLE, FIR, UNKNOWN
    }

    /**
     * Default constructor that initializes a tree with unknown species,
     * zero for year planted, height, and growth rate.
     */
    public Tree() {
        this.treeSpecies = Species.UNKNOWN;
        this.yearPlanted = 0;
        this.treeHeight = 0.0;
        this.treeGrowthRate = 0.0;
    }

    /**
     * Constructs a new tree with specified attributes.
     *
     * @param treeSpecies The species of the tree.
     * @param yearPlanted The year the tree was planted, expected to be no earlier than 2000.
     * @param treeHeight The initial height of the tree in feet, expected to be at least 10 feet.
     * @param treeGrowthRate The annual growth rate of the tree as a percentage, expected to start from 10%.
     */
    public Tree(Species treeSpecies, int yearPlanted, double treeHeight, double treeGrowthRate) {
        this();
        this.treeSpecies = treeSpecies;
        this.yearPlanted = yearPlanted;
        this.treeHeight = treeHeight;
        this.treeGrowthRate = treeGrowthRate;
    }

    /**
     * Gets the species of the tree.
     *
     * @return The species of the tree.
     */
    public Species getTreeSpecies() {
        return this.treeSpecies;
    }

    /**
     * Gets the year the tree was planted.
     *
     * @return The year the tree was planted.
     */
    public int getYearPlanted() {
        return this.yearPlanted;
    }

    /**
     * Gets the current height of the tree.
     *
     * @return The height of the tree in feet.
     */
    public double getTreeHeight() {
        return this.treeHeight;
    }

    /**
     * Gets the annual growth rate of the tree.
     *
     * @return The growth rate of the tree as a percentage.
     */
    public double getTreeGrowthRate() {
        return this.treeGrowthRate;
    }

    /**
     * Provides a string representation of the tree's details including species,
     * year planted, height, and growth rate.
     *
     * @return A formatted string describing the tree.
     */
    @Override
    public String toString() {
        return String.format("%-4s %4d %6.2f' %5.1f%%\n",
                treeSpecies,
                yearPlanted,
                treeHeight,
                treeGrowthRate);
    }

    /**
     * Generates a random tree with random attributes for species, year of planting,
     * initial height, and growth rate. Ensures that the species is not UNKNOWN.
     *
     * @return A randomly generated tree with valid properties.
     */
    public static Tree createRandomTree() {
        Random rand = new Random();
        Species randomTreeSpecies = Species.values()[rand.nextInt(Species.values().length - 1)]; // Exclude UNKNOWN
        int randomYearPlanted = rand.nextInt(25) + MIN_YEAR_PLANTED; // Years range from 2000 to 2024
        double randomTreeHeight = MIN_HEIGHT + (MAX_NEW_HEIGHT * rand.nextDouble()); // Height from 10 to 30 feet
        double randomTreeGrowthRate = (MIN_GROWTH_RATE + (MIN_GROWTH_RATE * rand.nextDouble())) / 100.0; // Growth rate from 10% to 20%

        return new Tree(randomTreeSpecies, randomYearPlanted, randomTreeHeight, randomTreeGrowthRate);
    }

    /**
     * Simulates the growth of the tree over one year by increasing its height
     * according to its growth rate.
     */
    public void individualTreeGrowth() {
        treeHeight *= 1 + (treeGrowthRate / 100); // Simulates 1 year of growth
    }

} // end of Tree class
