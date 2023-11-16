
// Travis Tan
// 10-23-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdatepicker.impl.*;

public class CheckOutForm {
  JDialog dialog;

  private int focusedPane = 0;
  private String bookSearch = "Search";
  private String userSearch = "Search";

  JLabel userIDLabel;
  JLabel bookIDLabel;
  JLabel returnDate;

  JPanel leftPanel;
  JPanel rightPanel;

  JTextField userField;
  JTextField bookField;
  JTextField searchBar;

  JButton cancel;
  JButton checkOut;

  UtilDateModel dateModel;

  private DefaultTableModel bookModel;
  private DefaultTableModel userModel;

  Runnable bookWorker;
  Thread bookThread;
  Runnable userWorker;
  Thread userThread;

  User user = new User();
  Book book = new Book();
  Transaction transaction = new Transaction();
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

  CheckOutForm(JFrame frame) {
    // Check Out Form Window
    dialog = new JDialog(frame);
    dialog.setTitle("Check Out Form");
    // dialog.setLayout(new BorderLayout());
    dialog.setSize(700, 438);
    dialog.setLocationRelativeTo(frame);
    dialog.setResizable(false);

    // Main Panel
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    dialog.add(mainPanel);

    // Panel that contains the search bar and Table
    rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    mainPanel.add(rightPanel, BorderLayout.EAST);

    // Panel that contains the User Input
    leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftPanel.setPreferredSize(new Dimension(250, 438));
    mainPanel.add(leftPanel, BorderLayout.WEST);

    // User Input Panel
    JPanel userInputPanel = new JPanel(new GridBagLayout());
    userInputPanel.setPreferredSize(new Dimension(230, 200));
    GridBagConstraints m = new GridBagConstraints();
    leftPanel.add(userInputPanel);

    // Text labels
    userIDLabel = new JLabel("User ID:");
    bookIDLabel = new JLabel("Book ID:");
    returnDate = new JLabel("Return Date:");

    // Text Fields
    userField = new JTextField();
    bookField = new JTextField();

    userField.setEditable(false);
    bookField.setEditable(false);
    // ISBN JLabel
    m.gridx = 0;
    m.gridy = 0;
    m.weightx = 0;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(bookIDLabel, m);

    // ID JLabel
    m.gridx = 0;
    m.gridy = 1;
    m.weightx = 0;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(userIDLabel, m);

    // Return Date JLabel
    m.gridx = 0;
    m.gridy = 2;
    m.weightx = 0;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(returnDate, m);

    // Book ID text field
    m.gridx = 1;
    m.gridy = 0;
    m.weightx = 1;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(bookField, m);

    // User ID text field
    m.gridx = 1;
    m.gridy = 1;
    m.weightx = 1;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(userField, m);

    // Date Picker
    dateModel = new UtilDateModel();
    dateModel.setSelected(true);
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
    JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

    // Adding Date Picker to the User Input Panel
    m.gridx = 1;
    m.gridy = 2;
    m.weightx = 1;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(datePicker, m);

    // Check Out Panel
    JPanel checkOutJPanel = new JPanel();
    checkOutJPanel.setLayout(new BoxLayout(checkOutJPanel, BoxLayout.Y_AXIS));
    checkOutJPanel.setPreferredSize(new Dimension(230, 30));
    leftPanel.add(checkOutJPanel);

    // check out button
    checkOut = new JButton("Check-Out");
    checkOut.setAlignmentX(Component.CENTER_ALIGNMENT);
    checkOutJPanel.add(checkOut);

    // Check Out button action listener
    checkOut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Gets the input from the form and then puts it into the database
        try {
          getInput();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
    });

    // Panel that contains the Cancel button
    JPanel cancelPanel = new JPanel();
    cancelPanel.setLayout(new BoxLayout(cancelPanel, BoxLayout.Y_AXIS));
    cancelPanel.setPreferredSize(new Dimension(230, 238));
    leftPanel.add(cancelPanel);

    // Cancel Button
    cancel = new JButton("Cancel");
    cancelPanel.add(Box.createRigidArea(new Dimension(0, 100)));
    cancelPanel.add(cancel);

