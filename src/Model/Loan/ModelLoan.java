package Model.Loan;

import Model.LibraryItem;
import java.time.LocalDate;

/**
 * Model untuk data peminjaman buku.
 * Extends LibraryItem → mewarisi id dan wajib implementasi getInfo().
 *
 * POLYMORPHISM: getInfo() mengembalikan ringkasan data peminjaman,
 *               berbeda dengan getInfo() di ModelBook.
 */
public class ModelLoan extends LibraryItem {

    private String    borrowerName;
    private int       bookId;
    private String    bookTitle;     // untuk tampilan (JOIN)
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;    // null jika belum dikembalikan
    private String    status;        // "DIPINJAM" / "DIKEMBALIKAN"

    // ===================== Getters & Setters =====================

    public String getBorrowerName()  { return borrowerName; }
    public void setBorrowerName(String n) { this.borrowerName = n; }

    public int getBookId()           { return bookId; }
    public void setBookId(int id)    { this.bookId = id; }

    public String getBookTitle()     { return bookTitle; }
    public void setBookTitle(String t) { this.bookTitle = t; }

    public LocalDate getLoanDate()   { return loanDate; }
    public void setLoanDate(LocalDate d) { this.loanDate = d; }

    public LocalDate getDueDate()    { return dueDate; }
    public void setDueDate(LocalDate d) { this.dueDate = d; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate d) { this.returnDate = d; }

    public String getStatus()        { return status; }
    public void setStatus(String s)  { this.status = s; }

    /**
     * Implementasi polymorphism dari LibraryItem.
     * Mengembalikan ringkasan data peminjaman.
     */
    @Override
    public String getInfo() {
        return "Pinjam [" + getId() + "] \"" + bookTitle + "\""
                + " oleh " + borrowerName
                + " | Pinjam: " + loanDate
                + " | Jatuh Tempo: " + dueDate
                + " | Status: " + status;
    }
}
