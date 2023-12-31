
//Travis Tan
// 11-06-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

  EditUserForm(JFrame frame, int user_ID, String name, String phoneNumber) {
    dialog = new JDialog(frame);
    dialog.setTitle("Edit User Form");
    dialog.setSize(300, 350);
    dialog.setLocationRelativeTo(frame);
    dialog.setResizable(false);

    // Main Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setSize(300, 350);
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

    phoneTextField.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {

        // TT 11-15-23 Max 10 Characters can input
        if (phoneTextField.getText().length() >= 10) {
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
        if (!(phoneTextField.getText().length() == 10)) {
          JOptionPane.showMessageDialog(null, "Invalid Phone Number", "Error Message", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (!(nameTextField.getText().contains(" "))) {
          JOptionPane.showMessageDialog(null, "Invalid Name", "Error Message", JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          getInput(user_ID);
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

    nameTextField.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {

        // TT 11-15-23 Max 30 Characters can input
        if (nameTextField.getText().length() >= 30) {
          e.consume();
        }

        // TT 11-15-23
        // Makes sure to can't put a '*' on the text field
        char c = e.getKeyChar();
        if (c == '*') {
          e.consume();
        }
      }
    });

    dialog.setVisible(true);
  }

  void getInput(int user_ID) throws SQLException {
    String name = nameTextField.getText();
    String phoneNumber = phoneTextField.getText();

    try {
      user.updateUser(user_ID, name, phoneNumber);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Failed to edit, please try again.");
    }

    Runnable userWorker = new UserTableWorker(MainWindow.userModel);
    Thread userThread = new Thread(userWorker);
    userThread.start();
    MainWindow.userModel.setRowCount(0);
  }
}
