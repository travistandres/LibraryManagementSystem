
// Travis Tan
// 10-25-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
        // resets the table after pressing the "Enter" key
        transactionModel.setRowCount(0);

        if (searchBar.getText().isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please enter a User ID.", "Error Message",
              JOptionPane.ERROR_MESSAGE);
        } else {
          try {
            // Displaying Transactions Table
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
    String columnNames[] = { "ISBN", "Book", "Return Date", "Transaction ID" };

    // Creating Transaction Table
    JTable transactionTable = new JTable(new DefaultTableModel(null, columnNames));
    transactionModel = (DefaultTableModel) transactionTable.getModel();
    transactionTable.getTableHeader().setReorderingAllowed(false);
    transactionTable.setDefaultEditor(Object.class, null);
    // only one row can be highlighted
    transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Enabling Check-In Button when Table is in Focus
    transactionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        checkIn.setEnabled(true);

        if (transactionTable.getSelectionModel().isSelectionEmpty()) {
          checkIn.setEnabled(false);
        }
      }
    });

    // This block of code makes the Transaction ID invisible but still holds data
    TableColumn column = transactionTable.getColumnModel().getColumn(3);
    column.setMinWidth(0);
    column.setMaxWidth(0);

    // Centering text on each cell
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames.length; i++) {
      transactionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Adding Transaction Table to the Scroll Pane
    JScrollPane transactionPane = new JScrollPane(transactionTable);
    transactionPane.setPreferredSize(new Dimension(370, 350));

    // Adding Transaction Pane to Table Panel
    tablePanel.add(transactionPane);

    // Check In Button
    checkIn = new JButton("Check-In");
    checkIn.setAlignmentX(Component.CENTER_ALIGNMENT);
    checkIn.setEnabled(false);
    tablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
    tablePanel.add(checkIn);

    // Action Listener for Check In Button
    checkIn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedRow = transactionTable.getSelectedRow();
        int transaction_ID = Integer.valueOf((String) transactionTable.getModel().getValueAt(selectedRow, 3));
        if (transactionTable.getSelectionModel().isSelectionEmpty()) {
          JOptionPane.showMessageDialog(null, "Nothing is selected!", "Error Message",
              JOptionPane.ERROR_MESSAGE);
        } else {
          try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(java.time.LocalDate.now().toString());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            transaction.logAndDelete(transaction_ID, sqlDate);
            JOptionPane.showMessageDialog(null, "Checked-In Successfully.");
            // removes the row that they checked in
            transactionModel.removeRow(selectedRow);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }
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

    searchBar.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {

        // TT 11-15-23
        // Makes sure only numbers are in the text field
        char c = e.getKeyChar();
        if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
          e.consume();
        }
      }
    });

    dialog.setVisible(true);
  }
}
