package com.sis.dao;

import com.sis.model.Student;
import com.sis.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // ── Authentication ─────────────────────────────────────────────────────────

    public Student loginStudent(String rollNo, String password) throws SQLException {
        String sql = "SELECT * FROM students WHERE roll_no = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapStudent(rs);
            }
        }
        return null;
    }

    public boolean loginAdmin(String username, String password) throws SQLException {
        String sql = "SELECT id FROM admins WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        }
    }

    // ── Student CRUD ───────────────────────────────────────────────────────────

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapStudent(rs));
        }
        return list;
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapStudent(rs);
        }
        return null;
    }

    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students (roll_no, name, email, password, branch, semester) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPassword());
            ps.setString(5, s.getBranch());
            ps.setInt(6, s.getSemester());
            ps.executeUpdate();
        }
    }

    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET roll_no=?, name=?, email=?, branch=?, semester=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getBranch());
            ps.setInt(5, s.getSemester());
            ps.setInt(6, s.getId());
            ps.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ── Marks & Attendance ─────────────────────────────────────────────────────

    public List<String[]> getMarks(int studentId) throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT subject, marks_obtained, max_marks FROM marks WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("subject"),
                    rs.getString("marks_obtained"),
                    rs.getString("max_marks")
                });
            }
        }
        return list;
    }

    public List<String[]> getAttendance(int studentId) throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT subject, attended, total_classes FROM attendance WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int attended = rs.getInt("attended");
                int total    = rs.getInt("total_classes");
                String pct   = total > 0 ? String.format("%.1f%%", (attended * 100.0 / total)) : "N/A";
                list.add(new String[]{rs.getString("subject"), attended + "/" + total, pct});
            }
        }
        return list;
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setRollNo(rs.getString("roll_no"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setBranch(rs.getString("branch"));
        s.setSemester(rs.getInt("semester"));
        return s;
    }
}
