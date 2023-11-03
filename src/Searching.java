//DW 11/2/2023
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.RowFilter;

public class Searching {
    //Referenced http://www.java2s.com/Tutorials/Java/Swing/JTable/Filter_a_JTable_with_RowFilter_in_Java.htm
    //search looks through all columns of a table and matches the data in the row to the search text to see if it should be shown
    public void search(String searchFor, DefaultTableModel model, JTable table){
        
        RowFilter<Object, Object> filter = new RowFilter<Object,Object>() {
            public boolean include(Entry entry) {
                boolean hasMatch = false;
                for (int i = 0; i < model.getColumnCount(); i++){
                String string = entry.getStringValue(i).toLowerCase();
                    if (string.contains(searchFor.toLowerCase())){
                        hasMatch = true;
                    }
                }
            return hasMatch;
            }
        };
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        sorter.setRowFilter(filter);
        table.setRowSorter(sorter);
    }
}
