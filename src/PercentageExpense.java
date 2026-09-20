package com.splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Splits an expense by custom percentages.
 * Demonstrates: Inheritance, Method Overriding (Polymorphism)
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public class PercentageExpense extends Expense {

    private Map<Person, Double> percentages; // Person → percentage (0–100)

    public PercentageExpense(String description, double totalAmount,
                             Person paidBy, List<Person> participants,
                             Map<Person, Double> percentages) {
        super(description, totalAmount, paidBy, participants);

        // Validate: percentages must sum to 100
        double sum = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - 100.0) > 0.01) {
            throw new IllegalArgumentException(
                    "Percentages must sum to 100. Got: " + sum);
        }
        this.percentages = percentages;
    }

    /**
     * Each participant pays their percentage of the total.
     */
    @Override
    public Map<Person, Double> split() {
        Map<Person, Double> splitMap = new HashMap<>();

        for (Person p : participants) {
            double pct   = percentages.getOrDefault(p, 0.0);
            double share = (totalAmount * pct) / 100.0;
            splitMap.put(p, share);
            p.addShare(share);
        }

        paidBy.addPayment(description, totalAmount);
        return splitMap;
    }

    @Override
    public String getSplitType() {
        return "PERCENTAGE";
    }
}
