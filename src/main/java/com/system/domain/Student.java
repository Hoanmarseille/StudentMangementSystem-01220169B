package com.system.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Represents a Student record with full encapsulation.
 * Satisfies requirements for Student Fields.
 */
public class Student {
    private String studentId;
    private String fullName;
    private String programme;
    private int level;
    private double gpa;
    private String email;
    private String phoneNumber;
    private String status; // "Active" or "Inactive"
    private LocalDateTime dateAdded;

    // Default constructor for flexibility
    public Student() {
        this.dateAdded = LocalDateTime.now();
    }

    // Full constructor for loading from Database/CSV
    public Student(String studentId, String fullName, String programme, int level,
                   double gpa, String email, String phoneNumber, String status, LocalDateTime dateAdded) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.programme = programme;
        this.level = level;
        this.gpa = gpa;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.dateAdded = dateAdded;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getProgramme() { return programme; }
    public void setProgramme(String programme) { this.programme = programme; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }



    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }

    @Override
    public String toString() {
        return "Student{" + "id='" + studentId + "', name='" + fullName + "'}";
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
    }
    // Inside Student.java
    public String getStatus() {
        // Access the threshold we saved in SettingsController
        double threshold = com.system.ui.SettingsController.activeThreshold;

        if (this.gpa >= threshold) {
            return "Active";
        } else {
            return "Inactive"; // Or "At Risk"
        }
    }
    public void setStatus(String status) {
        this.status = status;
    }



}