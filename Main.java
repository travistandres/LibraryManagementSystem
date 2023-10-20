import javax.swing.SwingUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });

        // Step 1: Define the database connection parameters.
        // String url = "jdbc:mysql://localhost:3306/LibraryManagementSystem"; //
        // JDBCURL for the database
        // String username = "root"; // Your MySQL username
        // String password = "Skatevolcom01!"; // Your MySQL password
        // // Step 2: Establish a connection to the database.
        // try {
        // Connection connection = DriverManager.getConnection(url, username, password);
        // // Step 3: Create an SQL INSERT statement.
        // String insertQuery = "INSERT INTO Users (Name, Phone) VALUES (?, ?)";
        // // Step 4: Prepare and execute the SQL statement.
        // try (PreparedStatement preparedStatement =
        // connection.prepareStatement(insertQuery)) {
        // // Set the parameter values for the INSERT statement.
        // preparedStatement.setString(1, "John Doe");
        // preparedStatement.setString(2, "123-456-7890");
        // // Execute the INSERT statement.
        // int rowsAffected = preparedStatement.executeUpdate();
        // if (rowsAffected > 0) {
        // System.out.println("New user record inserted successfully.");
        // } else {
        // System.out.println("Insert operation failed.");
        // }
        // }
        // // Step 5: Close the database connection.
        // connection.close();
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
    }
}
