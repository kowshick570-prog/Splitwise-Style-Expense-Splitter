package com.splitwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a group of people and their shared expenses.
 * Demonstrates: Encapsulation, Composition, Collection handling
 *
 * @author Kowshick K
 * @regNo 711524BEE302
 */
public class Group {

    private String            groupName;
    private List<Person>      members;
    private List<Expense>     expenses;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Group(String groupName) {
        this.groupName = groupName;
        this.members   = new ArrayList<>();
        this.expenses  = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Member Management
    // -------------------------------------------------------

    public void addMember(Person person) {
        for (Person m : members) {
            if (m.getName().equalsIgnoreCase(person.getName())) {
                throw new IllegalArgumentException("Member '" + person.getName() + "' already exists.");
            }
        }
        members.add(person);
    }

    public Person getMemberByName(String name) {
        for (Person p : members) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        throw new IllegalArgumentException("Member '" + name + "' not found in group.");
    }

    // -------------------------------------------------------
    // Expense Management
    // -------------------------------------------------------

    /**
     * Adds an expense to the group and immediately applies the split.
     *
     * @param expense the expense to add
     * @return the split breakdown
     */
    public Map<Person, Double> addExpense(Expense expense) {
        expenses.add(expense);
        return expense.split();
    }

    // -------------------------------------------------------
    // Settlement Calculation
    // -------------------------------------------------------

    /**
     * Calculates the minimum set of transactions needed to settle all debts.
     * Uses a greedy algorithm: match the biggest creditor with the biggest debtor.
     *
     * @return list of Settlement objects
     */
    public List<Settlement> calculateSettlements() {
        // Build net-balance map
        Map<Person, Double> balance = new HashMap<>();
        for (Person p : members) {
            balance.put(p, p.getNetBalance());
        }

        List<Settlement> settlements = new ArrayList<>();

        // Greedy settlement loop
        while (true) {
            Person maxCreditor = null;
            Person maxDebtor   = null;
            double maxCredit   = 0.001;  // ignore near-zero balances
            double maxDebt     = 0.001;

            for (Map.Entry<Person, Double> e : balance.entrySet()) {
                if (e.getValue() > maxCredit) {
                    maxCredit   = e.getValue();
                    maxCreditor = e.getKey();
                }
                if (e.getValue() < -maxDebt) {
                    maxDebt   = -e.getValue();
                    maxDebtor = e.getKey();
                }
            }

            if (maxCreditor == null || maxDebtor == null) break;

            double amount = Math.min(maxCredit, maxDebt);
            settlements.add(new Settlement(maxDebtor, maxCreditor, amount));

            balance.put(maxCreditor, balance.get(maxCreditor) - amount);
            balance.put(maxDebtor,   balance.get(maxDebtor)   + amount);
        }

        return settlements;
    }

    // -------------------------------------------------------
    // Report
    // -------------------------------------------------------

    public void printSummary() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.printf ("║  GROUP : %-36s║%n", groupName);
        System.out.println("╠══════════════════════════════════════════════╣");

        System.out.println("║  MEMBERS & BALANCES                          ║");
        for (Person p : members) {
            double bal = p.getNetBalance();
            String status = bal > 0 ? "gets back" : (bal < 0 ? "owes      " : "settled   ");
            System.out.printf("║   %-12s │ %-9s ₹%-10.2f      ║%n",
                    p.getName(), status, Math.abs(bal));
        }

        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  EXPENSES                                    ║");
        for (Expense e : expenses) {
            System.out.printf("║   %s%n", e);
        }

        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  SETTLEMENTS (minimum transactions)          ║");
        List<Settlement> settlements = calculateSettlements();
        if (settlements.isEmpty()) {
            System.out.println("║   All settled! No transactions needed.       ║");
        } else {
            for (Settlement s : settlements) {
                System.out.printf("║   %-12s → %-12s  ₹%-8.2f   ║%n",
                        s.getFrom().getName(), s.getTo().getName(), s.getAmount());
            }
        }
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------
    public String        getGroupName() { return groupName; }
    public List<Person>  getMembers()   { return new ArrayList<>(members); }
    public List<Expense> getExpenses()  { return new ArrayList<>(expenses); }
}
