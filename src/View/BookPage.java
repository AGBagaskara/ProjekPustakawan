package View;

import Controller.ControllerBook;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Halaman Manajemen Buku — CRUD buku perpustakaan.
 * Layout: tabel di kiri, form di kanan.
 */
public class BookPage extends JFrame {

    Integer        selectedId;
    Integer        selectedRow;
    ControllerBook controller;

    // ===================== Tabel =====================
    JTable             table;
    DefaultTableModel  tableModel;
    JScrollPane        scrollPane;
    String[]           namaKolom = {"ID", "Judul", "Penulis", "ISBN", "Stok"};

    // ===================== Form =====================
    JLabel      labelTitle  = new JLabel("Judul");
    JLabel      labelAuthor = new JLabel("Penulis");
    JLabel      labelIsbn   = new JLabel("ISBN");
    JLabel      labelStock  = new JLabel("Stok");
    JLabel      labelSearch = new JLabel("Cari Judul:");
    JLabel      statusLabel = new JLabel(" ");

    JTextField  inputTitle  = new JTextField();
    JTextField  inputAuthor = new JTextField();
    JTextField  inputIsbn   = new JTextField();
    JTextField  inputStock  = new JTextField();
    JTextField  inputSearch = new JTextField();

    JButton     tombolAdd    = new JButton("Add");
    JButton     tombolUpdate = new JButton("Update");
    JButton     tombolDelete = new JButton("Delete");
    JButton     tombolClear  = new JButton("Clear");
    JButton     tombolSearch = new JButton("Cari");
    JButton     tombolShowAll = new JButton("Tampilkan Semua");
    JButton     tombolPinjam = new JButton("⇄  Ke Halaman Peminjaman");

    // Layout constants
    private static final int FX = 660, FW = 220, IH = 28, LH = 20, BH = 30;

    public BookPage() {
        tableModel = new DefaultTableModel(namaKolom, 0);
        table      = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        setTitle("Library Management — Manajemen Buku");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setSize(930, 580);

        // ---- Tabel ----
        add(scrollPane);
        scrollPane.setBounds(10, 50, 630, 490);

        // ---- Search bar ----
        add(labelSearch);  labelSearch.setBounds(10, 14, 80, 24);
        add(inputSearch);  inputSearch.setBounds(90, 12, 340, 28);
        add(tombolSearch); tombolSearch.setBounds(435, 12, 90, 28);
        add(tombolShowAll); tombolShowAll.setBounds(530, 12, 110, 28);

        // ---- Form ----
        int y = 10;
        add(labelTitle);  labelTitle.setBounds(FX, y, FW, LH);  y += LH;
        add(inputTitle);  inputTitle.setBounds(FX, y, FW, IH);  y += IH + 8;
        add(labelAuthor); labelAuthor.setBounds(FX, y, FW, LH); y += LH;
        add(inputAuthor); inputAuthor.setBounds(FX, y, FW, IH); y += IH + 8;
        add(labelIsbn);   labelIsbn.setBounds(FX, y, FW, LH);   y += LH;
        add(inputIsbn);   inputIsbn.setBounds(FX, y, FW, IH);   y += IH + 8;
        add(labelStock);  labelStock.setBounds(FX, y, FW, LH);  y += LH;
        add(inputStock);  inputStock.setBounds(FX, y, FW, IH);  y += IH + 20;

        add(tombolAdd);    tombolAdd.setBounds(FX, y, FW, BH);    y += BH + 4;
        add(tombolUpdate); tombolUpdate.setBounds(FX, y, FW, BH); y += BH + 4;
        add(tombolDelete); tombolDelete.setBounds(FX, y, FW, BH); y += BH + 4;
        add(tombolClear);  tombolClear.setBounds(FX, y, FW, BH);  y += BH + 20;

        add(statusLabel); statusLabel.setBounds(FX, y, FW, LH); y += LH + 4;
        statusLabel.setForeground(Color.GRAY);

        add(tombolPinjam); tombolPinjam.setBounds(FX, y, FW, BH + 6);
        tombolPinjam.setBackground(new Color(52, 120, 200));
        tombolPinjam.setForeground(Color.WHITE);

        // ===================== Controller =====================
        controller = new ControllerBook(this);
        controller.showAllBooks();

        // ===================== Event Handling =====================

        // Klik baris → isi form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedId = (int) table.getValueAt(selectedRow, 0);
                    inputTitle.setText(table.getValueAt(selectedRow, 1).toString());
                    inputAuthor.setText(table.getValueAt(selectedRow, 2).toString());
                    inputIsbn.setText(table.getValueAt(selectedRow, 3).toString());
                    inputStock.setText(table.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        tombolAdd.addActionListener(e -> controller.insertBook());

        tombolUpdate.addActionListener(e -> {
            if (selectedId != null) controller.updateBook(selectedId);
            else JOptionPane.showMessageDialog(null, "Pilih buku yang ingin diupdate.");
        });

        tombolDelete.addActionListener(e -> {
            if (selectedRow != null) {
                controller.deleteBook(selectedRow);
                selectedRow = null; selectedId = null;
            } else {
                JOptionPane.showMessageDialog(null, "Pilih buku yang ingin dihapus.");
            }
        });

        tombolClear.addActionListener(e -> clearForm());

        tombolSearch.addActionListener(e -> {
            String keyword = inputSearch.getText().trim();
            if (!keyword.isEmpty()) controller.searchBooks(keyword);
        });

        tombolShowAll.addActionListener(e -> {
            inputSearch.setText("");
            controller.showAllBooks();
        });

        tombolPinjam.addActionListener(e -> {
            dispose();
            new LoanPage();
        });
    }

    // ===================== Getters untuk Controller =====================
    public JTable  getTableBook()    { return table; }
    public String  getInputTitle()   { return inputTitle.getText().trim(); }
    public String  getInputAuthor()  { return inputAuthor.getText().trim(); }
    public String  getInputIsbn()    { return inputIsbn.getText().trim(); }
    public String  getInputStock()   { return inputStock.getText().trim(); }

    public void clearForm() {
        inputTitle.setText(""); inputAuthor.setText("");
        inputIsbn.setText("");  inputStock.setText("");
        selectedId = null;      selectedRow = null;
        table.clearSelection();
    }

    /** Menampilkan teks loading di status label saat query berjalan di background. */
    public void setLoading(boolean loading) {
        statusLabel.setText(loading ? "Memuat data..." : " ");
    }
}
