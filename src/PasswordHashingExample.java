
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordHashingExample {

    //private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/my_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static void storePasswordInDatabase(String name, String username, String hashedPassword) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Assume 'users' table with 'username' and 'password' columns
            String sql = "INSERT INTO users ( name, username, password) VALUES (?, ?, ?)";
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

    public static void main(String[] args) {
        // Example usage
        String name  = "Example User"; // Replace with your name or username
        String username = "exampleUser";
        String password = "examplePassword";

        String hashedPassword = hashPassword(password);
        storePasswordInDatabase(name,username, hashedPassword);

        System.out.println("Password hashed and stored in the database.");
    }
}
