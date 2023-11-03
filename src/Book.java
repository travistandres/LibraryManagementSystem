//DW 10/23/2023
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
public class Book {
    private final String url = "jdbc:mysql://librarydatabase.cupwod9sczsb.us-east-2.rds.amazonaws.com:3306/LibraryManagementSystem";
    private final String username = "admin"; // Your MySQL username
    private final String password = "2QH03UdHKY8t9TT4PeSb"; // Your MySQL password
    
    private Connection connection;
    Book(){}
    public void deleteBook(int id) throws SQLException{
      try{
        connection = DriverManager.getConnection(url, username, password);
        try{
          String sql = "DELETE FROM book WHERE book_id = ?";
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
    public void addBook(String title, String author, String isbn) throws SQLException{
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
              String sql = "INSERT INTO book (title, author, isbn, availability)" + "VALUES (?, ?, ?, ?)";
              PreparedStatement ps = connection.prepareStatement(sql);
              ps.setString(1, title);
              ps.setString(2, author);
              ps.setString(3, isbn);
              ps.setInt(4, 2);
              ps.executeUpdate();
            } catch (Exception e) {
              e.printStackTrace();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayBooks(DefaultTableModel bookModel) throws SQLException{
    try {
      connection = DriverManager.getConnection(url, username, password);
      String query = "SELECT book_id, title, author, isbn, genre FROM book";
      try {
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery(query);

        while (rs.next()) {
          String id = rs.getString("book_id");
          String title = rs.getString("title");
          String author = rs.getString("author");
          String isbn = rs.getString("isbn");
          String genre = rs.getString("genre");

          String[] data = { id, title, author, genre ,isbn};

          bookModel.addRow(data);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
    public void makeAvailable(int bookID) throws SQLException{
      try{
        connection = DriverManager.getConnection(url, username, password);
        try {
          String sql = "UPDATE book SET availability = " + 2 + " WHERE book_id = " + bookID;
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.executeUpdate(sql);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        connection.close();
      } catch (SQLException e){
        e.printStackTrace();
      }
    }
    public void makeUnavailable(int bookID) throws SQLException{
      try{
        connection = DriverManager.getConnection(url, username, password);
        try {
          String sql = "UPDATE book SET availability = " + 1 + " WHERE book_id = " + bookID;
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.executeUpdate(sql);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        connection.close();
      } catch (SQLException e){
        e.printStackTrace();
      }
    }
}
//The database sees 2 as Available and 1 as Unavailable when talking about availability in the book table