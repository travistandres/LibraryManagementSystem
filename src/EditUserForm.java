
//Travis Tan
// 11-06-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

public class EditUserForm {
  JDialog dialog;

  JLabel fullNameLabel;
  JLabel phoneNumberLabel;

  JTextField nameTextField;
  JTextField phoneTextField;

  JButton saveButton;

  User user = new User();

  EditUserForm(JFrame frame, String name, String phoneNumber) {
    dialog = new JDialog(frame);
    dialog.setTitle("Edit User Form");
    dialog.setSize(300, 350);
    dialog.setLocationRelativeTo(frame);
    dialog.setResizable(false);

    // Main Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setSize(200, 300);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    dialog.add(mainPanel);

    // User Input Panel
    JPanel inputPanel = new JPanel();
    inputPanel.setPreferredSize(new Dimension(400, 0));
    inputPanel.setLayout(new GridBagLayout());
    inputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Edit User"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    GridBagConstraints gbc = new GridBagConstraints();

    // Text labels
    fullNameLabel = new JLabel("Full Name:");
    phoneNumberLabel = new JLabel("Phone Number:");

    // Input Fields. Fields are automatically filled depending on what row they
    // selected
    nameTextField = new JTextField(name);
    phoneTextField = new JTextField(phoneNumber);

    // Layout Properties and Adding it to the inputPanel
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(fullNameLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(phoneNumberLabel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(nameTextField, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(phoneTextField, gbc);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setPreferredSize(new Dimension(400, 100));

    // Save Button
    saveButton = new JButton("Save Changes");
    saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    buttonPanel.add(saveButton);

    // Action Listener for Save Button
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          getInput();
          JOptionPane.showMessageDialog(null, "Edits have been saved.");
          dialog.dispose();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });

    // Cancel Panel
    JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    // Cancel Button
    JButton cancel = new JButton("Cancel");
    cancelPanel.add(cancel);

    // Action Listener for Cancel Button
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog.dispose();
      }
    });

    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    mainPanel.add(inputPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    mainPanel.add(buttonPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    mainPanel.add(cancelPanel);

    dialog.setVisible(true);
  }

  void getInput() throws SQLException {
    String name = nameTextField.getText();
    String phoneNumber = phoneTextField.getText();

    // Input Alter method here for database.

    Runnable userWorker = new UserTableWorker(MainWindow.userModel);
    Thread userThread = new Thread(userWorker);
    userThread.start();
    MainWindow.userModel.setRowCount(0);
  }
}
