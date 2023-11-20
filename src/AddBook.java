// Jaxon Elwell
// 10-28-2020

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddBook {
    public static void openBookGUI(JFrame parent) {
        // Create the main JFrame for the Add Book GUI
        JDialog dialog = new JDialog(parent);
        dialog.setTitle("Add Book Form");
        dialog.setSize(300, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        // Main Panel
        // TT 11-13-23 Created a Main panel with a BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setSize(300, 350);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(mainPanel);

        // Create a panel for the text fields with padding
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setPreferredSize(new Dimension(400, 0));
        textFieldPanel.setLayout(new GridBagLayout());
        textFieldPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Add Book"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Title:");
        JTextField title = new JTextField();
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbn = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField author = new JTextField();
        JLabel genreLabel = new JLabel("Genre:");
        String[] genreStrings = { "Fiction", "NonFiction", "Mystery", "Young Adult", "Science Fiction", "Fantasy",
                "Horror", "Romance", "Historical Fiction", "Other" };
        JComboBox genreList = new JComboBox(genreStrings);

        isbn.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {

                // TT 11-15-23 Max 17 Characters can input
                if (isbn.getText().length() >= 17) {
                    e.consume();
                }

                // TT 11-15-23
                // Makes sure only numbers and '-' are in the text field
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') || (c == '-') || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

        // TT 11-13-23
        // Layout Properties and adding it to the textFieldPanel
        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(titleLabel, gbc);

        // Author Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(authorLabel, gbc);

        // Genre Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(genreLabel, gbc);

        // ISBN Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(isbnLabel, gbc);

        // Title Text Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(title, gbc);

        // Author Text Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(author, gbc);

        // Genre ComboBox
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(genreList, gbc);

        // ISBN Text Field
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(isbn, gbc);

        // Create a panel for the Add and Add Another buttons with padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(300, 50));

        // Add Button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any of the text fields are empty
                if (title.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a title.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isbn.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an ISBN.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (author.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an author.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (title.getText().length() >= 50) {
                    JOptionPane.showMessageDialog(null, "Only 50 Characters is allowed for title");
                }
                if (author.getText().length() >= 50) {
                    JOptionPane.showMessageDialog(null, "Only 50 Characters is allowed for author");
                }

                // Save the text entered in the text fields
                String titleText = title.getText();
                String authorText = author.getText();
                String genreText = genreList.getSelectedItem().toString();
                String isbnString = formatISBN(isbn.getText());

                // Check if the ISBN is of correct length
                if (verifyLength(isbnString) == false) {
                    JOptionPane.showMessageDialog(null, "ISBN is not the correct length.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Book.verifyISBN(isbn.getText())) {
                    JOptionPane.showMessageDialog(null, "ISBN is already taken.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Try catch block for the addBook method
                try {
                    Book book = new Book();
                    book.addBook(titleText, authorText, isbnString, genreText); // Call the addBook method in the Book
                                                                                // class to add the book to the database
                    Runnable bookWorker = new BookTableWorker(MainWindow.bookModel);
                    Thread bookThread = new Thread(bookWorker);
                    bookThread.start();
                    MainWindow.bookModel.setRowCount(0);
                    dialog.dispose(); // Close the Add Book GUI
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Failed to add, please try again.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                }
            }
        });

        // Add some rigid space between the buttons
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons

        // Add Another Button
        JButton addAnotherButton = new JButton("Add Another");
        addAnotherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any of the text fields are empty
                if (title.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a title.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isbn.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an ISBN.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (author.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an author.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the text entered in the text fields
                String titleText = title.getText();
                String authorText = author.getText();
                String genreText = genreList.getSelectedItem().toString();
                String isbnString = formatISBN(isbn.getText());

                // Check if the ISBN is of correct length
                if (verifyLength(isbnString) == false) {
                    JOptionPane.showMessageDialog(null, "ISBN is not the correct length.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if the ISBN is a duplicate
                if (Book.verifyISBN(isbnString)) {
                    JOptionPane.showMessageDialog(null, "ISBN is already taken.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the addBook method in the Book class to add the book to the database
                // Try catch block for the addBook method
                try {
                    Book book = new Book();
                    book.addBook(titleText, authorText, isbnString, genreText); // Call the addBook method in the Book
                                                                                // class to add the book to the database
                    Runnable bookWorker = new BookTableWorker(MainWindow.bookModel);
                    Thread bookThread = new Thread(bookWorker);
                    bookThread.start();
                    MainWindow.bookModel.setRowCount(0);
                    openBookGUI(parent); // Open another Add Book GUI
                    dialog.dispose(); // Close the Add Book GUI
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Failed to add, please try again.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                }
            }
        });

        // Add glue components to center-align the buttons
        buttonPanel.add(addAnotherButton);

        // Create a panel for the Cancel button with padding
        JPanel cancelButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton closeButton = new JButton("Cancel");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the Add User GUI
            }
        });
        cancelButtonPanel.add(closeButton);

        // Adding Panels to Main Panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(textFieldPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(cancelButtonPanel);

        title.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {

                // TT 11-20-23 Max 9 Characters can input
                if (title.getText().length() >= 50) {
                    e.consume();
                }
            }
        });

        author.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {

                // TT 11-20-23 Max 9 Characters can input
                if (author.getText().length() >= 50) {
                    e.consume();
                }
            }
        });

        // Make the main frame visible
        dialog.setVisible(true);
    }

    // Method to insure proper ISBN format
    public static String formatISBN(String isbn) {
        String formattedISBN = "";
        String strippedISBN = isbn.replaceAll("[^0-9]", "");
        if (strippedISBN.length() == 13) {
            formattedISBN = strippedISBN.substring(0, 3)
                    + "-" + strippedISBN.substring(3, 4)
                    + "-" + strippedISBN.substring(4, 9)
                    + "-" + strippedISBN.substring(9, 12)
                    + "-" + strippedISBN.substring(12, 13);
        }
        return formattedISBN;
    }

    // Method to check if ISBN is correct
    public static boolean verifyLength(String isbn) {
        if (isbn.length() == 17) {
            return true;
        }
        return false;
    }
}