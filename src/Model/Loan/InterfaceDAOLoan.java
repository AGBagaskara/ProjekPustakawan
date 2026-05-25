package Model.Loan;

import java.util.List;

/**
 * Interface khusus untuk DAO Peminjaman.
 */
public interface InterfaceDAOLoan {

    /**
     * Mengembalikan buku (mengupdate return_date dan status).
     */
    void returnBook(int loanId);

    /**
     * Mengambil daftar peminjaman yang belum dikembalikan.
     */
    List<ModelLoan> getActiveLoan();
    
    void insert(ModelLoan loan);
    void update(ModelLoan loan);
    void delete(int id);
    List<ModelLoan> getAll();
}
