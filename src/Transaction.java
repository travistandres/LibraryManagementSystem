
//DW 11/3/2023
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
    private Book book = new Book();

    public void addTransaction(int bookID, int userID, Date returnDate) throws SQLException {
        try {
            connection = DriverManager.getConnection(url, username, password);
            try {
                String sql = "INSERT INTO transactions (returnDate, user_id, book_id)" + "VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setDate(1, returnDate);
                ps.setInt(2, userID);
                ps.setInt(3, bookID);
                ps.executeUpdate();
                book.makeUnavailable(bookID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayTransactionsForUser(DefaultTableModel transactionModel, int userID) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            try {
                String userQuery = "SELECT user_id FROM user WHERE user_id LIKE ?";
                PreparedStatement userStatement = connection.prepareStatement(userQuery);
                userStatement.setInt(1, userID);
                ResultSet userSet = userStatement.executeQuery();
                if (userSet.next()) {
                    String query = "SELECT book_id, returnDate, transaction_id FROM transactions WHERE user_id = ?";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, userID);
                    ResultSet rs = ps.executeQuery();
                    // TT 11-15-23
                    // Checks if the user has a transaction
                    boolean hasTransaction = rs.isBeforeFirst();
                    if (!hasTransaction) {
                        JOptionPane.showMessageDialog(null, "User ID does not have any transactions", "Error Message",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    while (rs.next()) {
                        int bookID = rs.getInt("book_id");
                        String bookQuery = "Select isbn, title From book where book_id = ?";
                        PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
                        bookStatement.setInt(1, bookID);
                        ResultSet bookSet = bookStatement.executeQuery();

                        while (bookSet.next()) {
                            String isbn = bookSet.getString("isbn");
                            String title = bookSet.getString("title");
                            Date returnDate = rs.getDate("returnDate");
                            String transactionID = rs.getString("transaction_id");

                            String[] data = { isbn, title, returnDate.toString(), transactionID };
                            transactionModel.addRow(data);

                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User ID not found!", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTransaction(int transactionID) throws SQLException {
        try {
            connection = DriverManager.getConnection(url, username, password);
            try {
                String sql = "DELETE FROM transactions WHERE transaction_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, transactionID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean copyTransactionToLog(int transactionID, Date dateReturned) throws SQLException {
        boolean dataTransfered = false;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Date returnDate = null;
            int userID = -1, bookID = -1;
            boolean dataCopied;
            try {
                String sql = "SELECT returnDate, user_id, book_id FROM transactions WHERE transaction_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, transactionID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    userID = rs.getInt("user_id");
                    bookID = rs.getInt("book_id");
                    returnDate = rs.getDate("returnDate");
                    book.makeAvailable(bookID);
                }
                dataCopied = true;
            } catch (SQLException e) {
                e.printStackTrace();
                dataCopied = false;
            }
            if (dataCopied) {
                try {
                    String sql = "INSERT INTO transaction_log (returnDate, user_id, book_id, transaction_id, dateReturned)"
                            + "VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setDate(1, returnDate);
                    ps.setInt(2, userID);
                    ps.setInt(3, bookID);
                    ps.setInt(4, transactionID);
                    ps.setDate(5, dateReturned);
                    ps.executeUpdate();
                    dataTransfered = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataTransfered;
    }

    public void logAndDelete(int transactionID, Date dateReturned) {
        try {
            if (copyTransactionToLog(transactionID, dateReturned)) {
                deleteTransaction(transactionID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}