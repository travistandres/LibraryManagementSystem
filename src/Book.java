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
    public void deleteBook(int bookID) throws SQLException{
      try{
        connection = DriverManager.getConnection(url, username, password);
        try{
          String sql = "DELETE FROM book WHERE book_id = ?";
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.setInt(1, bookID);
          ps.executeUpdate();
        } catch (Exception e){
          e.printStackTrace();
        }
        connection.close();
      } catch (Exception e){
        e.printStackTrace();
      }
    }
    public void addBook(String title, String author, String isbn, String genre) throws SQLException{
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
              String sql = "INSERT INTO book (title, author, isbn, genre, availability)" + "VALUES (?, ?, ?, ?, ?)";
              PreparedStatement ps = connection.prepareStatement(sql);
              ps.setString(1, title);
              ps.setString(2, author);
              ps.setString(3, isbn);
              ps.setString(4, genre);
              ps.setInt(5, 2);
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
    public boolean isAvailable(int bookID) throws SQLException{
      boolean available = false;
      try{
        connection = DriverManager.getConnection(url, username, password);
        try {
          String sql = "SELECT availability FROM book WHERE book_id = " + bookID;
          PreparedStatement ps = connection.prepareStatement(sql);
          ResultSet rs = ps.executeQuery(sql);
          while (rs.next()){
            if (rs.getString("availability").equals("Available")){
              available = true;
            }
          }
        } catch (SQLException e){
          e.printStackTrace();
        }
        connection.close();
      } catch (SQLException e){
        e.printStackTrace();
      }
      return available;
    }
    public void updateBook(int bookID, String title, String author, String genre) {
      try{
        connection = DriverManager.getConnection(url, username, password);
        try{
          String sql = "UPDATE book SET title = '" + title + "', author = '" + author + "', genre = '" + genre + "' WHERE book_id = " + bookID;
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.executeUpdate(sql);
        } catch (SQLException e){
          e.printStackTrace();
        }
      } catch (SQLException e){
        e.printStackTrace();
      }
    }
    // J.E. 10/23/2020
    // Method to verify if the ISBN is a duplicate
    public static boolean verifyISBN(String isbn) 
    {
        final String url = "jdbc:mysql://librarydatabase.cupwod9sczsb.us-east-2.rds.amazonaws.com:3306/LibraryManagementSystem";
        final String username = "admin"; // Your MySQL username
        final String password = "2QH03UdHKY8t9TT4PeSb"; // Your MySQL password

        String query = "SELECT isbn FROM book WHERE isbn = ?";

        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create a PreparedStatement with the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, isbn);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there are results
            if (resultSet.next()) // Value in the database matches the string
            {
                // Close resources
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return true;
            } 
            else  // No match found
            {
                // Close resources
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
  }
//The database sees 2 as Available and 1 as Unavailable when talking about availability in the book table