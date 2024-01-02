import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordHandler {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/my_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public static String hashPassword(String plainTextPassword) {
        String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
        return hashedPassword;
    }

    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        //with BCrypt.checkpw() method, we can verify the hashed password.
        //if the password is correct, it will return true.
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static void storePasswordInDatabase(String name, String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String hashedPassword = hashPassword(password);

            // Assume you have a table named 'users' with columns 'username' and 'password'
            String sql = "INSERT INTO users (name, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticateUser(String name, String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Assume you have a table named 'users' with columns 'username' and 'password'
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        //get  password from database and compare with password entered by user.
                        String hashedPasswordFromDB = resultSet.getString("password");
                        //call verifyPassword method to verify password.
                        return verifyPassword(password, hashedPasswordFromDB); 
                        // true if password matches, false otherwise.
                        // after calling verifyPassword method, you can perform further actions based on the result.
                        //return keyword used to return the result to authenticateUser method who called form main function body.
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // Example usage
        String name = "Example";
        String username = "exampleUser";
        String password = "examplePassword";

        storePasswordInDatabase(name, username, password);

        if (authenticateUser(name, username, password)) {
            System.out.println("Authentication successful");
        } else {
            System.out.println("Authentication failed");
        }
    }
}
