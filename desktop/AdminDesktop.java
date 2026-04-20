import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Admin Desktop Application – Student Information System
 * Compile : javac -cp mysql-connector-j-8.x.x.jar AdminDesktop.java
 * Run     : java  -cp .;mysql-connector-j-8.x.x.jar AdminDesktop   (Windows)
 *           java  -cp .:mysql-connector-j-8.x.x.jar AdminDesktop   (Linux/Mac)
 */
public class AdminDesktop extends JFrame {

    // ── DB Config ──────────────────────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:postgresql://localhost:5432/sis_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";

    // ── UI Components ──────────────────────────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField tfRoll, tfName, tfEmail, tfBranch, tfSemester, tfPassword;
    private JLabel statusLabel;

    public AdminDesktop() {
        setTitle("SIS Admin Desktop");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Login screen first ─────────────────────────────────────────────────
        if (!showLoginDialog()) {
            JOptionPane.showMessageDialog(null, "Authentication failed. Exiting.");
            System.exit(0);
        }

        buildUI();
        loadStudents();
        setVisible(true);
    }

    // ── Login Dialog ───────────────────────────────────────────────────────────
    private boolean showLoginDialog() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] fields = {"Username:", userField, "Password:", passField};

        int opt = JOptionPane.showConfirmDialog(null, fields,
                "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return false;

        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id FROM admins WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage());
            return false;
        }
    }

    // ── Build UI ───────────────────────────────────────────────────────────────
    private void buildUI() {
        setLayout(new BorderLayout(8, 8));

        // Table
        String[] cols = {"ID", "Roll No", "Name", "Email", "Branch", "Semester"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFormFromTable();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        tfRoll     = addFormRow(formPanel, gbc, "Roll No",   0);
        tfName     = addFormRow(formPanel, gbc, "Name",      1);
        tfEmail    = addFormRow(formPanel, gbc, "Email",     2);
        tfPassword = addFormRow(formPanel, gbc, "Password",  3);
        tfBranch   = addFormRow(formPanel, gbc, "Branch",    4);
        tfSemester = addFormRow(formPanel, gbc, "Semester",  5);

        // Buttons
        JButton btnAdd    = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear  = new JButton("Clear");
        JButton btnRefresh= new JButton("Refresh");

        btnAdd.setBackground(new Color(67, 160, 71));    btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(25, 118, 210)); btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(229, 57, 53));  btnDelete.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadStudents());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear); btnPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.EAST);

        // Status bar
        statusLabel = new JLabel(" Ready");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JTextField addFormRow(JPanel panel, GridBagConstraints gbc, String label, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel(label + ":"), gbc);
        JTextField tf = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(tf, gbc);
        return tf;
    }

    // ── DB Operations ──────────────────────────────────────────────────────────
    private Connection getConnection() throws SQLException {
        try { Class.forName("org.postgresql.Driver"); }
        catch (ClassNotFoundException e) { throw new SQLException("PostgreSQL Driver missing"); }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("roll_no"), rs.getString("name"),
                    rs.getString("email"), rs.getString("branch"), rs.getInt("semester")
                });
            }
            setStatus("Loaded " + tableModel.getRowCount() + " students.");
        } catch (SQLException e) { setStatus("Error: " + e.getMessage()); }
    }

    private void addStudent() {
        if (!validateForm()) return;
        String sql = "INSERT INTO students (roll_no,name,email,password,branch,semester) VALUES(?,?,?,?,?,?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, -1);
            ps.executeUpdate();
            clearForm(); loadStudents(); setStatus("Student added.");
        } catch (SQLException e) { setStatus("Error: " + e.getMessage()); }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row < 0) { setStatus("Select a student first."); return; }
        if (!validateForm()) return;
        int id = (int) tableModel.getValueAt(row, 0);
        String sql = "UPDATE students SET roll_no=?,name=?,email=?,branch=?,semester=? WHERE id=?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfRoll.getText().trim());
            ps.setString(2, tfName.getText().trim());
            ps.setString(3, tfEmail.getText().trim());
            ps.setString(4, tfBranch.getText().trim());
            ps.setInt(5, Integer.parseInt(tfSemester.getText().trim()));
            ps.setInt(6, id);
            ps.executeUpdate();
            clearForm(); loadStudents(); setStatus("Student updated.");
        } catch (SQLException e) { setStatus("Error: " + e.getMessage()); }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) { setStatus("Select a student first."); return; }
        int id   = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 2);
        if (JOptionPane.showConfirmDialog(this,
                "Delete student \"" + name + "\"?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            clearForm(); loadStudents(); setStatus("Student deleted.");
        } catch (SQLException e) { setStatus("Error: " + e.getMessage()); }
    }

    private void fillStatement(PreparedStatement ps, int id) throws SQLException {
        ps.setString(1, tfRoll.getText().trim());
        ps.setString(2, tfName.getText().trim());
        ps.setString(3, tfEmail.getText().trim());
        ps.setString(4, tfPassword.getText().trim());
        ps.setString(5, tfBranch.getText().trim());
        ps.setInt(6, Integer.parseInt(tfSemester.getText().trim()));
    }

    private void populateFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        tfRoll.setText((String) tableModel.getValueAt(row, 1));
        tfName.setText((String) tableModel.getValueAt(row, 2));
        tfEmail.setText((String) tableModel.getValueAt(row, 3));
        tfBranch.setText((String) tableModel.getValueAt(row, 4));
        tfSemester.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        tfPassword.setText("");
    }

    private void clearForm() {
        tfRoll.setText(""); tfName.setText(""); tfEmail.setText("");
        tfPassword.setText(""); tfBranch.setText(""); tfSemester.setText("");
        table.clearSelection();
    }

    private boolean validateForm() {
        if (tfRoll.getText().isBlank() || tfName.getText().isBlank()
                || tfEmail.getText().isBlank() || tfBranch.getText().isBlank()
                || tfSemester.getText().isBlank()) {
            setStatus("All fields are required (except password for update).");
            return false;
        }
        try { Integer.parseInt(tfSemester.getText().trim()); }
        catch (NumberFormatException e) { setStatus("Semester must be a number."); return false; }
        return true;
    }

    private void setStatus(String msg) {
        statusLabel.setText(" " + msg);
    }

    // ── Entry Point ────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDesktop::new);
    }
}
