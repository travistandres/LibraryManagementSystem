
// Travis Tan
// 10-23-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;

public class CheckOutForm {
  JDialog dialog;

  JLabel ID;
  JLabel ISBN;
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

  User user = new User();
  Book book = new Book();
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
    ID = new JLabel("User ID:");
    ISBN = new JLabel("Book ID:");
    returnDate = new JLabel("Return Date:");

    // Text Fields
    userField = new JTextField();
    bookField = new JTextField();

    // ID JLabel
    m.gridx = 0;
    m.gridy = 0;
    m.weightx = 0;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(ID, m);

    // ISBN JLabel
    m.gridx = 0;
    m.gridy = 1;
    m.weightx = 0;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(ISBN, m);

    // Return Date JLabel
    m.gridx = 0;
    m.gridy = 2;
    m.weightx = 0;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(returnDate, m);

    // ID # text field
    m.gridx = 1;
    m.gridy = 0;
    m.weightx = 1;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(userField, m);

    // ISBN text field
    m.gridx = 1;
    m.gridy = 1;
    m.weightx = 1;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(bookField, m);

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
        getInput();
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
        source.removeFocusListener(this);
      }
    });
    rightPanel.add(searchBar);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Book Table Column Names
    String[] columnNames = { "Book ID", "Title", "Author", "Genre", "ISBN" };

    // Creating Book Table
    JTable bookTable = new JTable(new DefaultTableModel(null, columnNames));
    bookModel = (DefaultTableModel) bookTable.getModel();
    bookTable.getTableHeader().setReorderingAllowed(false);
    bookTable.setDefaultEditor(Object.class, null);

    // Adding Book Table to the Scroll Pane
    JScrollPane bookPane = new JScrollPane(bookTable);

    // Centering Text on each cell on Book Table
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames.length; i++) {
      bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Adding data to User Table
    try {
      book.displayBooks(bookModel);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // User Table Column Names
    String[] columnNames1 = { "User ID", "Name", "Phone Number" };

    // Creating User Table
    JTable userTable = new JTable(new DefaultTableModel(null, columnNames1));
    userModel = (DefaultTableModel) userTable.getModel();
    userTable.getTableHeader().setReorderingAllowed(false);
    userTable.setDefaultEditor(Object.class, null);

    // Adding User Table to the Scroll Pane
    JScrollPane userPane = new JScrollPane(userTable);

    // Centering Text on each cell on the User Table
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames1.length; i++) {
      userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Adding data to User Table
    try {
      user.displayUsers(userModel);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    // Adding Tables to Tabs then to the TABLE Frame
    JTabbedPane tablePane = new JTabbedPane();
    tablePane.add("Books", bookPane);
    tablePane.add("Users", userPane);
    tablePane.setPreferredSize(new Dimension(420, 350));
    rightPanel.add(tablePane);

    dialog.setVisible(true);
  }

  void getInput() {
    if (userField.getText().isEmpty() || bookField.getText().isEmpty() || dateModel.getValue() == null) {
      JOptionPane.showMessageDialog(null, "Not All Fields Were Filled Out.", "Invalid TextFields",
          JOptionPane.ERROR_MESSAGE);
    } else {
      int userID = Integer.parseInt(userField.getText());
      int bookID = Integer.parseInt(bookField.getText());
      int day = dateModel.getDay();
      // + 1 because when selecting from the calendar GUI, it reduces the month by 1,
      // probably an error in the package code
      int month = dateModel.getMonth() + 1;
      int year = dateModel.getYear();
      String date = year + "-" + month + "-" + day;

      // insert method here for adding a transaction into the Transaction Table
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
