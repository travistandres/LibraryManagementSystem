//DW 10/25/2023
//#region imports
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
//#endregion
public class User {
    private final String url = "jdbc:mysql://librarydatabase.cupwod9sczsb.us-east2.rds.amazonaws.com:3306/LibraryManagementSystem";
    private final String username = "admin"; // Your MySQL username
    private final String password = "2QH03UdHKY8t9TT4PeSb"; // Your MySQL password
    private Connection connection;
    private Statement statement;
    
    public void addUser(DefaultTableModel userModel, String name, String phoneNumber, int id) throws SQLException{
        try{
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            try{
                String sql = "INSERT INTO user ()" + "VALUES (id)" //SQL insert stament using variables
                statement.executeUpdate
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayUsers(DefaultTableModel userModel) throws SQLException{
    try {
      connection = DriverManager.getConnection(url, username, password);
      String query = "SELECT user_id, fullName, phoneNumber FROM user";
      try {
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery(query);

        while (rs.next()) {
          String id = rs.getString("user_id");
          String name = rs.getString("fullName");
          String number = rs.getString("phoneNumber");

          String[] data = { id, name, number };

          userModel.addRow(data);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}