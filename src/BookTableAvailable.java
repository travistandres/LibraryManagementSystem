import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class BookTableAvailable extends BookTableWorker {

  private final Object lock = new Object();
  private boolean dataReady = false;

  BookTableAvailable(DefaultTableModel model) {
    super(model);
  }

  @Override
  public void run() {
    try {

      synchronized (lock) {
        book.displayBooks(model);
        dataReady = true;
        lock.notifyAll();
      }

      synchronized (lock) {
        while (!dataReady) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        SwingUtilities.invokeLater(() -> {
          for (int i = model.getRowCount() - 1; i >= 0; i--) {
            int columnIndex = model.getColumnCount() - 1;
            if (((String) model.getValueAt(i, columnIndex)).equals("Unavailable")) {
              model.removeRow(i);
            }
          }
        });
      }

    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}