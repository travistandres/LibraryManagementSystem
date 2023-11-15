// Jaxon Elwell
// 10-26-2020

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddUser {
    public static void openAddUserGUI(JFrame parent) {
        // Create the main JFrame for the Add User GUI
        JDialog dialog = new JDialog(parent);
        dialog.setTitle("Add User Form");
        dialog.setSize(300, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        // Create a main panel with GridBagLayout and add some padding
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
        textFieldPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Add User"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstName = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastName = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phone = new JTextField();

        phone.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {

                // TT 11-15-23 Max 10 Characters can input
                if (phone.getText().length() >= 10) {
                    e.consume();
                }

                // TT 11-15-23
                // Makes sure only numbers are in the text field
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

        // First Name Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(firstNameLabel, gbc);

        // Last Name Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(lastNameLabel, gbc);

        // Phone Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(phoneLabel, gbc);

        // First Name Text Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(firstName, gbc);

        // Last Name Text Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(lastName, gbc);

        // Phone Text Field
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        textFieldPanel.add(phone, gbc);

        // Create a panel for the Add and Add Another buttons with padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(300, 50));

        // Add Button
        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any of the text fields are empty
                if (firstName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a first name.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (lastName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a last name.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a phone number.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the text entered in the text fields
                String fullName = firstName.getText() + " " + lastName.getText();
                String phoneNumber = phone.getText();

                // Check if full name is longer than 30 characters
                if(checkNameLength(fullName))
                {
                    JOptionPane.showMessageDialog(null, "Full name character limit is 30.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the addUser method in the User class to add the user to the database
                // Try catch block for the addUser method
                try {
                    User user = new User();
                    user.addUser(fullName, phoneNumber);
                    Runnable userWorker = new UserTableWorker(MainWindow.userModel);
                    Thread userThread = new Thread(userWorker);
                    userThread.start();
                    MainWindow.userModel.setRowCount(0);
                    dialog.dispose(); // Close the Add User GUI
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
        addAnotherButton.setPreferredSize(new Dimension(115, 30)); // Set preferred size
        addAnotherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a first name.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (lastName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a last name.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a phone number.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the text entered in the text fields
                String fullName = firstName.getText() + " " + lastName.getText();
                String phoneNumber = phone.getText();

                // Check if full name is longer than 30 characters
                if(checkNameLength(fullName))
                {
                    JOptionPane.showMessageDialog(null, "Full name character limit is 30.", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the addUser method in the User class to add the user to the database
                // Try catch block for the addUser method
                try {
                    User user = new User();
                    user.addUser(fullName, phoneNumber);
                    Runnable userWorker = new UserTableWorker(MainWindow.userModel);
                    Thread userThread = new Thread(userWorker);
                    userThread.start();
                    MainWindow.userModel.setRowCount(0);
                    dialog.dispose(); // Close the Add User GUI
                    openAddUserGUI(parent); // Open another Add User GUI
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

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(textFieldPanel, gbc);
        mainPanel.add(buttonPanel, gbc);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 70)));
        mainPanel.add(cancelButtonPanel, gbc);

        dialog.setVisible(true);
    }

    // method to check if name is longer than 30 characters
    public static boolean checkNameLength(String name) {
        if (name.length() > 30) {
            return true;
        }
        return false;
    }
}