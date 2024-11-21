import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface Interface extends Remote {
    String addExpense(String username, double amount, String detail) throws RemoteException;
    Map<String, Double> viewUserExpenses(String username) throws RemoteException;
    Map<String, Double> viewGroupBalances() throws RemoteException;
    String splitExpenses() throws RemoteException;
    String removeUser(String username) throws RemoteException;
}
