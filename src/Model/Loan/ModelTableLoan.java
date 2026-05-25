package Model.Loan;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModelTableLoan extends AbstractTableModel {

    private List<ModelLoan> list;
    private final String[]  kolom = {
        "ID", "Peminjam", "Judul Buku", "Tgl Pinjam", "Jatuh Tempo", "Tgl Kembali", "Status"
    };

    public ModelTableLoan(List<ModelLoan> list) {
        this.list = list;
    }

    @Override public int getRowCount()    { return list.size(); }
    @Override public int getColumnCount() { return kolom.length; }
    @Override public String getColumnName(int col) { return kolom[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ModelLoan l = list.get(row);
        switch (col) {
            case 0: return l.getId();
            case 1: return l.getBorrowerName();
            case 2: return l.getBookTitle();
            case 3: return l.getLoanDate();
            case 4: return l.getDueDate();
            case 5: return l.getReturnDate() != null ? l.getReturnDate().toString() : "-";
            case 6: return l.getStatus();
            default: return null;
        }
    }
}
