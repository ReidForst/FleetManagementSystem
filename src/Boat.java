import java.io.Serializable;
//=================================================================================================
/**
 * The Boat class holds information about a single boat, including its type, name, year, make/model,
 * price, and expenses. It allows adding expenses and generating a string representation of the boat.
 */
public class Boat implements Serializable {
    //-------------------------------------------------------------------------------------------------
    private final BoatType type;
    private final String name;
    private final int year;
    private final String makeModel;
    private final double length;
    private final double price;
    private double expenses;
    //-------------------------------------------------------------------------------------------------
    /**
     * Constructor to create a new boat.
     * @param type The type of the boat (SAILING or POWER).
     * @param name The name of the boat.
     * @param year The year the boat was manufactured.
     * @param makeModel The make/model of the boat.
     * @param length The length of the boat in feet.
     * @param price The purchase price of the boat.
     */
    public Boat(BoatType type, String name, int year, String makeModel, double length, double price) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.price = price;
        this.expenses = 0.0;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Returns the name of the boat.
     * @return The name of the boat.
     */
    public String getName() {
        return name;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Returns the price of the boat.
     * @return The price of the boat.
     */
    public double getPrice() {
        return price;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Returns the total expenses of the boat.
     * @return The total expenses.
     */
    public double getExpenses() {
        return expenses;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds an expense to the boat. Returns the remaining balance if the expense is within the allowed limit.
     * @param amount The amount to add as an expense.
     * @return The remaining allowance for spending.
     */
    public double addExpense(double amount) {
        double remainingAllowance = price - expenses; // Calculate the remaining balance
        if (amount <= remainingAllowance) {
            expenses += amount;  // Add the expense if it's within the allowable limit
            return expenses;  // Return the new total spent
        }
        return remainingAllowance;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Generates a string representation of the boat's information.
     * @return A string containing the boat's details.
     */
    public String toString() {
        return String.format("%-8s %-20s %4d %-12s %3.0f' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, makeModel, length, price, expenses);
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================