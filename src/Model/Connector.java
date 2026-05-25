package Model;

import java.sql.*;

/**
 * Singleton-style connector ke database MySQL library_db.
 */
public class Connector {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME     = "library_db";
    private static final String URL         = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String USERNAME    = "root";
    private static final String PASSWORD    = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[DB] Connected to " + DB_NAME);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("[DB] Connection Failed: " + e.getLocalizedMessage());
        }
        return conn;
    }
}