    // Action Listener for exiting out of the dialog
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog.dispose();
      }
    });

    // Search Bar
    searchBar = new JTextField("Search");
    searchBar.setPreferredSize(new Dimension(100, 30));
    // "Search" text prompt. Goes away after user clicks search box
    searchBar.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        JTextField source = (JTextField) e.getComponent();
        source.setText("");
        bookSearch = "";
        userSearch = "";
        source.removeFocusListener(this);
      }
    });
    rightPanel.add(searchBar);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Book Table Column Names
    String[] columnNames = { "Book ID", "Title", "Author", "Genre", "ISBN", "Availability" };

    // Creating Book Table
    JTable bookTable = new JTable(new DefaultTableModel(null, columnNames)) {
      @Override
      public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);

        Object avail = bookModel.getValueAt(row, 4);
        System.out.println(row + " " + column);
        if (avail.equals("Unavailable")) {
          bookModel.removeRow(row);
        }

        return component;
      }
    };
    bookModel = (DefaultTableModel) bookTable.getModel();
    // Starts a thread to get Book Data from the database
    bookWorker = new BookTableWorker(bookModel);
    bookThread = new Thread(bookWorker);
    bookThread.start();

    // column header is not draggable
    bookTable.getTableHeader().setReorderingAllowed(false);
    bookTable.setDefaultEditor(Object.class, null);
    // can only highlight one row at a time
    bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bookTable.getColumnModel().getColumn(0).setMaxWidth(50);
    bookTable.getColumnModel().getColumn(5).setMinWidth(0);
    bookTable.getColumnModel().getColumn(5).setMaxWidth(0);

    // Adding Book Table to the Scroll Pane
    JScrollPane bookPane = new JScrollPane(bookTable);

    // Centering Text on each cell on Book Table
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames.length; i++) {
      bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // User Table Column Names
    String[] columnNames1 = { "User ID", "Name", "Phone Number" };

    // Creating User Table
    JTable userTable = new JTable(new DefaultTableModel(null, columnNames1));
    userModel = (DefaultTableModel) userTable.getModel();
    // Starts a Thread to get Data from the database
    userWorker = new UserTableWorker(userModel);
    userThread = new Thread(userWorker);
    userThread.start();

    // column header is not draggable
    userTable.getTableHeader().setReorderingAllowed(false);
    userTable.setDefaultEditor(Object.class, null);
    // can only highlight one row at a time
    userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Adding function to Search Bar DW 11/2/2023
    Searching search = new Searching();

    KeyListener keylistener = new KeyListener() {
      public void keyPressed(KeyEvent keyEvent) {
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (focusedPane != 1) {
          search.search(searchBar.getText(), bookModel, bookTable);
        } else {
          search.search(searchBar.getText(), userModel, userTable);
        }
      }
    };
    searchBar.addKeyListener(keylistener);

    // DW 11/13/2023 populating text field with selected ID
    bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        int bookTableFocus = bookTable.getSelectedRow();
        if (bookTableFocus >= 0) {
          bookField.setText(bookTable.getValueAt(bookTableFocus, 0).toString());
        }
      }
    });
    userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        int userTableFocus = userTable.getSelectedRow();
        if (userTableFocus >= 0) {
          userField.setText(userTable.getValueAt(userTableFocus, 0).toString());
        }
      }
    });

    // Adding User Table to the Scroll Pane
    JScrollPane userPane = new JScrollPane(userTable);

    // Centering Text on each cell on the User Table
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames1.length; i++) {
      userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Adding Tables to Tabs then to the TABLE Frame
    JTabbedPane tablePane = new JTabbedPane();
    tablePane.add("Books", bookPane);
    tablePane.add("Users", userPane);
    tablePane.setPreferredSize(new Dimension(420, 350));
    rightPanel.add(tablePane);

    // Add Listener to know which pane has focus
    tablePane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        if (focusedPane == 0) {
          bookSearch = searchBar.getText();
          searchBar.setText(userSearch);
          focusedPane = 1;
        } else {
          userSearch = searchBar.getText();
          searchBar.setText(bookSearch);
          focusedPane = 0;
        }
      }
    });

    dialog.setVisible(true);
  }

  void getInput() throws SQLException {
    if (userField.getText().isEmpty() || bookField.getText().isEmpty() || dateModel.getValue() == null) {
      JOptionPane.showMessageDialog(null, "Not All Fields Were Filled Out.", "Error Message",
          JOptionPane.ERROR_MESSAGE);
    } else {
      int userID = Integer.parseInt(userField.getText());
      int bookID = Integer.parseInt(bookField.getText());
      int day = dateModel.getDay();
      // + 1 because when selecting from the calendar GUI, it reduces the month by 1,
      // probably an error in the package code
      int month = dateModel.getMonth() + 1;
      int year = dateModel.getYear();
      String newDate = year + "-" + month + "-" + day;

      if (book.isAvailable(bookID)) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
          java.util.Date date = sdf.parse(newDate);
          java.sql.Date sqlDate = new java.sql.Date(date.getTime());
          transaction.addTransaction(bookID, userID, sqlDate);
          JOptionPane.showMessageDialog(null, "Checked-Out Successfully.");
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "Failed to add, please try again.");
          e.printStackTrace();
        }
      } else {
        JOptionPane.showMessageDialog(null, "Book is Unavailable!", "Error Message",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // Format for the Date Picker
  static class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
      return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
      if (value != null) {
        Calendar cal = (Calendar) value;
        return dateFormatter.format(cal.getTime());
      }

      return "";
    }

  }
}
