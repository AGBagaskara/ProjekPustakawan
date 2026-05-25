package Model.Book;

import Model.LibraryItem;

/**
 * Model untuk data buku.
 * Extends LibraryItem → mewarisi id dan wajib implementasi getInfo().
 *
 * POLYMORPHISM: getInfo() mengembalikan ringkasan data buku.
 */
public class ModelBook extends LibraryItem {

    private String title;
    private String author;
    private String isbn;
    private int    stock;

    // ===================== Getters & Setters =====================

    public String getTitle()  { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn()   { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getStock()     { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Implementasi polymorphism dari LibraryItem.
     * Mengembalikan ringkasan data buku.
     */
    @Override
    public String getInfo() {
        return "Buku [" + getId() + "] \"" + title + "\" oleh " + author
                + " | ISBN: " + isbn + " | Stok: " + stock;
    }
}
