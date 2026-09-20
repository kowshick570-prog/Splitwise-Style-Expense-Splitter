package com.splitwise;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a person in the expense-splitting group.
 * Demonstrates: Encapsulation, Data Abstraction
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public class Person {

    // --- Encapsulated Fields ---
    private String name;
    private double totalPaid;       // Total amount this person has paid
    private double totalShare;      // Total amount this person owes

    // Tracks individual payment records: <description, amount>
    private Map<String, Double> paymentHistory;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Person(String name) {
        this.name = name;
        this.totalPaid = 0.0;
        this.totalShare = 0.0;
        this.paymentHistory = new HashMap<>();
    }

    // -------------------------------------------------------
    // Business Methods
    // -------------------------------------------------------

    /**
     * Records that this person paid for an expense.
     *
     * @param description label for the expense
     * @param amount      amount paid
     */
    public void addPayment(String description, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        totalPaid += amount;
        paymentHistory.put(description, paymentHistory.getOrDefault(description, 0.0) + amount);
    }

    /**
     * Adds to the share this person must pay.
     *
     * @param amount share amount
     */
    public void addShare(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Share amount cannot be negative.");
        }
        totalShare += amount;
    }

    /**
     * Net balance = what they paid − what they owe.
     * Positive  → others owe this person money.
     * Negative  → this person owes money to others.
     *
     * @return net balance
     */
    public double getNetBalance() {
        return totalPaid - totalShare;
    }

    // -------------------------------------------------------
    // Getters / Setters (Encapsulation)
    // -------------------------------------------------------
    public String getName()                        { return name; }
    public double getTotalPaid()                   { return totalPaid; }
    public double getTotalShare()                  { return totalShare; }
    public Map<String, Double> getPaymentHistory() { return new HashMap<>(paymentHistory); }

    @Override
    public String toString() {
        return String.format("Person{name='%s', paid=%.2f, share=%.2f, balance=%.2f}",
                name, totalPaid, totalShare, getNetBalance());
    }
}
