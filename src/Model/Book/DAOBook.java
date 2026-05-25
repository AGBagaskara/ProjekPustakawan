package Model.Book;

import Model.AbstractDAO;
import Model.Connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk operasi CRUD tabel `book`.
 *
 * INHERITANCE  : extends AbstractDAO<ModelBook>
 *                → wajib implementasi insert, update, delete, getAll.
 * INTERFACE    : implements InterfaceDAOBook
 *                → wajib implementasi searchByTitle, getAvailableBooks.
 */
public class DAOBook extends AbstractDAO<ModelBook> implements InterfaceDAOBook {

    // ======================== INSERT ========================
    @Override
    public void insert(ModelBook book) {
        try {
            String query = "INSERT INTO book (title, author, isbn, stock) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getStock());
            ps.executeUpdate();
            ps.close();
            logInfo("INSERT", book.getInfo());
        } catch (SQLException e) {
            System.out.println("[DAO] Insert Book Failed: " + e.getLocalizedMessage());
        }
    }

    // ======================== UPDATE ========================
    @Override
    public void update(ModelBook book) {
        try {
            String query = "UPDATE book SET title=?, author=?, isbn=?, stock=? WHERE id=?;";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getStock());
            ps.setInt(5, book.getId());
            ps.executeUpdate();
            ps.close();
            logInfo("UPDATE", book.getInfo());
        } catch (SQLException e) {
            System.out.println("[DAO] Update Book Failed: " + e.getLocalizedMessage());
        }
    }

    // ======================== DELETE ========================
    @Override
    public void delete(int id) {
        try {
            String query = "DELETE FROM book WHERE id=?;";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            logInfo("DELETE", "id=" + id);
        } catch (SQLException e) {
            System.out.println("[DAO] Delete Book Failed: " + e.getLocalizedMessage());
        }
    }

    // ======================== GET ALL ========================
    @Override
    public List<ModelBook> getAll() {
        List<ModelBook> list = new ArrayList<>();
        try {
            Statement st = Connector.connect().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM book ORDER BY id;");
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("[DAO] GetAll Book Failed: " + e.getLocalizedMessage());
        }
        return list;
    }

    // ===================== SEARCH BY TITLE =====================
    @Override
    public List<ModelBook> searchByTitle(String keyword) {
        List<ModelBook> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM book WHERE title LIKE ? ORDER BY title;";
            PreparedStatement ps = Connector.connect().prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println("[DAO] Search Book Failed: " + e.getLocalizedMessage());
        }
        return list;
    }

    // ================== GET AVAILABLE BOOKS ==================
    @Override
    public List<ModelBook> getAvailableBooks() {
        List<ModelBook> list = new ArrayList<>();
        try {
            Statement st = Connector.connect().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM book WHERE stock > 0 ORDER BY title;");
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("[DAO] GetAvailable Failed: " + e.getLocalizedMessage());
        }
        return list;
    }

    // ===================== HELPER =====================
    private ModelBook mapRow(ResultSet rs) throws SQLException {
        ModelBook b = new ModelBook();
        b.setId(rs.getInt("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setIsbn(rs.getString("isbn"));
        b.setStock(rs.getInt("stock"));
        return b;
    }
}
