package com.splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Splits an expense by exact custom amounts per person.
 * Demonstrates: Inheritance, Method Overriding (Polymorphism)
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public class ExactExpense extends Expense {

    private Map<Person, Double> exactAmounts; // Person → exact share amount

    public ExactExpense(String description, double totalAmount,
                        Person paidBy, List<Person> participants,
                        Map<Person, Double> exactAmounts) {
        super(description, totalAmount, paidBy, participants);

        // Validate: exact amounts must sum to totalAmount
        double sum = exactAmounts.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new IllegalArgumentException(
                    "Exact amounts must sum to totalAmount=" + totalAmount + ". Got: " + sum);
        }
        this.exactAmounts = exactAmounts;
    }

    /**
     * Each participant pays their exact specified amount.
     */
    @Override
    public Map<Person, Double> split() {
        Map<Person, Double> splitMap = new HashMap<>();

        for (Person p : participants) {
            double share = exactAmounts.getOrDefault(p, 0.0);
            splitMap.put(p, share);
            p.addShare(share);
        }

        paidBy.addPayment(description, totalAmount);
        return splitMap;
    }

    @Override
    public String getSplitType() {
        return "EXACT";
    }
}
