package com.splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Splits an expense equally among all participants.
 * Demonstrates: Inheritance, Method Overriding (Polymorphism)
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public class EqualExpense extends Expense {

    public EqualExpense(String description, double totalAmount,
                        Person paidBy, List<Person> participants) {
        super(description, totalAmount, paidBy, participants);
    }

    /**
     * Each participant pays an equal share.
     */
    @Override
    public Map<Person, Double> split() {
        double share = totalAmount / participants.size();
        Map<Person, Double> splitMap = new HashMap<>();

        for (Person p : participants) {
            splitMap.put(p, share);
            p.addShare(share);
        }

        // Record the payment for the payer
        paidBy.addPayment(description, totalAmount);
        return splitMap;
    }

    @Override
    public String getSplitType() {
        return "EQUAL";
    }
}
