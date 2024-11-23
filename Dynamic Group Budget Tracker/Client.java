import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1200);
            Interface tracker = (Interface) registry.lookup("BudgetTracker");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.println("[Client]: Welcome to the Budget Tracker!");

            // Notify the server about the new user
            tracker.addExpense(username, 0, "User joined");

            while (true) {
                System.out.println("\nCommands: ADD <amount> <detail>, VIEW, BALANCE, SPLIT, REMOVE, EXIT");
                System.out.print("Enter command: ");
                String input = scanner.nextLine();

                if (input.startsWith("ADD ")) {
                    String[] parts = input.split(" ", 3);
                    if (parts.length < 3) {
                        System.out.println("[Client]: Invalid ADD command. Use: ADD <amount> <detail>");
                        continue;
                    }

                    double amount = Double.parseDouble(parts[1]);
                    String detail = parts[2];
                    System.out.println(tracker.addExpense(username, amount, detail));

                } else if (input.equals("VIEW")) {
                    try {
                        Map<String, Double> userExpenses = tracker.viewUserExpenses(username);
                        System.out.println("[Client]: Expenses for " + username + ": " + userExpenses);
                    } catch (Exception e) {
                        System.out.println("[Client]: " + e.getMessage());
                    }

                } else if (input.equals("BALANCE")) {
                    Map<String, Double> groupBalances = tracker.viewGroupBalances();
                    System.out.println("[Client]: Group Balances: " + groupBalances);

                } else if (input.equals("SPLIT")) {
                    System.out.println(tracker.splitExpenses());

                } else if (input.startsWith("REMOVE")) {
                    System.out.println(tracker.removeUser(username));
                    break;

                } else if (input.equals("EXIT")) {
                    break;

                } else {
                    System.out.println("[Client]: Invalid command.");
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
