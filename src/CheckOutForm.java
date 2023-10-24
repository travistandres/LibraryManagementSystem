
// Travis Tan
// 10-23-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
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

  JTextField idField;
  JTextField isbnField;
  JTextField searchBar;

  JButton cancel;
  JButton checkOut;

  UtilDateModel dateModel;

  private DefaultTableModel model;

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
    leftPanel.setPreferredSize(new Dimension(330, 438));
    mainPanel.add(leftPanel, BorderLayout.WEST);

    // User Input Panel
    JPanel userInputPanel = new JPanel(new GridBagLayout());
    userInputPanel.setPreferredSize(new Dimension(300, 200));
    GridBagConstraints m = new GridBagConstraints();
    leftPanel.add(userInputPanel);

    // Text labels
    ID = new JLabel("ID #:");
    ISBN = new JLabel("ISBN:");
    returnDate = new JLabel("Return Date:");

    // Text Fields
    idField = new JTextField();
    isbnField = new JTextField();

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
    userInputPanel.add(idField, m);

    // ISBN text field
    m.gridx = 1;
    m.gridy = 1;
    m.weightx = 1;
    m.fill = GridBagConstraints.HORIZONTAL;
    m.insets = new Insets(5, 5, 5, 5);
    userInputPanel.add(isbnField, m);

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
    checkOutJPanel.setPreferredSize(new Dimension(300, 30));
    leftPanel.add(checkOutJPanel);

    // check out button
    checkOut = new JButton("Check-Out");
    checkOut.setAlignmentX(Component.CENTER_ALIGNMENT);
    checkOutJPanel.add(checkOut);

    // Check Out button action listener
    checkOut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Travis Tan 10-23-23
        // Gets the input of the Calendar input to a string
        getInput();
      }
    });

    // Panel that contains the Cancel button
    JPanel cancelPanel = new JPanel();
    cancelPanel.setLayout(new BoxLayout(cancelPanel, BoxLayout.Y_AXIS));
    cancelPanel.setPreferredSize(new Dimension(330, 238));
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
    model = (DefaultTableModel) bookTable.getModel();
    // Adding Book Table to the Scroll Pane
    JScrollPane bookPane = new JScrollPane(bookTable);
    bookTable.getTableHeader().setReorderingAllowed(false);

    // User Table Column Names
    String[] columnNames1 = { "User ID", "Name", "Phone Number" };
    // Creating User Table
    JTable userTable = new JTable(new DefaultTableModel(null, columnNames1));
    model = (DefaultTableModel) userTable.getModel();
    // Adding User Table to the Scroll Pane
    JScrollPane userPane = new JScrollPane(userTable);
    userTable.getTableHeader().setReorderingAllowed(false);

    // Adding Tables to Tabs then to the TABLE Frame
    JTabbedPane tablePane = new JTabbedPane();
    tablePane.add("Books", bookPane);
    tablePane.add("Users", userPane);
    tablePane.setPreferredSize(new Dimension(350, 350));
    rightPanel.add(tablePane);

    dialog.setVisible(true);
  }

  void getInput() {
    if (idField.getText().isEmpty() || isbnField.getText().isEmpty() || dateModel.getValue() == null) {
      JOptionPane.showMessageDialog(null, "Not All Fields Were Filled Out.", "Invalid TextFields",
          JOptionPane.ERROR_MESSAGE);
    } else {
      String date = dateModel.getValue().toString();
      System.out.println(date);
    }
  }

  // Format for the Date Picker
  static class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "MM-dd-yyyy";
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
