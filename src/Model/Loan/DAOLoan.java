package Model.Loan;

import Model.AbstractDAO;
import Model.Connector;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk operasi CRUD tabel `loan`.
 *
 * INHERITANCE : extends AbstractDAO<ModelLoan>
 * INTERFACE   : implements InterfaceDAOLoan
 */
public class DAOLoan extends AbstractDAO<ModelLoan> implements InterfaceDAOLoan {

    // ======================== INSERT ========================
    @Override
    public void insert(ModelLoan loan) {
        try {
            String query = "INSERT INTO loan (borrower_name, book_id, loan_date, due_date, status) "
                         + "VALUES (?, ?, ?, ?, 'DIPINJAM');";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setString(1, loan.getBorrowerName());
            ps.setInt(2, loan.getBookId());
            ps.setDate(3, Date.valueOf(loan.getLoanDate()));
            ps.setDate(4, Date.valueOf(loan.getDueDate()));
            ps.executeUpdate();
            ps.close();

            // Kurangi stok buku
            PreparedStatement ps2 = Connector.connect()
                    .prepareStatement("UPDATE book SET stock = stock - 1 WHERE id = ?;");
            ps2.setInt(1, loan.getBookId());
            ps2.executeUpdate();
            ps2.close();

            logInfo("INSERT", loan.getBorrowerName() + " meminjam book_id=" + loan.getBookId());
        } catch (SQLException e) {
            System.out.println("[DAO] Insert Loan Failed: " + e.getLocalizedMessage());
        }
    }

    // ======================== UPDATE ========================
    @Override
    public void update(ModelLoan loan) {
        try {
            String query = "UPDATE loan SET borrower_name=?, book_id=?, loan_date=?, "
                         + "due_date=?, status=? WHERE id=?;";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setString(1, loan.getBorrowerName());
            ps.setInt(2, loan.getBookId());
            ps.setDate(3, Date.valueOf(loan.getLoanDate()));
            ps.setDate(4, Date.valueOf(loan.getDueDate()));
            ps.setString(5, loan.getStatus());
            ps.setInt(6, loan.getId());
            ps.executeUpdate();
            ps.close();
            logInfo("UPDATE", loan.getInfo());
        } catch (SQLException e) {
            System.out.println("[DAO] Update Loan Failed: " + e.getLocalizedMessage());
        }
    }

    // ======================== DELETE ========================
    @Override
    public void delete(int id) {
        try {
            // Ambil dulu book_id dan status sebelum dihapus
            PreparedStatement psSel = Connector.connect()
                    .prepareStatement("SELECT book_id, status FROM loan WHERE id=?;");
            psSel.setInt(1, id);
            ResultSet rs = psSel.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                String status = rs.getString("status");
                // Kembalikan stok jika masih dipinjam
                if ("DIPINJAM".equals(status)) {
                    PreparedStatement psStock = Connector.connect()
                            .prepareStatement("UPDATE book SET stock = stock + 1 WHERE id=?;");
                    psStock.setInt(1, bookId);
                    psStock.executeUpdate();
                    psStock.close();
                }
            }
            psSel.close();

            PreparedStatement ps = Connector.connect()
                    .prepareStatement("DELETE FROM loan WHERE id=?;");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            logInfo("DELETE", "id=" + id);
        } catch (SQLException e) {
            System.out.println("[DAO] Delete Loan Failed: " + e.getLocalizedMessage());
        }
    }

    // ======================== GET ALL ========================
    @Override
    public List<ModelLoan> getAll() {
        List<ModelLoan> list = new ArrayList<>();
        try {
            String query = "SELECT l.*, b.title AS book_title FROM loan l "
                         + "JOIN book b ON l.book_id = b.id ORDER BY l.id DESC;";
            Statement st = Connector.connect().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("[DAO] GetAll Loan Failed: " + e.getLocalizedMessage());
        }
        return list;
    }

    // ===================== RETURN BOOK =====================
    @Override
    public void returnBook(int loanId) {
        try {
            // Update loan
            String query = "UPDATE loan SET return_date=?, status='DIKEMBALIKAN' WHERE id=?;";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, loanId);
            ps.executeUpdate();
            ps.close();

            // Kembalikan stok buku
            PreparedStatement psBook = Connector.connect()
                    .prepareStatement("UPDATE book SET stock = stock + 1 "
                            + "WHERE id = (SELECT book_id FROM loan WHERE id=?);");
            psBook.setInt(1, loanId);
            psBook.executeUpdate();
            psBook.close();

            logInfo("RETURN", "loan_id=" + loanId);
        } catch (SQLException e) {
            System.out.println("[DAO] Return Book Failed: " + e.getLocalizedMessage());
        }
    }

    // ================== GET ACTIVE LOAN ==================
    @Override
    public List<ModelLoan> getActiveLoan() {
        List<ModelLoan> list = new ArrayList<>();
        try {
            String query = "SELECT l.*, b.title AS book_title FROM loan l "
                         + "JOIN book b ON l.book_id = b.id "
                         + "WHERE l.status = 'DIPINJAM' ORDER BY l.due_date;";
            Statement st = Connector.connect().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("[DAO] GetActive Loan Failed: " + e.getLocalizedMessage());
        }
        return list;
    }

    // ===================== HELPER =====================
    private ModelLoan mapRow(ResultSet rs) throws SQLException {
        ModelLoan l = new ModelLoan();
        l.setId(rs.getInt("id"));
        l.setBorrowerName(rs.getString("borrower_name"));
        l.setBookId(rs.getInt("book_id"));
        l.setBookTitle(rs.getString("book_title"));
        l.setLoanDate(rs.getDate("loan_date").toLocalDate());
        l.setDueDate(rs.getDate("due_date").toLocalDate());
        Date rd = rs.getDate("return_date");
        l.setReturnDate(rd != null ? rd.toLocalDate() : null);
        l.setStatus(rs.getString("status"));
        return l;
    }
}
