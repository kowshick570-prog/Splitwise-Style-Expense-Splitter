package com.splitwise;

import java.util.*;

/**
 * Main interactive CLI application for the Expense Splitter.
 * Demonstrates: All OOP pillars — Encapsulation, Inheritance,
 *               Polymorphism, Abstraction
 *
 * @author  Kowshick K
 * @regNo   711524BEE302
 * @subject Java OOP Mini Project
 */
public class SplitwiseApp {

    private static final Scanner sc = new Scanner(System.in);
    private static Group         currentGroup = null;

    // ================================================================
    //  MAIN ENTRY POINT
    // ================================================================
    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1  -> createGroup();
                case 2  -> addMember();
                case 3  -> addExpense();
                case 4  -> viewSummary();
                case 5  -> viewBalances();
                case 6  -> viewSettlements();
                case 0  -> { System.out.println("\n  Bye! Expenses settled ✔"); running = false; }
                default -> System.out.println("  ⚠ Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    // ================================================================
    //  BANNER
    // ================================================================
    private static void printBanner() {
        System.out.println("""
                ╔══════════════════════════════════════════════════╗
                ║      SPLITWISE-STYLE EXPENSE SPLITTER            ║
                ║      Java OOP Mini Project                       ║
                ║      Author  : Kowshick K                        ║
                ║      Reg No  : 711524BEE302                      ║
                ║      Dept    : B.E. EEE — KIT, Coimbatore        ║
                ╚══════════════════════════════════════════════════╝
                """);
    }

    // ================================================================
    //  MENU
    // ================================================================
    private static void printMainMenu() {
        System.out.println("""
                ┌─────────────────────────────┐
                │         MAIN MENU           │
                ├─────────────────────────────┤
                │  1. Create / Switch Group   │
                │  2. Add Member              │
                │  3. Add Expense             │
                │  4. View Group Summary      │
                │  5. View Balances           │
                │  6. View Settlements        │
                │  0. Exit                    │
                └─────────────────────────────┘""");
    }

    // ================================================================
    //  1. CREATE GROUP
    // ================================================================
    private static void createGroup() {
        System.out.print("\n  Group name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("  ⚠ Name cannot be empty."); return; }

        currentGroup = new Group(name);
        System.out.println("  ✔ Group '" + name + "' created!\n");
    }

    // ================================================================
    //  2. ADD MEMBER
    // ================================================================
    private static void addMember() {
        if (!checkGroup()) return;
        System.out.print("\n  Member name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("  ⚠ Name cannot be empty."); return; }

        try {
            currentGroup.addMember(new Person(name));
            System.out.println("  ✔ '" + name + "' added to group.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("  ⚠ " + e.getMessage());
        }
    }

    // ================================================================
    //  3. ADD EXPENSE
    // ================================================================
    private static void addExpense() {
        if (!checkGroup()) return;
        List<Person> members = currentGroup.getMembers();
        if (members.isEmpty()) {
            System.out.println("  ⚠ Add at least one member first.");
            return;
        }

        // Expense description
        System.out.print("\n  Expense description: ");
        String desc = sc.nextLine().trim();

        // Amount
        double amount = readDouble("  Total amount (₹): ");

        // Payer
        System.out.println("  Who paid?");
        listMembers(members);
        int payerIdx = readInt("  Choose payer (number): ") - 1;
        if (payerIdx < 0 || payerIdx >= members.size()) {
            System.out.println("  ⚠ Invalid choice."); return;
        }
        Person paidBy = members.get(payerIdx);

        // Participants
        System.out.println("  Select participants (comma-separated numbers, or 'all'):");
        listMembers(members);
        System.out.print("  → ");
        String input = sc.nextLine().trim();

        List<Person> participants;
        if (input.equalsIgnoreCase("all")) {
            participants = new ArrayList<>(members);
        } else {
            participants = new ArrayList<>();
            for (String s : input.split(",")) {
                try {
                    int idx = Integer.parseInt(s.trim()) - 1;
                    if (idx >= 0 && idx < members.size()) participants.add(members.get(idx));
                } catch (NumberFormatException ignored) {}
            }
        }
        if (participants.isEmpty()) { System.out.println("  ⚠ No valid participants selected."); return; }

        // Split type
        System.out.println("""
                  Split type:
                    1. Equal
                    2. Percentage
                    3. Exact amounts""");
        int splitType = readInt("  Choose split type: ");

        try {
            Expense expense = switch (splitType) {
                case 1 -> new EqualExpense(desc, amount, paidBy, participants);
                case 2 -> buildPercentageExpense(desc, amount, paidBy, participants);
                case 3 -> buildExactExpense(desc, amount, paidBy, participants);
                default -> { System.out.println("  ⚠ Invalid split type."); yield null; }
            };

            if (expense != null) {
                Map<Person, Double> split = currentGroup.addExpense(expense);
                System.out.println("\n  ✔ Expense added! Split breakdown:");
                split.forEach((p, v) ->
                        System.out.printf("     %-12s → ₹%.2f%n", p.getName(), v));
                System.out.println();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("  ⚠ " + e.getMessage());
        }
    }

    // ================================================================
    //  BUILD PERCENTAGE EXPENSE
    // ================================================================
    private static PercentageExpense buildPercentageExpense(
            String desc, double amount, Person paidBy, List<Person> participants) {

        Map<Person, Double> pct = new HashMap<>();
        System.out.println("  Enter percentage for each participant (must sum to 100):");
        for (Person p : participants) {
            double p_pct = readDouble("    " + p.getName() + " %: ");
            pct.put(p, p_pct);
        }
        return new PercentageExpense(desc, amount, paidBy, participants, pct);
    }

    // ================================================================
    //  BUILD EXACT EXPENSE
    // ================================================================
    private static ExactExpense buildExactExpense(
            String desc, double amount, Person paidBy, List<Person> participants) {

        Map<Person, Double> exact = new HashMap<>();
        System.out.println("  Enter exact amount for each participant (must sum to ₹" + amount + "):");
        for (Person p : participants) {
            double share = readDouble("    " + p.getName() + " ₹: ");
            exact.put(p, share);
        }
        return new ExactExpense(desc, amount, paidBy, participants, exact);
    }

    // ================================================================
    //  4. VIEW SUMMARY
    // ================================================================
    private static void viewSummary() {
        if (!checkGroup()) return;
        System.out.println();
        currentGroup.printSummary();
        System.out.println();
    }

    // ================================================================
    //  5. VIEW BALANCES
    // ================================================================
    private static void viewBalances() {
        if (!checkGroup()) return;
        System.out.println("\n  BALANCES in group '" + currentGroup.getGroupName() + "':");
        for (Person p : currentGroup.getMembers()) {
            double bal = p.getNetBalance();
            String status;
            if      (bal >  0.01) status = "gets back ₹" + String.format("%.2f", bal);
            else if (bal < -0.01) status = "owes      ₹" + String.format("%.2f", Math.abs(bal));
            else                  status = "is settled ✔";
            System.out.printf("    %-14s │ %s%n", p.getName(), status);
        }
        System.out.println();
    }

    // ================================================================
    //  6. VIEW SETTLEMENTS
    // ================================================================
    private static void viewSettlements() {
        if (!checkGroup()) return;
        List<Settlement> settlements = currentGroup.calculateSettlements();
        System.out.println("\n  SETTLEMENTS (minimum transactions):");
        if (settlements.isEmpty()) {
            System.out.println("    All settled! No payments needed. ✔");
        } else {
            for (Settlement s : settlements) {
                System.out.printf("    %s pays ₹%.2f → %s%n",
                        s.getFrom().getName(), s.getAmount(), s.getTo().getName());
            }
        }
        System.out.println();
    }

    // ================================================================
    //  HELPERS
    // ================================================================

    private static boolean checkGroup() {
        if (currentGroup == null) {
            System.out.println("  ⚠ No group created yet. Use option 1.\n");
            return false;
        }
        return true;
    }

    private static void listMembers(List<Person> members) {
        for (int i = 0; i < members.size(); i++) {
            System.out.printf("    %d. %s%n", i + 1, members.get(i).getName());
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  ⚠ Enter a valid number."); }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v < 0) { System.out.println("  ⚠ Value cannot be negative."); continue; }
                return v;
            } catch (NumberFormatException e) { System.out.println("  ⚠ Enter a valid number."); }
        }
    }
}
