package com.splitwise;

import java.util.List;
import java.util.Map;

/**
 * Abstract base class for all expense types.
 * Demonstrates: Abstraction, Inheritance, Polymorphism
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public abstract class Expense {

    // --- Protected fields accessible to subclasses ---
    protected String description;
    protected double totalAmount;
    protected Person paidBy;
    protected List<Person> participants;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Expense(String description, double totalAmount, Person paidBy, List<Person> participants) {
        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive.");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("At least one participant required.");
        }
        this.description  = description;
        this.totalAmount  = totalAmount;
        this.paidBy       = paidBy;
        this.participants = participants;
    }

    // -------------------------------------------------------
    // Abstract Method — subclasses must implement
    // -------------------------------------------------------

    /**
     * Splits the expense and returns a map of Person → amount owed.
     *
     * @return split breakdown
     */
    public abstract Map<Person, Double> split();

    /**
     * Returns a short label describing the split type.
     */
    public abstract String getSplitType();

    // -------------------------------------------------------
    // Getters (Encapsulation)
    // -------------------------------------------------------
    public String      getDescription()  { return description;  }
    public double      getTotalAmount()  { return totalAmount;  }
    public Person      getPaidBy()       { return paidBy;       }
    public List<Person> getParticipants(){ return participants; }

    @Override
    public String toString() {
        return String.format("[%s] '%s' — ₹%.2f paid by %s (%d participants)",
                getSplitType(), description, totalAmount, paidBy.getName(), participants.size());
    }
}
