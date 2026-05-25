package Model;

import java.util.List;

/**
 * Abstract class sebagai base untuk semua DAO.
 *
 * KONSEP ABSTRACT CLASS:
 * - Tidak bisa diinstansiasi langsung.
 * - Memaksa setiap DAO turunan untuk mengimplementasikan
 *   method insert, update, delete, dan getAll.
 * - Method logInfo() adalah concrete method (sudah ada implementasinya)
 *   yang bisa digunakan langsung oleh semua turunan.
 *
 * @param <T> Tipe model yang dikelola oleh DAO (Book, Loan, dsb.)
 */
public abstract class AbstractDAO<T> {

    /**
     * Menyimpan data baru ke database.
     */
    public abstract void insert(T model);

    /**
     * Mengupdate data yang sudah ada di database.
     */
    public abstract void update(T model);

    /**
     * Menghapus data berdasarkan ID.
     */
    public abstract void delete(int id);

    /**
     * Mengambil semua data dari database.
     */
    public abstract List<T> getAll();

    /**
     * Concrete method — mencetak log operasi ke console.
     * Diwarisi oleh semua DAO turunan tanpa perlu di-override.
     */
    public void logInfo(String operation, String detail) {
        System.out.println("[DAO] " + getClass().getSimpleName()
                + " | " + operation + " → " + detail);
    }
}
