package Main;

import View.BookPage;
import javax.swing.SwingUtilities;

/**
 * Entry point aplikasi Library Management System.
 *
 * SwingUtilities.invokeLater() memastikan GUI dibuat di Event Dispatch Thread (EDT),
 * sesuai aturan thread-safety Swing.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookPage());
    }
}
