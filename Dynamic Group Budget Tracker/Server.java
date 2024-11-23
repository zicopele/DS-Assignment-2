import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.PrintWriter;

public class Server implements Interface {
    private final Map<String, Double> userBalances = new HashMap<>();
    private final Map<String, List<String>> userExpenses = new HashMap<>();
    private final List<PrintWriter> clientWriters = new ArrayList<>(); // List of all client outputs

    @Override
    public synchronized String addExpense(String username, double amount, String detail) throws RemoteException {
        userBalances.put(username, userBalances.getOrDefault(username, 0.0) + amount);
        userExpenses.computeIfAbsent(username, k -> new ArrayList<>())
                    .add("Expense: $" + amount + " - " + detail);

        // Create message with [Notification] prefix
        String message = "[Notification]: " + username + " added an expense of $" + amount + " for: " + detail;
        System.out.println(message); // Log on server console
        notifyClients(message); // Notify all clients
        return "Expense added for " + username + ": $" + amount + " (" + detail + ")";
    }

    @Override
    public synchronized Map<String, Double> viewUserExpenses(String username) throws RemoteException {
        if (!userExpenses.containsKey(username)) {
            throw new RemoteException("No expenses found for user: " + username);
        }
        return Map.of(username, userBalances.get(username));
    }

    @Override
    public synchronized Map<String, Double> viewGroupBalances() throws RemoteException {
        return new HashMap<>(userBalances);
    }

    @Override
    public synchronized String splitExpenses() throws RemoteException {
        if (userBalances.isEmpty()) {
            throw new RemoteException("No users to split expenses.");
        }

        double total = userBalances.values().stream().mapToDouble(Double::doubleValue).sum();
        double perUser = total / userBalances.size();
        userBalances.replaceAll((k, v) -> perUser);

        // Create message with [Notification] prefix
        String message = "[Notification]: Expenses split evenly. Each user owes: $" + perUser;
        System.out.println(message); // Log on server console
        notifyClients(message); // Notify all clients
        return message;
    }

    @Override
    public synchronized String removeUser(String username) throws RemoteException {
        if (!userBalances.containsKey(username)) {
            throw new RemoteException("User not found: " + username);
        }

        userBalances.remove(username);
        userExpenses.remove(username);

        // Create message with [Notification] prefix
        String message = "[Notification]: " + username + " left the Budget Tracker.";
        System.out.println(message); // Log on server console
        notifyClients(message); // Notify all clients
        return message;
    }

    private synchronized void addUser(String username) {
        // Only add the user when they join, without adding an expense
        userBalances.put(username, 0.0);
        userExpenses.put(username, new ArrayList<>());

        // Clean and simple join message with [Notification] prefix
        String message = "[Notification]: " + username + " joined the Budget Tracker.";
        System.out.println(message); // Print clean join notification on the server
        notifyClients(message); // Notify all clients about the new user
    }

    private synchronized void notifyClients(String message) {
        // Broadcast the message to all connected clients
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    public static void main(String[] args) {
        try {
            // Create and export the server object
            Server server = new Server();
            Interface stub = (Interface) UnicastRemoteObject.exportObject(server, 0);

            // Create and bind the registry
            Registry registry = LocateRegistry.createRegistry(1200);
            registry.rebind("BudgetTracker", stub);

            System.out.println("[Server]: Budget Tracker Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle individual client connections
    private class ClientHandler extends Thread {
        private final PrintWriter out;
        private final String username;

        public ClientHandler(PrintWriter out, String username) {
            this.out = out;
            this.username = username;
        }

        public void run() {
            try {
                addUser(username); // Notify server and all clients when a user joins

                String input;
                // Add code to handle client commands (e.g., ADD, VIEW, EXIT, etc.)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
