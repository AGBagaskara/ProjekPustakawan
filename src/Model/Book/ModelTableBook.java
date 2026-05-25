package Model.Book;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModelTableBook extends AbstractTableModel {

    private List<ModelBook> list;
    private final String[]  kolom = {"ID", "Judul", "Penulis", "ISBN", "Stok"};

    public ModelTableBook(List<ModelBook> list) {
        this.list = list;
    }

    @Override public int getRowCount()    { return list.size(); }
    @Override public int getColumnCount() { return kolom.length; }
    @Override public String getColumnName(int col) { return kolom[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ModelBook b = list.get(row);
        switch (col) {
            case 0: return b.getId();
            case 1: return b.getTitle();
            case 2: return b.getAuthor();
            case 3: return b.getIsbn();
            case 4: return b.getStock();
            default: return null;
        }
    }
}
