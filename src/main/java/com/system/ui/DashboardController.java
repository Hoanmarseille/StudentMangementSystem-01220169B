package com.system.ui;

import com.system.domain.DashboardStats;
import com.system.domain.Student;
import com.system.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class DashboardController {
    @FXML private Label totalStudentsLabel;
    @FXML private Label avgGpaLabel;
    @FXML
    private Label activeLabel;

    private StudentService studentService;

    public void setStudentService(StudentService service) {
        this.studentService = service;
        refreshStats();
        this.studentService = service;
        updateDashboardStats(); //// Call refresh immediately when service is set
    }

    private void refreshStats() {
        try {
            // Use the service to get the pre-calculated stats
            DashboardStats stats = studentService.getDashboardStats();

            totalStudentsLabel.setText(String.valueOf(stats.getTotalStudents()));
            avgGpaLabel.setText(String.format("%.2f", stats.getAverageGpa()));
            activeLabel.setText(String.valueOf(stats.getActiveStudents()));

        } catch (Exception e) {
            e.printStackTrace();
            // Default to 0 if database fails
            totalStudentsLabel.setText("0");
            avgGpaLabel.setText("0.00");
            activeLabel.setText("0");
        }
    }

    @FXML
    private void handleReport() {
        System.out.println("Generating Week 3 Report...");
    }
    @FXML private Label inactiveCountLabel;

    private void updateDashboardStats() {
        try {
            List<Student> students = studentService.fetchAllStudents();
            double threshold = SettingsController.activeThreshold;

            // Requirement 5.2 logic: Filter based on GPA threshold
            long activeCount = students.stream().filter(s -> s.getGpa() >= threshold).count();
            long inactiveCount = students.stream().filter(s -> s.getGpa() < threshold).count();


            inactiveCountLabel.setText(String.valueOf(inactiveCount));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Inside DashboardController.java
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleGenerateReport() {
        if (mainController != null) {
            mainController.showAcademicReport(); // The Jump!
        }
    }
    // Add this setter

}