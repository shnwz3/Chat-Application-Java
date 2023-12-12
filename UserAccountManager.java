import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserAccountManager {
    private Map<String, String> userAccounts;
    private String filename = "user_accounts.ser"; // The file to store user accounts

    public UserAccountManager() {
        userAccounts = loadUserAccounts();
    }

    private Map<String, String> loadUserAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Map<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            // This exception is expected the first time the program runs
            System.out.println("No existing user accounts file found. Creating a new one.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle other exceptions here
        }
        return new HashMap<>();
    }

    private void saveUserAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(userAccounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null) {
            // Handle the case where either username or password is null
            return false;
        }

        String storedPassword = userAccounts.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public void addUser(String username, String password) {
        if (username != null && password != null) {
            userAccounts.put(username, password);
            saveUserAccounts();
            System.out.println("User added successfully");
        } else {
            // Handle the case where either username or password is null
            System.out.println("Error: Username and password cannot be null. User not added.");
        }
    }

    public static void main(String[] args) {
        // Example usage
        UserAccountManager userAccountManager = new UserAccountManager();

        // Authenticate the user (login)
        if (userAccountManager.authenticateUser("shah", "786")) {
            System.out.println("Authentication successful");
        } else {
            System.out.println("Authentication failed");
        }

        // Add a new user (SignIn)
        userAccountManager.addUser("new_user", "new_password");

        // Try to add a user with null username and password
        userAccountManager.addUser(null, null);

        // Authenticate the new user
        if (userAccountManager.authenticateUser("new_user", "new_password")) {
            System.out.println("New user authentication successful");
        } else {
            System.out.println("New user authentication failed");
        }
    }
}
