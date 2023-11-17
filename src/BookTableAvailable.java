import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class BookTableAvailable implements Runnable {

  Book book = new Book();
  DefaultTableModel model;

  BookTableAvailable(DefaultTableModel model) {
    this.model = model;
  }

  @Override
  public void run() {
    try {
      book.displayBooks(model);
      for (int i = model.getRowCount() - 1; i >= 0; i--) {
        if (((String) model.getValueAt(i, 5)).equals("Unavailable")) {
          model.removeRow(i);
        } // end of if block
      }
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}