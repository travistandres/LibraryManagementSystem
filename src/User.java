//DW 10/25/2023
//#region imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
//#endregion
public class User {
    private final String url = "jdbc:mysql://librarydatabase.cupwod9sczsb.us-east-2.rds.amazonaws.com:3306/LibraryManagementSystem";
    private final String username = "admin"; // Your MySQL username
    private final String password = "2QH03UdHKY8t9TT4PeSb"; // Your MySQL password
    private Connection connection;
    User(){}
    public void deleteUser(int id) throws SQLException{
      try{
        connection = DriverManager.getConnection(url, username, password);
        try{
          String sql = "DELETE FROM user WHERE user_id = ?";
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.setInt(1, id);
          ps.executeUpdate();
        } catch (Exception e){
          e.printStackTrace();
        }
        connection.close();
      } catch (Exception e){
        e.printStackTrace();
      }
    }
    public void addUser(String name, String phoneNumber) throws SQLException{
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
              String sql = "INSERT INTO user (fullName, phoneNumber)" + "VALUES (?, ?)";
              PreparedStatement ps = connection.prepareStatement(sql);
              ps.setString(1, name);
              ps.setString(2, phoneNumber);
              ps.executeUpdate();
            } catch (Exception e) {
              e.printStackTrace();
            }
            connection.close();
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
    public void updateUser(int userID, String name, String phoneNumber) {
    try{
      connection = DriverManager.getConnection(url, username, password);
      try{
        String sql = "UPDATE user SET fullName = '" + name + "', phoneNumber = '" + phoneNumber + "' WHERE user_id = " + userID;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate(sql);
      } catch (SQLException e){
        e.printStackTrace();
      }
    } catch (SQLException e){
      e.printStackTrace();
    }
  }
}