import java.io.*;
import java.util.*;
//=================================================================================================
/**
 * This class manages a collection of boats, allowing the user to add boats, remove boats,
 * and manage their expenses. It can load data from a CSV file and save the fleet to a database.
 */
public class Fleet implements Serializable {
    //-------------------------------------------------------------------------------------------------
    private final List<Boat> boats = new ArrayList<>();
    //-------------------------------------------------------------------------------------------------
    /**
     * Loads the fleet data from a CSV file.
     * @param fileName The name of the CSV file to load the data from.
     * @throws IOException If the file cannot be read.
     */
    public void loadFromCSV(String fileName) throws IOException {
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                addBoatFromCSV(fileScanner.nextLine());
            }
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds a boat to the fleet by parsing the CSV data.
     * @param csvData The CSV string representing a boat's information.
     * @return true if the boat was added successfully, false if there was an error.
     */
    public boolean addBoatFromCSV(String csvData) {
        try {
            String[] parts = csvData.split(",");
            BoatType type = BoatType.valueOf(parts[0].toUpperCase());
            String name = parts[1];
            int year = Integer.parseInt(parts[2]);
            String makeModel = parts[3];
            double length = Double.parseDouble(parts[4]);
            double price = Double.parseDouble(parts[5]);

            boats.add(new Boat(type, name, year, makeModel, length, price));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Removes a boat from the fleet by its name.
     * @param name The name of the boat to remove.
     * @return true if the boat was removed, false if it was not found.
     */
    public boolean removeBoat(String name) {
        return boats.removeIf(boat -> boat.getName().equalsIgnoreCase(name));
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds an expense to a boat's account.
     * @param name The name of the boat to add the expense to.
     * @param amount The amount to spend.
     * @return true if the expense was successfully added, false if not.
     */
    public boolean addExpense(String name, double amount) {
        Boat boat = findBoatByName(name);
        if (boat != null) {
            double remainingAllowance = boat.addExpense(amount);  // Calling the Boat's addExpense method
            if (remainingAllowance >= 0) {
                return true;  // If the expense was successfully added
            } else {
                System.out.printf("Expense not permitted, only $%.2f left to spend.\n", -remainingAllowance);  // Show remaining balance
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Generates a report of all boats and their expenses.
     * @return A string containing the fleet's report.
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder("Fleet report:\n");
        double totalPrice = 0, totalExpenses = 0;

        for (Boat boat : boats) {
            report.append(boat).append("\n");
            totalPrice += boat.getPrice();
            totalExpenses += boat.getExpenses();
        }

        report.append(String.format("Total : Paid $ %.2f : Spent $ %.2f\n", totalPrice, totalExpenses));
        return report.toString();
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Finds a boat by its name.
     * @param name The name of the boat to find.
     * @return The Boat object if found, or null if not found.
     */
    public Boat findBoatByName(String name) {
        for (Boat boat : boats) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null; // Return null if no boat is found
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Checks if a boat exists in the fleet by name.
     * @param name The name of the boat to check.
     * @return true if the boat exists, false if it does not.
     */
    public boolean boatExists(String name) {
        for (Boat boat : boats) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Gets the remaining allowance for a boat based on its purchase price and expenses.
     * @param name The name of the boat to check.
     * @return The remaining allowance for the boat, or -1 if the boat is not found.
     */
    public double getRemainingAllowance(String name) {
        Boat boat = findBoatByName(name);
        if (boat != null) {
            return boat.getPrice() - boat.getExpenses();
        }
        return -1.0; // Indicate that the boat was not found
    }

//-------------------------------------------------------------------------------------------------
}
//=================================================================================================