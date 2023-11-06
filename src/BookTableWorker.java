import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class BookTableWorker implements Runnable {

  Book book = new Book();
  DefaultTableModel model;

  BookTableWorker(DefaultTableModel model) {
    this.model = model;
  }

  @Override
  public void run() {
    try {
      book.displayBooks(model);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}
