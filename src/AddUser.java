import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser 
{
    // Logic for the add user button
    // Opens a popup window when the add user button is pressed
    public static void AddUser(JFrame parent)
    {
        // Create a new JFrame to hold the popup window
        JDialog popup = new JDialog(parent, "Add User", true);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popup.setSize(446, 326);
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
        popup.setResizable(false);

       // Create a new JPanel to hold the text fields and button
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        popup.add(panel);

        // Create a new JLabel for the first name text field
        JLabel firstNameLabel = new JLabel("First Name:");
        panel.add(firstNameLabel);

        // Create a new JTextField for the user to enter the first name
        JTextField firstName = new JTextField();
        panel.add(firstName);

        // Create a new JLabel for the last name text field
        JLabel lastNameLabel = new JLabel("Last Name:");
        panel.add(lastNameLabel);

        // Create a new JTextField for the user to enter the last name
        JTextField lastName = new JTextField();
        panel.add(lastName);

        // Create a new JLabel for the address text field
        JLabel addressLabel = new JLabel("Address:");
        panel.add(addressLabel);

        // Create a new JTextField for the user to enter the address
        JTextField address = new JTextField();
        panel.add(address);
    }

}