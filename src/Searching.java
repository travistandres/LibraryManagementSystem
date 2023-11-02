//DW 11/2/2023
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.RowFilter;

public class Searching {
    //Referenced http://www.java2s.com/Tutorials/Java/Swing/JTable/Filter_a_JTable_with_RowFilter_in_Java.htm
    public void search(int category, String searchFor, DefaultTableModel model, JTable table){
        RowFilter<Object, Object> filter = new RowFilter<Object,Object>() {
            public boolean include(Entry entry) {
                String string = entry.getStringValue(category);
                return string.contains(searchFor);
            }
        };
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        sorter.setRowFilter(filter);
        table.setRowSorter(sorter);
    }
}
