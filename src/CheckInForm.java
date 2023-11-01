
// Travis Tan
// 10-25-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CheckInForm {
  JDialog dialog;

  JLabel userID;

  JTextField searchBar;

  JButton checkIn;
  JButton cancel;

  DefaultTableModel transactionModel;

  Transaction transaction = new Transaction();

  CheckInForm(JFrame frame) {
    // Check In Form Window
    dialog = new JDialog(frame);
    dialog.setTitle("Check In Form");
    dialog.setSize(400, 550);
    dialog.setLocationRelativeTo(frame);
    dialog.setResizable(false);

    // Main Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setSize(400, 550);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    dialog.add(mainPanel);

    // Search Panel
    JPanel searchPanel = new JPanel(new FlowLayout());

    // Search Panel Components
    userID = new JLabel("User ID:");
    searchBar = new JTextField();
    searchBar.setPreferredSize(new Dimension(300, 20));

    // Allows "Enter" key detection to search
    Action action = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {

        if (searchBar.getText().isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please enter a User ID.", "Error Message",
              JOptionPane.ERROR_MESSAGE);
        } else {
          try {
            transaction.displayTransactionsForUser(transactionModel, Integer.parseInt(searchBar.getText()));
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }

      }
    };

    // Adding the action listener to the search bar
    searchBar.addActionListener(action);

    // Adding Search Panel Components to Search Panel
    searchPanel.add(userID);
    searchPanel.add(searchBar);

    // Table Panel
    JPanel tablePanel = new JPanel();
    tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

    // Column Names
    String columnNames[] = { "ISBN", "Book", "Return Date" };

    // Creating Transaction Table
    JTable transactionTable = new JTable(new DefaultTableModel(null, columnNames));
    transactionModel = (DefaultTableModel) transactionTable.getModel();
    transactionTable.getTableHeader().setReorderingAllowed(false);
    transactionTable.setDefaultEditor(Object.class, null);

    // Adding Transaction Table to the Scroll Pane
    JScrollPane transactionPane = new JScrollPane(transactionTable);
    transactionPane.setPreferredSize(new Dimension(370, 350));

    // Adding Transaction Pane to Table Panel
    tablePanel.add(transactionPane);

    // Displaying Transactions Table

    // Check In Button
    checkIn = new JButton("Check-In");
    checkIn.setAlignmentX(Component.CENTER_ALIGNMENT);
    tablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
    tablePanel.add(checkIn);

    // Action Listener for Check In Button
    checkIn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Insert Method here
      }
    });

    // Cancel Panel
    JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    // Cancel Button
    cancel = new JButton("Cancel");
    cancelPanel.add(cancel);

    // Action Listener for Cancel Button
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog.dispose();
      }
    });

    // Adding All Panels to Main Panel
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    mainPanel.add(searchPanel);
    mainPanel.add(tablePanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    mainPanel.add(cancelPanel);

    dialog.setVisible(true);
  }
}
