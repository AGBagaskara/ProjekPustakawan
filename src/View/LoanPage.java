package View;

import Controller.ControllerLoan;
import Model.Book.ModelBook;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Halaman Peminjaman & Pengembalian Buku.
 * Layout: tabel di kiri, form di kanan.
 */
public class LoanPage extends JFrame {

    Integer        selectedRow;
    ControllerLoan controller;

    // ===================== Tabel =====================
    JTable            table;
    DefaultTableModel tableModel;
    JScrollPane       scrollPane;
    String[]          namaKolom = {
        "ID", "Peminjam", "Judul Buku", "Tgl Pinjam", "Jatuh Tempo", "Tgl Kembali", "Status"
    };

    // ===================== Form =====================
    JLabel     labelBorrower = new JLabel("Nama Peminjam");
    JLabel     labelBook     = new JLabel("Pilih Buku (tersedia)");
    JLabel     labelDue      = new JLabel("Jatuh Tempo (yyyy-mm-dd)");
    JLabel     statusLabel   = new JLabel(" ");

    JTextField inputBorrower = new JTextField();
    JComboBox<String> comboBook = new JComboBox<>();
    JTextField inputDueDate  = new JTextField(LocalDate.now().plusDays(7).toString());

    JButton tombolPinjam    = new JButton("Pinjam");
    JButton tombolKembali   = new JButton("Kembalikan Buku");
    JButton tombolDelete    = new JButton("Hapus Record");
    JButton tombolClear     = new JButton("Clear");
    JButton tombolBuku      = new JButton("⇄  Ke Halaman Buku");

    // ID buku yang dipilih di combo box (disimpan terpisah)
    private java.util.Map<String, Integer> bookIdMap = new java.util.LinkedHashMap<>();

    // Layout constants
    private static final int FX = 680, FW = 210, IH = 28, LH = 20, BH = 30;

    public LoanPage() {
        tableModel = new DefaultTableModel(namaKolom, 0);
        table      = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        setTitle("Library Management — Peminjaman & Pengembalian");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setSize(940, 580);

        // ---- Tabel ----
        add(scrollPane);
        scrollPane.setBounds(10, 10, 650, 530);

        // ---- Form ----
        int y = 10;
        add(labelBorrower); labelBorrower.setBounds(FX, y, FW, LH);   y += LH;
        add(inputBorrower); inputBorrower.setBounds(FX, y, FW, IH);   y += IH + 8;
        add(labelBook);     labelBook.setBounds(FX, y, FW, LH);       y += LH;
        add(comboBook);     comboBook.setBounds(FX, y, FW, IH);       y += IH + 8;
        add(labelDue);      labelDue.setBounds(FX, y, FW, LH);        y += LH;
        add(inputDueDate);  inputDueDate.setBounds(FX, y, FW, IH);    y += IH + 20;

        add(tombolPinjam);  tombolPinjam.setBounds(FX, y, FW, BH);   y += BH + 4;
        add(tombolKembali); tombolKembali.setBounds(FX, y, FW, BH);  y += BH + 4;
        add(tombolDelete);  tombolDelete.setBounds(FX, y, FW, BH);   y += BH + 4;
        add(tombolClear);   tombolClear.setBounds(FX, y, FW, BH);    y += BH + 20;

        add(statusLabel); statusLabel.setBounds(FX, y, FW, LH);
        statusLabel.setForeground(Color.GRAY);
        y += LH + 4;

        // Warna tombol peminjaman & pengembalian
        tombolPinjam.setBackground(new Color(46, 160, 67));
        tombolPinjam.setForeground(Color.WHITE);
        tombolKembali.setBackground(new Color(220, 150, 30));
        tombolKembali.setForeground(Color.WHITE);
        tombolDelete.setBackground(new Color(200, 60, 60));
        tombolDelete.setForeground(Color.WHITE);

        add(tombolBuku); tombolBuku.setBounds(FX, y, FW, BH + 6);
        tombolBuku.setBackground(new Color(52, 120, 200));
        tombolBuku.setForeground(Color.WHITE);

        // ===================== Controller =====================
        controller = new ControllerLoan(this);
        controller.showAllLoans();
        controller.loadAvailableBooks();

        // ===================== Event Handling =====================

        // Klik baris → simpan index
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
            }
        });

        tombolPinjam.addActionListener(e -> controller.insertLoan());

        tombolKembali.addActionListener(e -> {
            if (selectedRow != null) controller.returnBook(selectedRow);
            else JOptionPane.showMessageDialog(null, "Pilih record peminjaman terlebih dahulu.");
        });

        tombolDelete.addActionListener(e -> {
            if (selectedRow != null) {
                controller.deleteLoan(selectedRow);
                selectedRow = null;
            } else {
                JOptionPane.showMessageDialog(null, "Pilih record yang ingin dihapus.");
            }
        });

        tombolClear.addActionListener(e -> clearForm());

        tombolBuku.addActionListener(e -> {
            dispose();
            new BookPage();
        });
    }

    // ===================== Getters untuk Controller =====================
    public JTable  getTableLoan()      { return table; }
    public String  getInputBorrower()  { return inputBorrower.getText().trim(); }
    public String  getInputDueDate()   { return inputDueDate.getText().trim(); }

    /**
     * Mengambil ID buku yang dipilih dari combo box.
     * Mengembalikan null jika combo kosong.
     */
    public Integer getSelectedBookId() {
        String selected = (String) comboBook.getSelectedItem();
        if (selected == null) return null;
        return bookIdMap.get(selected);
    }

    /**
     * Mengisi combo box dengan daftar buku yang tersedia.
     * Dipanggil oleh controller setelah data selesai dimuat dari DB.
     */
    public void populateBookCombo(List<ModelBook> books) {
        comboBook.removeAllItems();
        bookIdMap.clear();
        for (ModelBook b : books) {
            String label = "[" + b.getId() + "] " + b.getTitle() + " (stok: " + b.getStock() + ")";
            comboBook.addItem(label);
            bookIdMap.put(label, b.getId());
        }
    }

    public void clearForm() {
        inputBorrower.setText("");
        inputDueDate.setText(LocalDate.now().plusDays(7).toString());
        comboBook.setSelectedIndex(comboBook.getItemCount() > 0 ? 0 : -1);
        selectedRow = null;
        table.clearSelection();
    }

    public void setLoading(boolean loading) {
        statusLabel.setText(loading ? "Memuat data..." : " ");
    }
}
