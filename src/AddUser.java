import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser 
{
    public static void AddUser(JFrame parent) {
        // Create a new JDialog to hold the popup window
        JDialog popup = new JDialog(parent, "Add User", true);
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popup.setSize(446, 326);
        popup.setLocationRelativeTo(null);
        popup.setResizable(false);

        // Create a new JPanel to hold the text fields and button
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout to arrange components in rows and columns
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create labels and text fields for user input
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstName = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastName = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField address = new JTextField();

        // Create the close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.dispose(); // Close the popup
            }
        });

        // Add components to the panel
        panel.add(firstNameLabel);
        panel.add(firstName);
        panel.add(lastNameLabel);
        panel.add(lastName);
        panel.add(addressLabel);
        panel.add(address);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(closeButton);

        // Add the panel to the popup
        popup.add(panel);

        // Make the popup visible after adding components
        popup.setVisible(true);
    }

}