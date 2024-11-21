import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Interface {
    private final Map<String, Double> userBalances = new HashMap<>();
    private final Map<String, List<String>> userExpenses = new HashMap<>();

    @Override
    public synchronized String addExpense(String username, double amount, String detail) throws RemoteException {
        userBalances.put(username, userBalances.getOrDefault(username, 0.0) + amount);
        userExpenses.computeIfAbsent(username, k -> new java.util.ArrayList<>())
                    .add("Expense: $" + amount + " - " + detail);
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

        return "Expenses split evenly. Each user owes: $" + perUser;
    }

    @Override
    public synchronized String removeUser(String username) throws RemoteException {
        if (!userBalances.containsKey(username)) {
            throw new RemoteException("User not found: " + username);
        }

        userBalances.remove(username);
        userExpenses.remove(username);
        return "User " + username + " removed successfully.";
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Interface stub = (Interface) UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BudgetTracker", stub);

            System.out.println("Budget Tracker Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
