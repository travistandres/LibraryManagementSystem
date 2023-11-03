//DW 10/30/2023
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Transaction {
    private final String url = "jdbc:mysql://librarydatabase.cupwod9sczsb.us-east-2.rds.amazonaws.com:3306/LibraryManagementSystem";
    private final String username = "admin"; // Your MySQL username
    private final String password = "2QH03UdHKY8t9TT4PeSb"; // Your MySQL password
    private Connection connection;
    public void addTransaction(int bookID, int userID, Date returnDate) throws SQLException{
        try {
            connection = DriverManager.getConnection(url, username, password);
            try{
                String sql = "INSERT INTO transactions (returnDate, user_id, book_id)" + "VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setDate(1, returnDate);
                ps.setInt(2, userID);
                ps.setInt(3, bookID);
                ps.executeUpdate();
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void displayTransactionsForUser(DefaultTableModel transactionModel, int userID){
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
                String query = "SELECT book_id, returnDate, transaction_id FROM transactions WHERE user_id = " + userID;
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery(query);
                if (rs.next()){
                    int bookID = rs.getInt("book_id");
                    String bookQuery = "Select isbn, title From book where book_id = " + bookID;
                    PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
                    ResultSet bookSet = bookStatement.executeQuery(bookQuery);

                    while (bookSet.next()) {
                        String isbn = bookSet.getString("isbn");
                        String title = bookSet.getString("title");
                        Date returnDate = rs.getDate("returnDate");
                        String transactionID = rs.getString("transaction_id");

                        String[] data = {isbn, title, returnDate.toString(), transactionID};
                        transactionModel.addRow(data);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User ID not found!", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    } 
    private void deleteTransaction(int transactionID) throws SQLException{
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
              String sql = "DELETE FROM transactions WHERE transaction_id = " + transactionID;
              PreparedStatement ps = connection.prepareStatement(sql);
              ps.executeUpdate();
              connection.close();
            } catch (SQLException e){
              e.printStackTrace();
            }
          } catch (SQLException e){
            e.printStackTrace();
          }
    }
    private boolean copyTransactionToLog(int transactionID) throws SQLException{
        boolean dataTransfered = false;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Date returnDate = null;
            int userID = -1, bookID = -1;
            boolean dataCopied;
            try{
                String sql = "SELECT returnDate, user_id, book_id FROM transactions WHERE transaction_id = " + transactionID;
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                userID = rs.getInt("user_id");
                bookID = rs.getInt("book_id");
                returnDate = rs.getDate("returnDate");
                dataCopied = true;
            } catch (SQLException e){
                e.printStackTrace();
                dataCopied = false;
            }
            if (dataCopied) {
                try{
                    String sql = "INSERT INTO transaction_log (returnDate, user_id, book_id, transaction_id)" + "VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setDate(1, returnDate);
                    ps.setInt(2, userID);
                    ps.setInt(3, bookID);
                    ps.setInt(4, transactionID);
                    ps.executeUpdate();
                    dataTransfered = true;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return dataTransfered;
    }
    public void logAndDelete(int transactionID){
        try{
            if (copyTransactionToLog(transactionID)){
                deleteTransaction(transactionID);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}