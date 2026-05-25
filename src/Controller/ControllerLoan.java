package Controller;

import Model.Book.DAOBook;
import Model.Book.ModelBook;
import Model.Loan.DAOLoan;
import Model.Loan.InterfaceDAOLoan;
import Model.Loan.ModelLoan;
import Model.Loan.ModelTableLoan;
import View.LoanPage;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Controller untuk halaman peminjaman & pengembalian.
 *
 * MULTITHREADING:
 * Sama seperti ControllerBook — semua query DB di Thread terpisah,
 * update UI lewat SwingUtilities.invokeLater().
 */
public class ControllerLoan {

    private LoanPage         view;
    private InterfaceDAOLoan daoLoan;
    private DAOBook          daoBook;

    public ControllerLoan(LoanPage view) {
        this.view    = view;
        this.daoLoan = new DAOLoan();
        this.daoBook = new DAOBook();
    }

    // ==================== SHOW ALL ====================
    public void showAllLoans() {
        view.setLoading(true);
        new Thread(() -> {
            List<ModelLoan> list = daoLoan.getAll();
            SwingUtilities.invokeLater(() -> {
                view.getTableLoan().setModel(new ModelTableLoan(list));
                view.setLoading(false);
            });
        }).start();
    }

    /**
     * Mengisi combo box buku dengan daftar buku yang stoknya tersedia.
     */
    public void loadAvailableBooks() {
        new Thread(() -> {
            List<ModelBook> books = daoBook.getAvailableBooks();
            SwingUtilities.invokeLater(() -> view.populateBookCombo(books));
        }).start();
    }

    // ==================== INSERT (Pinjam) ====================
    public void insertLoan() {
        try {
            String borrower = view.getInputBorrower();
            Integer bookId  = view.getSelectedBookId();
            String dueDateStr = view.getInputDueDate();

            if (borrower.isEmpty()) throw new Exception("Nama peminjam wajib diisi!");
            if (bookId == null)     throw new Exception("Pilih buku terlebih dahulu!");
            if (dueDateStr.isEmpty()) throw new Exception("Tanggal jatuh tempo wajib diisi! (yyyy-mm-dd)");

            LocalDate dueDate = LocalDate.parse(dueDateStr);
            if (dueDate.isBefore(LocalDate.now()) || dueDate.isEqual(LocalDate.now())) {
                throw new Exception("Tanggal jatuh tempo harus setelah hari ini!");
            }

            ModelLoan loan = new ModelLoan();
            loan.setBorrowerName(borrower);
            loan.setBookId(bookId);
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(dueDate);
            loan.setStatus("DIPINJAM");

            new Thread(() -> {
                daoLoan.insert(loan);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Peminjaman berhasil dicatat.");
                    view.clearForm();
                    showAllLoans();
                    loadAvailableBooks();
                });
            }).start();

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Error: Format tanggal salah. Gunakan format yyyy-mm-dd");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // ==================== RETURN (Kembalikan) ====================
    public void returnBook(int baris) {
        Integer id       = (int) view.getTableLoan().getValueAt(baris, 0);
        String  peminjam = view.getTableLoan().getValueAt(baris, 1).toString();
        String  status   = view.getTableLoan().getValueAt(baris, 6).toString();

        if ("DIKEMBALIKAN".equals(status)) {
            JOptionPane.showMessageDialog(null, "Buku ini sudah dikembalikan sebelumnya.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                null,
                "Konfirmasi pengembalian buku oleh \"" + peminjam + "\"?",
                "Kembalikan Buku",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                daoLoan.returnBook(id);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Buku berhasil dikembalikan.");
                    showAllLoans();
                    loadAvailableBooks();
                });
            }).start();
        }
    }

    // ==================== DELETE ====================
    public void deleteLoan(int baris) {
        Integer id      = (int) view.getTableLoan().getValueAt(baris, 0);
        String peminjam = view.getTableLoan().getValueAt(baris, 1).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
                null,
                "Hapus record peminjaman oleh \"" + peminjam + "\"?",
                "Hapus Record",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                daoLoan.delete(id);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Record peminjaman dihapus.");
                    view.clearForm();
                    showAllLoans();
                    loadAvailableBooks();
                });
            }).start();
        }
    }
}
