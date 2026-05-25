package Model.Book;

import java.util.List;

/**
 * Interface khusus untuk DAO Buku.
 * Mendefinisikan kontrak operasi yang harus ada di DAOBook,
 * di luar operasi CRUD standar dari AbstractDAO.
 */
public interface InterfaceDAOBook {

    /**
     * Mencari buku berdasarkan judul (pencarian parsial).
     */
    List<ModelBook> searchByTitle(String keyword);

    /**
     * Mengambil daftar buku yang stoknya > 0 (tersedia untuk dipinjam).
     */
    List<ModelBook> getAvailableBooks();
    
    void insert(ModelBook book);
    void update(ModelBook book);
    void delete(int id);
    List<ModelBook> getAll();
}
