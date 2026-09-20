package com.splitwise;

/**
 * Represents a single payment needed to settle a debt between two people.
 * Demonstrates: Encapsulation, Value Object pattern
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public class Settlement {

    private final Person from;    // Person who needs to pay
    private final Person to;      // Person who receives the money
    private final double amount;  // Amount to be transferred

    public Settlement(Person from, Person to, double amount) {
        this.from   = from;
        this.to     = to;
        this.amount = amount;
    }

    public Person getFrom()   { return from;   }
    public Person getTo()     { return to;     }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("%s pays ₹%.2f to %s", from.getName(), amount, to.getName());
    }
}
