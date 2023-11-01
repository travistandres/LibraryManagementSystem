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
          connection.close();
        } catch (Exception e){
          e.printStackTrace();
        }
      } catch (Exception e){
        e.printStackTrace();
      }
    }
    public void addBook(String title, String author, int id, String isbn) throws SQLException{
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
              String sql = "INSERT INTO book (book_id, title, author, isbn)" + "VALUES (?, ?, ?, ?)";
              PreparedStatement ps = connection.prepareStatement(sql);
              ps.setInt(1, id);
              ps.setString(2, title);
              ps.setString(3, author);
              ps.setString(4, isbn);
              ps.executeUpdate();
              connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
enum Available{
    OUT(0), 
    IN(1);

    int value;

    Available(int value){
        this.value = value;
    }
}