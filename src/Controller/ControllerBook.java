package Controller;

import Model.Book.DAOBook;
import Model.Book.InterfaceDAOBook;
import Model.Book.ModelBook;
import Model.Book.ModelTableBook;
import View.BookPage;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Controller untuk halaman manajemen buku.
 * Menghubungkan BookPage (View) dengan DAOBook (Model).
 *
 * MULTITHREADING:
 * Setiap pemanggilan ke database dijalankan di background thread
 * menggunakan Thread baru, supaya GUI tidak freeze saat query berlangsung.
 * Setelah data siap, pembaruan tabel dikembalikan ke EDT (Event Dispatch Thread)
 * menggunakan SwingUtilities.invokeLater() — ini adalah praktik yang benar untuk Swing.
 */
public class ControllerBook {

    private BookPage          view;
    private InterfaceDAOBook  daoBook;

    public ControllerBook(BookPage view) {
        this.view    = view;
        this.daoBook = new DAOBook();
    }

    // ==================== SHOW ALL ====================
    /**
     * Memuat semua data buku dari DB di background thread,
     * lalu memperbarui tabel di EDT.
     */
    public void showAllBooks() {
        view.setLoading(true);
        new Thread(() -> {
            List<ModelBook> list = daoBook.getAll();
            SwingUtilities.invokeLater(() -> {
                view.getTableBook().setModel(new ModelTableBook(list));
                view.setLoading(false);
            });
        }).start();
    }

    /**
     * Mencari buku berdasarkan keyword di background thread.
     */
    public void searchBooks(String keyword) {
        view.setLoading(true);
        new Thread(() -> {
            List<ModelBook> list = daoBook.searchByTitle(keyword);
            SwingUtilities.invokeLater(() -> {
                view.getTableBook().setModel(new ModelTableBook(list));
                view.setLoading(false);
            });
        }).start();
    }

    // ==================== INSERT ====================
    public void insertBook() {
        try {
            String title  = view.getInputTitle();
            String author = view.getInputAuthor();
            String isbn   = view.getInputIsbn();
            String stockStr = view.getInputStock();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
                throw new Exception("Semua kolom wajib diisi!");
            }

            int stock = Integer.parseInt(stockStr);
            if (stock < 1) throw new Exception("Stok minimal 1.");

            ModelBook book = new ModelBook();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setStock(stock);

            new Thread(() -> {
                daoBook.insert(book);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Buku berhasil ditambahkan.");
                    view.clearForm();
                    showAllBooks();
                });
            }).start();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Stok harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // ==================== UPDATE ====================
    public void updateBook(int id) {
        try {
            String title  = view.getInputTitle();
            String author = view.getInputAuthor();
            String isbn   = view.getInputIsbn();
            String stockStr = view.getInputStock();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
                throw new Exception("Semua kolom wajib diisi!");
            }

            int stock = Integer.parseInt(stockStr);
            if (stock < 0) throw new Exception("Stok tidak boleh negatif.");

            ModelBook book = new ModelBook();
            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setStock(stock);

            new Thread(() -> {
                daoBook.update(book);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Data buku berhasil diubah.");
                    view.clearForm();
                    showAllBooks();
                });
            }).start();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Stok harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // ==================== DELETE ====================
    public void deleteBook(int baris) {
        Integer id    = (int) view.getTableBook().getValueAt(baris, 0);
        String  title = view.getTableBook().getValueAt(baris, 1).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
                null,
                "Hapus buku \"" + title + "\"?\n(Pastikan buku tidak sedang dipinjam)",
                "Hapus Buku",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                daoBook.delete(id);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Buku berhasil dihapus.");
                    view.clearForm();
                    showAllBooks();
                });
            }).start();
        }
    }
}
