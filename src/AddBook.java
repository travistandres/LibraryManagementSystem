// Jaxon Elwell
// 10-28-2020

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddBook {
    public static void openBookGUI(JFrame parent) {
        // Create the main JFrame for the Add Book GUI
        JFrame addBookFrame = new JFrame("Add Book");
        addBookFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addBookFrame.setSize(400, 550);
        addBookFrame.setLocationRelativeTo(parent);

        // Create a main panel with GridBagLayout and add some padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addBookFrame.add(mainPanel);

        // Create a panel for the title label
        JPanel titlePanel = new JPanel();
        JLabel windowLabel = new JLabel("Add Book");
        windowLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(windowLabel);

        // Create a panel for the text fields with padding
        JPanel textFieldPanel = new JPanel(new GridLayout(4, 2, 40, 20));
        textFieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel titleLabel = new JLabel("Title:");
        JTextField title = new JTextField();
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genre = new JTextField();
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbn = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField author = new JTextField();


        textFieldPanel.add(titleLabel);
        textFieldPanel.add(title);
        textFieldPanel.add(genreLabel);
        textFieldPanel.add(genre);
        textFieldPanel.add(isbnLabel);
        textFieldPanel.add(isbn);
        textFieldPanel.add(authorLabel);
        textFieldPanel.add(author);

        // Create a panel for the Add and Add Another buttons with padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the text entered in the text fields
                String titleText = title.getText();
                String genreText = genre.getText();
                String isbnNumber = isbn.getText();
                String authorText = author.getText();
                // Call the addBook method in the Book class to add the book to the database
                Book.addBook(titleText,  genreText, isbnNumber, authorText);
            }
        });

        // Add some rigid space between the buttons
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons

        JButton addAnotherButton = new JButton("Add Another");
        addAnotherButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addAnotherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the text entered in the text fields
                String titleText = title.getText();
                String genreText = genre.getText();
                String isbnNumber = isbn.getText();
                String authorText = author.getText();
                // Call the addBook method in the Book class to add the book to the database
                Book.addBook(titleText,  genreText, isbnNumber, authorText);
                openBookGUI(parent); // Open another Add User GUI
                // Close the current Add User GUI
                addBookFrame.dispose();
            }
        });

        // Add glue components to center-align the buttons
        buttonPanel.add(addAnotherButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // Create a panel for the Cancel button with padding
        JPanel cancelButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton closeButton = new JButton("Cancel");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookFrame.dispose(); // Close the Add User GUI
            }
        });
        cancelButtonPanel.add(closeButton);

        // Specify GridBagLayout constraints to center-align the title panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 60, 0); // Add space below the title
        mainPanel.add(titlePanel, gbc);

        // Specify GridBagLayout constraints to center-align the text field panel
        gbc.gridy = 1;
        mainPanel.add(textFieldPanel, gbc);

        // Specify GridBagLayout constraints for the button panel
        gbc.gridy = 2;
        mainPanel.add(buttonPanel, gbc);

        // Specify GridBagLayout constraints for the close button panel
        gbc.gridy = 3;
        mainPanel.add(cancelButtonPanel, gbc);

        // Make the main frame visible
        addBookFrame.setVisible(true);
    }
}
