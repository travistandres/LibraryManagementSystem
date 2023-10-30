//DW 10/26/2023
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

public class Transaction {
    private final String url = "jdbc:mysql://librarydatabase.cupwod9sczsb.us-east-2.rds.amazonaws.com:3306/LibraryManagementSystem";
    private final String username = "admin"; // Your MySQL username
    private final String password = "2QH03UdHKY8t9TT4PeSb"; // Your MySQL password
    private Connection connection;
    public void addTransaction(int book_id, int user_id, Date returnDate) throws SQLException{
        try {
            connection = DriverManager.getConnection(url, username, password);
            try{
                String sql = "INSERT INTO transactions (returnDate, user_id, book_id)" + "VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setDate(1, returnDate);
                ps.setInt(2, user_id);
                ps.setInt(3, book_id);
                ps.executeUpdate();
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void displayTransactions(DefaultTableModel transactionModel, int user_id){
        try{
            connection = DriverManager.getConnection(url, username, password);
            try{
                String query = "SELECT * FROM transactions WHERE user_id = " + user_id;
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery(query);
                while (rs.next()) {
                    try{
                        String bookQuery = "SELECT isbn, title FROM book";
                        PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
                        ResultSet bookSet = bookStatement.executeQuery(bookQuery);

                        String isbn = bookSet.getString("isbn");
                        String title = bookSet.getString("title");
                        Date returnDate = rs.getDate("returnDate");

                        String[] data = {isbn, title, returnDate.toString()};
                        transactionModel.addRow(data);
                    } catch (SQLException e){
                        e.printStackTrace();
                    }

                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
