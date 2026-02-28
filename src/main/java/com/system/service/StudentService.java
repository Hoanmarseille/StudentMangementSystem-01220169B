package com.system.service;

import com.system.domain.DashboardStats;
import com.system.domain.Student;
import com.system.repository.StudentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentService {

    // The repository interface allows for Clean Architecture [cite: 100]
    private final StudentRepository repository;

    // Constructor injection: This allows you to pass your SQLite implementation in
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    /**
     * Requirement 5.1: View all students
     * This resolves the 'Cannot resolve method fetchAllStudents' error.
     */
    public List<Student> fetchAllStudents() throws Exception {
        return repository.getAllStudents();
    }

    /**
     * Requirement 5.1: Add a new student with validation [cite: 74, 79]
     */
    public void saveStudent(Student student) throws Exception {
        // 1. Validation check
        List<String> errors = validateStudent(student);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        // 2. Threshold Logic: Set status based on the Settings threshold
        double threshold = com.system.ui.SettingsController.activeThreshold;
        if (student.getGpa() >= threshold) {
            student.setStatus("Active");
        } else {
            student.setStatus("Inactive");
        }

        // 3. Persist to Database
        repository.addStudent(student);
    }
    /**
     * Requirement 5.1: Update an existing student [cite: 43]
     */
    public void updateStudent(Student student) throws Exception {
        List<String> errors = validateStudent(student);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }
        repository.updateStudent(student);
    }

    /**
     * Requirement 5.1: Delete a student [cite: 44]
     */
    public void removeStudent(String studentId) throws Exception {
        repository.deleteStudent(studentId);
    }

    /**
     * Requirement 5.1: Search students [cite: 45]
     */
    public List<Student> search(String query) throws Exception {
        return repository.searchStudents(query);
    }

    /**
     * Requirement 5.3: Top Performers Report [cite: 63]
     */
    public List<Student> getTopPerformers(int limit) throws Exception {
        return repository.getAllStudents().stream()
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Validates a student object based on project requirements [cite: 80-87].
     */
    public List<String> validateStudent(Student student) {
        List<String> errors = new ArrayList<>();

        // 1. Student ID Validation: 4 to 20 chars, letters/digits only [cite: 80]
        if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
            errors.add("Student ID is required.");
        } else if (student.getStudentId().length() < 4 || student.getStudentId().length() > 20) {
            errors.add("Student ID must be between 4 and 20 characters.");
        } else if (!student.getStudentId().matches("^[a-zA-Z0-9]+$")) {
            errors.add("Student ID must contain only letters and digits.");
        }

        // 2. Full Name Validation: 2 to 60 chars, no digits [cite: 81]
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            errors.add("Full name is required.");
        } else if (student.getFullName().length() < 2 || student.getFullName().length() > 60) {
            errors.add("Full name must be between 2 and 60 characters.");
        } else if (student.getFullName().matches(".*\\d.*")) {
            errors.add("Full name must not contain digits.");
        }

        // 3. Programme Validation [cite: 82]
        if (student.getProgramme() == null || student.getProgramme().isEmpty()) {
            errors.add("Programme is required.");
        }

        // 4. Level Validation: must be specific values [cite: 83]
        List<Integer> validLevels = List.of(100, 200, 300, 400, 500, 600, 700);
        if (!validLevels.contains(student.getLevel())) {
            errors.add("Level must be one of: 100, 200, 300, 400, 500, 600, 700.");
        }

        // 5. GPA Validation: 0.0 to 4.0 [cite: 84]
        if (student.getGpa() < 0.0 || student.getGpa() > 4.0) {
            errors.add("GPA must be between 0.0 and 4.0.");
        }

        // 6. Email Validation: must have '@' and '.' [cite: 85]
        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            if (!student.getEmail().contains("@") || !student.getEmail().contains(".")) {
                errors.add("Email must contain an '@' sign and a dot.");
            }
        }

        // 7. Phone Number Validation: 10 to 15 digits only [cite: 86]
        if (student.getPhoneNumber() != null && !student.getPhoneNumber().isEmpty()) {
            if (!student.getPhoneNumber().matches("^\\d{10,15}$")) {
                errors.add("Phone number must be 10 to 15 digits (numbers only).");
            }
        }

        return errors;
    }

    // This method does the heavy lifting for the dashboard
    public DashboardStats getDashboardStats() throws Exception {
        List<Student> students = repository.getAllStudents();

        int total = students.size();
        double avg = students.stream().mapToDouble(Student::getGpa).average().orElse(0.0);
        int active = (int) students.stream()
                .filter(s -> "Active".equalsIgnoreCase(s.getStatus()))
                .count();

        return new DashboardStats(total, avg, active);
    }
    @FXML
    private Label inactiveCountLabel; // Link this to the FXML fx:id
    private StudentService studentService;
    private void updateDashboardStats() {
        try {
            List<Student> students = studentService.fetchAllStudents();
            double threshold = com.system.ui.SettingsController.activeThreshold;

            // Count those who fall below the threshold
            long activeCount = students.stream().filter(s -> s.getGpa() >= threshold).count();
            long inactiveCount = students.stream().filter(s -> s.getGpa() < threshold).count();


            inactiveCountLabel.setText(String.valueOf(inactiveCount)); // Update the new label

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Map<String, List<Student>> generateAcademicReportData() throws Exception {
        List<Student> allStudents = fetchAllStudents();
        double threshold = com.system.ui.SettingsController.activeThreshold;

        // Grouping students by their performance against your threshold
        return allStudents.stream().collect(java.util.stream.Collectors.groupingBy(s ->
                s.getGpa() >= threshold ? "Good Standing" : "At Risk"
        ));
    }

}
