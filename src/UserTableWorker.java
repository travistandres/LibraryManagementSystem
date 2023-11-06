import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class UserTableWorker implements Runnable {

  User user = new User();
  DefaultTableModel model;

  UserTableWorker(DefaultTableModel model) {
    this.model = model;
  }

  public void run() {
    try {
      user.displayUsers(model);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}
