
//Travis Tan
// 11-06-23
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

public class EditBookForm {
  JDialog dialog;

  JLabel titleLabel;
  JLabel authorLabel;
  JLabel genreLabel;

  JTextField titleField;
  JTextField authorField;
  JComboBox genreList;

  JButton saveButton;

  Book book = new Book();

  EditBookForm(JFrame frame, int book_ID, String title, String author, String genre) {
    dialog = new JDialog(frame);
    dialog.setTitle("Edit Book Form");
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
    inputPanel.setPreferredSize(new Dimension(300, 0));
    inputPanel.setLayout(new GridBagLayout());
    inputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Edit Book"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    GridBagConstraints gbc = new GridBagConstraints();

    // Text labels
    titleLabel = new JLabel("Title:");
    authorLabel = new JLabel("Author:");
    genreLabel = new JLabel("Genre:");

    // Input Fields. Fields are automatically filled depending on what row they
    // selected
    titleField = new JTextField(title);
    authorField = new JTextField(author);
    // Genre ComboBox
    String[] genreStrings = { "Fiction", "NonFiction", "Mystery", "Young Adult", "Science Fiction", "Fantasy", "Horror",
        "Romance", "Historical Fiction", "Other" };
    genreList = new JComboBox(genreStrings);
    genreList.setSelectedItem(genre);

    // Layout Properties and Adding it to the inputPanel
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(titleLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(authorLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(genreLabel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(titleField, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(authorField, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    inputPanel.add(genreList, gbc);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setPreferredSize(new Dimension(300, 100));

    // Save Button
    saveButton = new JButton("Save Changes");
    saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    buttonPanel.add(saveButton);

    // Action Listener for Save Button
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          getInput(book_ID);
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
    mainPanel.add(cancelPanel);

    dialog.setVisible(true);
  }

  void getInput(int book_ID) throws SQLException {
    String title = titleField.getText();
    String author = authorField.getText();
    String genre = genreList.getSelectedItem().toString();

    try {
      book.updateBook(book_ID, title, author, genre);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Failed to edit, please try again.");
    }

    Runnable bookWorker = new BookTableWorker(MainWindow.bookModel);
    Thread bookThread = new Thread(bookWorker);
    bookThread.start();
    MainWindow.bookModel.setRowCount(0);
  }
}
