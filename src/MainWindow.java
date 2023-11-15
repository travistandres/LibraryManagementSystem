
// Travis Tan
// 10-23-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainWindow {
  JFrame frame;
  JPanel leftPanel;
  JPanel rightPanel;

  private int focusedPane = 0; // 1 will represent the userPane, 0 will represent the bookPane (if a third tab
                               // is added this will no longer work)
  private String bookSearch = "Search"; // Holds the text in the search bar so when searching the textarea will be
  // different depending on which table is being searched
  private String userSearch = "Search";

  private JButton addBook;
  private JButton addUser;
  private JButton checkIn;
  private JButton checkOut;

  static DefaultTableModel bookModel;
  static DefaultTableModel userModel;

  private JTextField mainSearch;

  JTable userTable;
  JTable bookTable;

  Runnable bookWorker;
  Thread bookThread;
  Runnable userWorker;
  Thread userThread;

  Thread bookThread2;
  Thread userThread2;

  User user = new User();
  Book book = new Book();

  MainWindow() {

    // Main Window
    frame = new JFrame("Library Management System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(892, 653);
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);

    Image icon = Toolkit.getDefaultToolkit().getImage("./src/book-stack.png");
    frame.setIconImage(icon);

    // Main Page
    JPanel main = new JPanel(new BorderLayout());
    main.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    frame.add(main);

    // Left Panel
    leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setPreferredSize(new Dimension(140, 0));
    leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
    main.add(leftPanel, BorderLayout.WEST);

    // Right Panel
    rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    rightPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
    main.add(rightPanel, BorderLayout.CENTER);

    // Buttons for the Left Panel
    // Add Book Button
    addBook = new JButton("Add Book");
    addBook.setPreferredSize(new Dimension(100, 50));
    addBook.setAlignmentX(Component.CENTER_ALIGNMENT);
    addBook.setMaximumSize(new Dimension(100, addBook.getMinimumSize().height));

    // Add Book Button Action Listener
    addBook.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            AddBook.openBookGUI(frame);
          }
        });
      }
    });

    // Add User Button
    addUser = new JButton("Add User");
    addUser.setPreferredSize(new Dimension(100, 50));
    addUser.setAlignmentX(Component.CENTER_ALIGNMENT);
    addUser.setMaximumSize(new Dimension(100, addUser.getMinimumSize().height));

    // Add User Button Action Listener
    addUser.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            AddUser.openAddUserGUI(frame);
          }
        });
      }
    });

    // Check-Out Button
    checkOut = new JButton("Check-Out");
    checkOut.setPreferredSize(new Dimension(100, 50));
    checkOut.setAlignmentX(Component.CENTER_ALIGNMENT);
    checkOut.setMaximumSize(new Dimension(100, checkOut.getMinimumSize().height));

    // Action Listener for Check-Out Button\
    // Pops up the Check Out Form
    checkOut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            new CheckOutForm(frame);
          }
        });
      }
    });

    // Check-In Button
    checkIn = new JButton("Check-In");
    checkIn.setPreferredSize(new Dimension(100, 50));
    checkIn.setAlignmentX(Component.CENTER_ALIGNMENT);
    checkIn.setMaximumSize(new Dimension(100, checkIn.getMinimumSize().height));

    // Action Listener for Check-In Button
    // Pops up the Check Out Form
    checkIn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            new CheckInForm(frame);
          }
        });
      }
    });

    // Adding Buttons to the Left Panel
    // Box.createRigidArea is just for padding (spacing) between the buttons
    leftPanel.add(Box.createRigidArea(new Dimension(0, 150)));
    leftPanel.add(addBook);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    leftPanel.add(addUser);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    leftPanel.add(checkOut);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    leftPanel.add(checkIn);

    // Panel for containing the search bar and Table
    JPanel TABLE = new JPanel();
    TABLE.setLayout(new BoxLayout(TABLE, BoxLayout.Y_AXIS));
    rightPanel.add(TABLE);

    // Search Bar
    mainSearch = new JTextField("Search");
    mainSearch.setPreferredSize(new Dimension(250, 30));
    // "Search" text prompt. Goes away after user clicks search box
    mainSearch.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        JTextField source = (JTextField) e.getComponent();
        source.setText("");
        bookSearch = "";
        userSearch = "";
        source.removeFocusListener(this);
      }
    });
    // #region Adding funtion to Search Bar DW 11/2/2023
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
          search.search(mainSearch.getText(), bookModel, bookTable);
        } else {
          search.search(mainSearch.getText(), userModel, userTable);
        }
      }
    };
    mainSearch.addKeyListener(keylistener);
    // #endregion

    // Adding Search Bar to TABLE pane
    TABLE.add(Box.createRigidArea(new Dimension(0, 30)));
    TABLE.add(mainSearch);
    TABLE.add(Box.createRigidArea(new Dimension(0, 10)));

    // Book Table Column Names
    String[] columnNames = { "Book ID", "Title", "Author", "Genre", "ISBN", "Availability" };
    // Creating Book Table
    bookTable = new JTable(new DefaultTableModel(null, columnNames));
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
    bookTable.getColumnModel().getColumn(0).setMaxWidth(70);

    // Adding Book Table to the Scroll Pane
    JScrollPane bookPane = new JScrollPane(bookTable);

    // Centering Text on each cell on Book Table
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames.length; i++) {
      bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // User Table Column Names
    String[] columnNames1 = { "User ID", "Name", "Phone Number" };

    // Creating User Table
    userTable = new JTable(new DefaultTableModel(null, columnNames1));
    userModel = (DefaultTableModel) userTable.getModel();
    // Starts a Thread to get Data from the database
    userWorker = new UserTableWorker(userModel);
    userThread = new Thread(userWorker);
    userThread.start();

    // column headers is not draggable
    userTable.getTableHeader().setReorderingAllowed(false);
    userTable.setDefaultEditor(Object.class, null);
    // can only highlight one row at a time
    userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Adding User Table to the Scroll Pane
    JScrollPane userPane = new JScrollPane(userTable);

    // Centering Text on each cell on User Table
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < columnNames1.length; i++) {
      userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Adding Tables to Tabs then to the TABLE Frame
    JTabbedPane tablePane = new JTabbedPane();
    tablePane.add("Books", bookPane);
    tablePane.add("Users", userPane);
    tablePane.setPreferredSize(new Dimension(700, 470));
    TABLE.add(tablePane);

    // Add listener to know which pane has focus
    tablePane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        if (focusedPane == 0) {
          bookSearch = mainSearch.getText();
          mainSearch.setText(userSearch);
          focusedPane = 1;
          // TT 11-04-23 clears focus on the Book Table when other tab has focus
          bookTable.clearSelection();
        } else {
          userSearch = mainSearch.getText();
          mainSearch.setText(bookSearch);
          focusedPane = 0;
          // TT 11-04-23 clears focus on the User Table
          userTable.clearSelection();
        }
      }
    });

    // Panel for containing the Edit and Remove buttons
    JPanel bottomRight = new JPanel();
    bottomRight.setLayout(new BoxLayout(bottomRight, BoxLayout.X_AXIS));
    rightPanel.add(bottomRight);

    // Creating the Edit and Remove buttons
    JButton edit = new JButton("Edit");
    edit.setEnabled(false);
    JButton remove = new JButton("Remove");
    remove.setEnabled(false);

    // Action Listener for Edit button
    edit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // if User Table is in focus, pops up an edit form for users
        int userTableFocus = userTable.getSelectedRow();
        if (userTableFocus >= 0) {
          int user_ID = Integer.valueOf((String) userTable.getModel().getValueAt(userTableFocus, 0));
          String name = (String) userTable.getModel().getValueAt(userTableFocus, 1);
          String phoneNumber = (String) userTable.getModel().getValueAt(userTableFocus, 2);
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              new EditUserForm(frame, user_ID, name, phoneNumber);
            }
          });
        }

        // if Book Table is in focus, pops up an edit form for books
        int bookTableFocus = bookTable.getSelectedRow();
        if (bookTableFocus >= 0) {
          int book_ID = Integer.valueOf((String) bookTable.getModel().getValueAt(bookTableFocus, 0));
          String title = (String) bookTable.getModel().getValueAt(bookTableFocus, 1);
          String author = (String) bookTable.getModel().getValueAt(bookTableFocus, 2);
          String genre = (String) bookTable.getModel().getValueAt(bookTableFocus, 3);
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              new EditBookForm(frame, book_ID, title, author, genre);
            }
          });
        }
      }
    });

    // Action Listener for Remove button
    remove.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // if Book Table is in focus, pops up a confirmation for removing selected book
        int bookSelectedRow = bookTable.getSelectedRow();
        if (bookSelectedRow >= 0) {
          String message = "Are you sure you want to remove \""
              + bookTable.getModel().getValueAt(bookSelectedRow, 1)
              + "\" by " + bookTable.getModel().getValueAt(bookSelectedRow, 2) + "?";
          int reply = JOptionPane.showConfirmDialog(null, message, "Remove Book", JOptionPane.YES_NO_OPTION);
          if (reply == JOptionPane.YES_OPTION) {
            String message1 = "Are you REALLY sure you want to remove \""
                + bookTable.getModel().getValueAt(bookSelectedRow, 1)
                + "\" by " + bookTable.getModel().getValueAt(bookSelectedRow, 2) + "?  (This action cannot be undone)";
            int reply1 = JOptionPane.showConfirmDialog(null, message1, "Remove Book", JOptionPane.YES_NO_OPTION);
            if (reply1 == JOptionPane.YES_OPTION) {
              // Deletes the data in the database
              int book_ID = Integer.valueOf((String) bookTable.getModel().getValueAt(bookSelectedRow, 0));
              try {
                book.deleteBook(book_ID);
              } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Failed to remove, please try again.");
                e1.printStackTrace();
              }
              bookModel.removeRow(bookSelectedRow);
              JOptionPane.showMessageDialog(null, "Book removed");
            } else {
              JOptionPane.showMessageDialog(null, "Removal of book canceled.");
            }
          } else {
            JOptionPane.showMessageDialog(null, "Removal of book canceled.");
          }
        }

        // if User Table is in focus, pops up a confirmation for removing selected user
        int userSelectedRow = userTable.getSelectedRow();
        if (userSelectedRow >= 0) {
          String message = "Are you sure you want to remove \""
              + userTable.getModel().getValueAt(userSelectedRow, 1)
              + "\"?";
          int reply = JOptionPane.showConfirmDialog(null, message, "Remove User", JOptionPane.YES_NO_OPTION);
          if (reply == JOptionPane.YES_OPTION) {
            String message1 = "Are you REALLY sure you want to remove \""
                + userTable.getModel().getValueAt(userSelectedRow, 1)
                + "\"? (This action cannot be undone)";
            int reply1 = JOptionPane.showConfirmDialog(null, message1, "Remove User", JOptionPane.YES_NO_OPTION);
            if (reply1 == JOptionPane.YES_OPTION) {
              // Deletes the data in the database
              int user_ID = Integer.valueOf((String) userTable.getModel().getValueAt(userSelectedRow, 0));
              try {
                user.deleteUser(user_ID);
              } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Failed to remove, please try again.");
                e1.printStackTrace();
              }
              userModel.removeRow(userSelectedRow);
              JOptionPane.showMessageDialog(null, "User removed");
            } else {
              JOptionPane.showMessageDialog(null, "Removal of user canceled.");
            }
          } else {
            JOptionPane.showMessageDialog(null, "Removal of user canceled.");
          }
        }
      }
    });

    // Enabling Buttons when Book Table is in focus
    bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        // enables the Edit and Remove button if a row is selected in the Book Table
        edit.setEnabled(true);
        remove.setEnabled(true);

        // disables the Edit and Remove buttons when it loses focus
        if (bookTable.getSelectionModel().isSelectionEmpty()) {
          edit.setEnabled(false);
          remove.setEnabled(false);
        }
      }
    });

    // Enabling Buttons when User Table is in focus
    userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {

        // enables the Edit and Remove button if a row is selected in the User Table
        edit.setEnabled(true);
        remove.setEnabled(true);

        // disables the Edit and Remove buttons when it loses focus
        if (userTable.getSelectionModel().isSelectionEmpty()) {
          edit.setEnabled(false);
          remove.setEnabled(false);
        }
      }
    });

    // Adding and placing the buttons
    bottomRight.add(Box.createRigidArea(new Dimension(552, 0)));
    bottomRight.add(edit);
    bottomRight.add(Box.createRigidArea(new Dimension(10, 0)));
    bottomRight.add(remove);
  }
}
