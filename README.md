# 💸 Splitwise-Style Expense Splitter

> **Java OOP Mini Project** | B.E. Electrical & Electronics Engineering

| Field         | Details                                      |
|---------------|----------------------------------------------|
| **Author**    | Kowshick K                                   |
| **Reg No**    | 711524BEE302                                 |
| **Course**    | B.E. EEE — KIT, Coimbatore (Anna University) |
| **Subject**   | Object-Oriented Programming using Java       |
| **Project**   | Splitwise-Style Expense Splitter             |

---

## 📌 Project Overview

A command-line expense-splitting application inspired by **Splitwise**, built entirely in Java to demonstrate all four pillars of **Object-Oriented Programming**.

Users can:
- Create groups of people
- Add expenses with three split modes — **Equal, Percentage, Exact**
- View each person's balance (who owes what)
- Calculate **minimum transactions** needed to settle all debts

---

## 🧱 OOP Concepts Demonstrated

| OOP Pillar        | Where Used                                                                 |
|-------------------|----------------------------------------------------------------------------|
| **Encapsulation** | `Person`, `Settlement` — private fields, public getters/setters            |
| **Abstraction**   | `Expense` abstract class — hides split logic behind a clean interface       |
| **Inheritance**   | `EqualExpense`, `PercentageExpense`, `ExactExpense` extend `Expense`        |
| **Polymorphism**  | `split()` method behaves differently in each subclass (method overriding)   |

---

## 🗂️ Project Structure

```
splitwise-expense-splitter/
│
├── src/
│   └── com/
            ├── SplitwiseApp.java       ← Main class (CLI menu)
│           ├── Person.java             ← Encapsulated person entity
│           ├── Expense.java            ← Abstract base class
│           ├── EqualExpense.java       ← Equal split (extends Expense)
│           ├── PercentageExpense.java  ← Percentage split (extends Expense)
│           ├── ExactExpense.java       ← Exact amount split (extends Expense)
│           ├── Group.java              ← Group management + settlement logic
│           └── Settlement.java         ← Value object for a payment transaction
│
├── README.md
└── .gitignore
```

---

## 🔍 Class Diagram (UML)

```
          ┌─────────────────┐
          │   <<abstract>>  │
          │    Expense      │
          │─────────────────│
          │ # description   │
          │ # totalAmount   │
          │ # paidBy        │
          │ # participants  │
          │─────────────────│
          │ +split()  ◄abstract►       
          │ +getSplitType() │
          └────────┬────────┘
                   │ extends
       ┌───────────┼───────────┐
       ▼           ▼           ▼
 EqualExpense  PercentExpense  ExactExpense
 (overrides    (overrides      (overrides
  split())      split())        split())

 ┌──────────┐         ┌──────────┐
 │  Person  │◄────────│  Group   │
 │──────────│ members │──────────│
 │-name     │         │-groupName│
 │-totalPaid│         │-members  │
 │-totalShare│        │-expenses │
 │──────────│         │──────────│
 │+getNet() │         │+addExpense()│
 └──────────┘         │+calcSettlements()│
                      └──────────┘
 ┌────────────┐
 │ Settlement │
 │────────────│
 │-from       │
 │-to         │
 │-amount     │
 └────────────┘
```

---

## ⚙️ How to Run

### Prerequisites
- Java JDK 11 or above
- Terminal / Command Prompt

### Compile
```bash
# From project root
javac -d out src/com/splitwise/*.java
```

### Run
```bash
java -cp out com.splitwise.SplitwiseApp
```

---

## 🎮 Sample Run

```
╔══════════════════════════════════════════════════╗
║      SPLITWISE-STYLE EXPENSE SPLITTER            ║
║      Author  : Kowshick K                        ║
║      Reg No  : 711524BEE302                      ╚
╚══════════════════════════════════════════════════╝

1. Create / Switch Group
2. Add Member
3. Add Expense
...

Group name: Trip to Ooty

Member name: Kowshick
Member name: Arun
Member name: Priya

--- Add Expense ---
Description: Hotel
Amount: 3000
Paid by: Kowshick
Split type: 1 (Equal)

  ✔ Expense added! Split breakdown:
     Kowshick     → ₹1000.00
     Arun         → ₹1000.00
     Priya        → ₹1000.00

--- Settlements ---
  Arun  pays ₹1000.00 → Kowshick
  Priya pays ₹1000.00 → Kowshick
```

---

## 🧠 Key Algorithms

### Minimum Transactions (Greedy Settlement)
The `Group.calculateSettlements()` method uses a **greedy algorithm** to minimize the number of transactions:
1. Compute net balance for each person (paid − owed)
2. Repeatedly match the **biggest creditor** with the **biggest debtor**
3. Transfer the minimum of the two balances
4. Repeat until all balances are zero

This produces the **optimal minimum number of payments**.

---

## 📐 Split Modes

| Mode           | Description                                  | Example (₹900 split 3 ways)          |
|----------------|----------------------------------------------|---------------------------------------|
| **Equal**      | Divided equally                              | Each pays ₹300                        |
| **Percentage** | Based on custom % (must sum to 100)          | 50%, 30%, 20% → ₹450, ₹270, ₹180    |
| **Exact**      | Custom fixed amounts (must sum to total)     | ₹500, ₹250, ₹150                     |

---

## 📚 References

- Java Documentation — [docs.oracle.com](https://docs.oracle.com/en/java/)
- OOP Concepts — *Let Us Java* by Kanetkar
- Splitwise Algorithm — Greedy debt simplification

---

*Submitted as part of Java OOP Mini Project — B.E. EEE, KIT Coimbatore*
