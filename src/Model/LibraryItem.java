package Model;

/**
 * Abstract class sebagai base model untuk semua entitas di perpustakaan.
 *
 * KONSEP POLYMORPHISM:
 * - Method getInfo() dideklarasikan abstract di sini.
 * - Setiap subclass (Book, Loan) wajib mengimplementasikannya
 *   dengan versi masing-masing.
 * - Dengan begitu, satu variabel bertipe LibraryItem bisa
 *   memanggil getInfo() dan hasilnya berbeda tergantung objek aslinya.
 */
public abstract class LibraryItem {

    private Integer id;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    /**
     * Mengembalikan string ringkasan informasi dari objek ini.
     * Setiap subclass mengimplementasikan sesuai atributnya masing-masing.
     */
    public abstract String getInfo();
}
