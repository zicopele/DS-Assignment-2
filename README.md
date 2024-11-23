# Dynamic Group Budget Tracker

This is a Java-based group budget tracker application that uses RMI (Remote Method Invocation) for distributed interaction between a server and multiple clients. Users can add expenses, view balances, split expenses among the group, and manage their membership.

---

## Features

1. **Add Expense**: Users can log individual expenses with a description and amount.
2. **View User Expenses**: Displays all logged expenses and the total balance for a user.
3. **View Group Balances**: Shows the balances of all users in the group.
4. **Split Expenses**: Evenly divides total expenses among all users.
5. **Remove User**: Removes a user from the tracker and clears their data.

---

## Setup Instructions

### 1. Prerequisites

- Java Development Kit (JDK) 8 or later installed.
- Basic understanding of terminal/command line operations.
- Ensure all `.java` files are in the same directory:
  - `Interface.java`
  - `Server.java`
  - `Client.java`

### 2. Compile the Code

Open a terminal or command prompt, navigate to the directory containing the files, and run:

```javac Interface.java Server.java Client.java```

### 3. Start the Server

Run the following command to start the RMI registry and the server:

```
rmiregistry 1200
java Server
```

### 4. Start the Client

In a new terminal or command prompt, run:
```java Client```

---

## Commands

Here are the commands users can issue to interact with the budget tracker:

### 1. ADD `<amount>` `<detail>`
Logs an expense for the current user, specifying the amount and a brief description.

- **Purpose**: Add an expense.
- **Syntax**: 
  ```ADD <amount> <detail>```

### 2. VIEW
Displays all expenses logged by the current user, along with their total balance.

- Purpose: View personal expenses.
- Syntax:
  ```VIEW```

### 3. BALANCE
Shows the current balances for all users in the group.

- Purpose: View the group balance summary.
- Syntax:
  ```BALANCE```

### 4. SPLIT
Evenly divides all expenses among the users in the group and updates their balances.

- Purpose: Split expenses among users.
- Syntax:
  ```SPLIT```

### 5. REMOVE
Removes the current user from the budget tracker, clearing their data from the server.

- Purpose: Remove the user from the system.
- Syntax:
  ```REMOVE```

### 6. EXIT
Gracefully disconnects the client from the server without removing the user from the tracker.

- Purpose: Exit the application without data loss.
- Syntax:
  ```EXIT```
