// Jaxon Elwell
// 10-26-2020

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser {
    public static void openAddUserGUI(JFrame parent) {
        // Create the main JFrame for the Add User GUI
        JFrame addUserFrame = new JFrame("Add User");
        addUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addUserFrame.setSize(400, 550);
        addUserFrame.setLocationRelativeTo(parent);

        // Create a main panel with GridBagLayout and add some padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addUserFrame.add(mainPanel);

        // Create a panel for the title label
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Add User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Create a panel for the text fields with padding
        JPanel textFieldPanel = new JPanel(new GridLayout(4, 2, 40, 20));
        textFieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstName = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastName = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phone = new JTextField();


        textFieldPanel.add(firstNameLabel);
        textFieldPanel.add(firstName);
        textFieldPanel.add(lastNameLabel);
        textFieldPanel.add(lastName);
        textFieldPanel.add(phoneLabel);
        textFieldPanel.add(phone);

        // Create a panel for the Add and Add Another buttons with padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        // Add Button
        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Check if any of the text fields are empty
                if(firstName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a first name.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(lastName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a last name.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(phone.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a phone number.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the text entered in the text fields
                String fullName = firstName.getText() + " " + lastName.getText();
                String phoneNumber = phone.getText();

                // Call the addUser method in the User class to add the user to the database
                // Try catch block for the addUser method
                try {
                    User user = new User();
                    user.addUser(fullName, phoneNumber);
                    Runnable userWorker = new UserTableWorker(MainWindow.userModel);
                    Thread userThread = new Thread(userWorker);
                    userThread.start();
                    MainWindow.userModel.setRowCount(0);
                    addUserFrame.dispose(); // Close the Add User GUI
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
            public void actionPerformed(ActionEvent e) 
            {
                if(firstName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a first name.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(lastName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a last name.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(phone.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please enter a phone number.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the text entered in the text fields
                String fullName = firstName.getText() + " " + lastName.getText();
                String phoneNumber = phone.getText();

                // Call the addUser method in the User class to add the user to the database
                // Try catch block for the addUser method
                try {
                    User user = new User();
                    user.addUser(fullName, phoneNumber);
                    Runnable userWorker = new UserTableWorker(MainWindow.userModel);
                    Thread userThread = new Thread(userWorker);
                    userThread.start();
                    MainWindow.userModel.setRowCount(0);
                    addUserFrame.dispose(); // Close the Add User GUI
                    openAddUserGUI(parent); // Open another Add User GUI
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
                addUserFrame.dispose(); // Close the Add User GUI
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
        addUserFrame.setVisible(true);
    }

    // Method to make an error popup for when adding a user fails
    public static void errorPopup() {
        JFrame errorFrame = new JFrame("Error");
        errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        errorFrame.setSize(300, 150);
        errorFrame.setLocationRelativeTo(null);

        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        errorFrame.add(errorPanel);

        JLabel errorLabel = new JLabel("Error adding user.");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 24));
        errorPanel.add(errorLabel);

        errorFrame.setVisible(true);
    }
}