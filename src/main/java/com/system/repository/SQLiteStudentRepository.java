package com.system.repository;

import com.system.domain.Student;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentRepository implements StudentRepository {
    // Database stored in a project-controlled 'data' folder [cite: 72, 149]
    private final String dbUrl = "jdbc:sqlite:data/students.db";

    public void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "student_id TEXT PRIMARY KEY, " +
                "full_name TEXT NOT NULL, " +
                "programme TEXT, " +
                "level INTEGER, " +
                "gpa REAL, " +
                "email TEXT, " +
                "phone_number TEXT, " + // Match the variable name
                "status TEXT, " +
                "date_added TEXT)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database schema successfully synced with Student domain.");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    @Override
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (student_id, full_name, programme, level, gpa, email, phone_number, status, date_added) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getProgramme());
            pstmt.setInt(4, student.getLevel());
            pstmt.setDouble(5, student.getGpa());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhoneNumber()); // Match Student.java
            pstmt.setString(8, student.getStatus());
            // Handle null dateAdded if necessary
            pstmt.setString(9, student.getDateAdded() != null ? student.getDateAdded().toString() : LocalDateTime.now().toString());

            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }

    @Override
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET full_name = ?, programme = ?, level = ?, gpa = ?, email = ?, phone = ?, status = ? WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getProgramme());
            pstmt.setInt(3, student.getLevel());
            pstmt.setDouble(4, student.getGpa());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhoneNumber());
            pstmt.setString(7, student.getStatus());
            pstmt.setString(8, student.getStudentId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteStudent(String studentId) throws SQLException {
        // Requirement: Delete a student record [cite: 44]
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Student> searchStudents(String query) throws SQLException {
        // Requirement: Search by ID or Full Name [cite: 45]
        List<Student> results = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_id LIKE ? OR full_name LIKE ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToStudent(rs));
                }
            }
        }
        return results;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("student_id"),
                rs.getString("full_name"),
                rs.getString("programme"),
                rs.getInt("level"),
                rs.getDouble("gpa"),
                rs.getString("email"),
                rs.getString("phone_number"), // Changed from "phone" to match Student.java logic
                rs.getString("status"),
                LocalDateTime.parse(rs.getString("date_added"))
        );
    }}