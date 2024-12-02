import java.io.*;
import java.util.*;
//=================================================================================================
/**
 * This class manages the Fleet System. It allows adding, removing boats, printing fleet data,
 * and managing expenses.
 */
public class FleetManagementSystem {
    //-------------------------------------------------------------------------------------------------
    private static final Scanner keyboard = new Scanner(System.in);
    private static final String DATABASE_FILE = "FleetData.db";
    private Fleet fleet;
    //-------------------------------------------------------------------------------------------------
    /**
     * Main method to start the Fleet Management System.
     * @param args Command-line arguments. If provided, will load data from CSV.
     */
    public static void main(String[] args) {
        FleetManagementSystem system = new FleetManagementSystem();
        system.start(args);
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Starts the system. If CSV data is provided, it loads from CSV. Otherwise, it loads from a saved database.
     * @param args Command-line arguments.
     */
    private void start(String[] args) {
        if (args.length > 0) {
            loadFromCSV(args[0]);
        } else {
            loadFromDatabase();
        }

        char option;
        do {
            printMenu();
            option = Character.toUpperCase(keyboard.nextLine().charAt(0));
            handleMenuOption(option);
        } while (option != 'X');

        saveToDatabase();
        System.out.println("Exiting the Fleet Management System");
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Loads fleet data from a CSV file.
     * @param fileName The CSV file to load the data from.
     */
    private void loadFromCSV(String fileName) {
        try {
            fleet = new Fleet();
            fleet.loadFromCSV(fileName);
            System.out.println("Fleet data loaded from CSV file.");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load fleet data from CSV file.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Loads fleet data from a saved database file.
     */
    private void loadFromDatabase() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            fleet = (Fleet) in.readObject();
            System.out.println("Fleet data loaded from database.");
        } catch (IOException | ClassNotFoundException e) {
            fleet = new Fleet();
            System.out.println("No existing fleet data found. Starting fresh.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Saves the current fleet data to a database file.
     */
    private void saveToDatabase() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            out.writeObject(fleet);
            System.out.println("Fleet data saved to database.");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to save fleet data.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Prints the menu of options available to the user.
     */
    private void printMenu() {
        System.out.println("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Handles the user's menu selection.
     * @param option The option selected by the user.
     */
    private void handleMenuOption(char option) {
        switch (option) {
            case 'P':
                System.out.println(fleet.generateReport());
                break;
            case 'A':
                addBoat();
                break;
            case 'R':
                removeBoat();
                break;
            case 'E':
                manageExpense();
                break;
            case 'X':
                break;
            default:
                System.out.println("Invalid menu option, try again.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds a new boat to the fleet by reading its details from the user input.
     */
    private void addBoat() {
        System.out.print("Please enter the new boat CSV data: ");
        String csvData = keyboard.nextLine();
        if (fleet.addBoatFromCSV(csvData)) {
            System.out.println("Boat added successfully.");
        } else {
            System.out.println("Failed to add boat. Check your input.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Removes a boat from the fleet by its name.
     */
    private void removeBoat() {
        System.out.print("Which boat do you want to remove? : ");
        String name = keyboard.nextLine();
        if (fleet.removeBoat(name)) {
            System.out.println("Boat removed successfully.");
        } else {
            System.out.println("Cannot find boat " + name);
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Manages the expenses of a boat by checking if the user can spend money on it.
     */
    private void manageExpense() {
        System.out.print("Which boat do you want to spend on? : ");
        String name = keyboard.nextLine();

        if (!fleet.boatExists(name)) {
            System.out.println("Cannot find boat " + name + ".");
            return;
        }

        System.out.print("How much do you want to spend?      : ");
        double amount = keyboard.nextDouble();
        keyboard.nextLine(); // Clear newline character

        // Check if the expense can be added
        double remainingAllowance = fleet.getRemainingAllowance(name);  // Get remaining balance

        if (remainingAllowance >= amount) {
            // If the expense is within the allowed limit, add it and confirm
            if (fleet.addExpense(name, amount)) {
                // Round the result to 2 decimal places
                double totalSpent = fleet.findBoatByName(name).getExpenses();
                System.out.println("Expense authorized, $" + String.format("%.2f", totalSpent) + " spent.");
            }
        } else {
            // If the expense is too high, show how much can still be spent
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", remainingAllowance);
        }
    }
//-------------------------------------------------------------------------------------------------
}

