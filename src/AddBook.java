// Jaxon Elwell
// 10-28-2020

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbn = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField author = new JTextField();
        JLabel genreLabel = new JLabel("Genre:");
        String[] genreStrings = { "Fiction", "NonFiction", "Mystery", "Young Adult", "Science Fiction", "Fantasy", "Horror", "Romance", "Historical Fiction", "Other" };
        JComboBox genreList = new JComboBox(genreStrings);


        textFieldPanel.add(titleLabel);
        textFieldPanel.add(title);
        textFieldPanel.add(isbnLabel);
        textFieldPanel.add(isbn);
        textFieldPanel.add(authorLabel);
        textFieldPanel.add(author);
        textFieldPanel.add(genreLabel);
        textFieldPanel.add(genreList);

        // Create a panel for the Add and Add Another buttons with padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        // Add Button
        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any of the text fields are empty
                if(title.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a title.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(isbn.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter an ISBN.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(author.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter an author.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(Book.verifyISBN(isbn.getText()))
                {
                    JOptionPane.showMessageDialog(null, "ISBN is already taken.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }                                                                                                  

                // Save the text entered in the text fields
                String titleText = title.getText();
                String isbnString = isbn.getText();
                String authorText = author.getText();
                String genreText = genreList.getSelectedItem().toString();
                // Try catch block for the addBook method
                try 
                {
                    Book book = new Book();
                    book.addBook(titleText, authorText, isbnString, genreText); // Call the addBook method in the Book class to add the book to the database
                    addBookFrame.dispose(); // Close the Add Book GUI
                } catch (Exception exception) {
                    errorPopup();
                    exception.printStackTrace();
                }
            }
        });


        // Add some rigid space between the buttons
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons


        // Add Another Button
        JButton addAnotherButton = new JButton("Add Another");
        addAnotherButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addAnotherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any of the text fields are empty
                if(title.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a title.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(isbn.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter an ISBN.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(author.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter an author.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }


                // Check if the ISBN is a duplicate
                if(Book.verifyISBN(isbn.getText()))
                {
                    JOptionPane.showMessageDialog(null, "ISBN is already taken.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the text entered in the text fields
                String titleText = title.getText();
                String isbnString = isbn.getText();
                String authorText = author.getText();
                String genreText = genreList.getSelectedItem().toString();
                // Call the addBook method in the Book class to add the book to the database
                // Try catch block for the addBook method
                try 
                {
                    Book book = new Book();
                    book.addBook(titleText, authorText, isbnString, genreText); // Call the addBook method in the Book class to add the book to the database
                    openBookGUI(parent); // Open another Add Book GUI
                    addBookFrame.dispose(); // Close the Add Book GUI
                } catch (Exception exception) {
                    errorPopup();
                    exception.printStackTrace();
                }
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


    // Method to display an error popup
    public static void errorPopup() 
    {
        JFrame errorFrame = new JFrame("Error");
        errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        errorFrame.setSize(300, 150);
        errorFrame.setLocationRelativeTo(null);

        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        errorFrame.add(errorPanel);

        JLabel errorLabel = new JLabel("Error adding book.");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 24));
        errorPanel.add(errorLabel);

        errorFrame.setVisible(true);
    }

    
}
