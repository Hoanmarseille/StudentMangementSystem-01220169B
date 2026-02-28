package com.system.ui;

import com.system.domain.Student;
import com.system.service.StudentService;
import com.system.util.CSVUtil;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import java.io.File;
import java.util.List;

public class DataToolsController {
    private StudentService studentService;

    public void setStudentService(StudentService service) {
        this.studentService = service;
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Student Data");
        fileChooser.setInitialFileName("students_export.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                List<Student> students = studentService.fetchAllStudents();
                CSVUtil.exportToCSV(students, file.getAbsolutePath()); // Call utility
                showAlert("Success", "Data exported successfully to " + file.getName());
            } catch (Exception e) {
                showAlert("Error", "Export failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Student CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Future Week 3 task: Implement CSVUtil.importFromCSV
                // For now, we print to console to verify the path
                System.out.println("Importing from: " + file.getAbsolutePath());
                showAlert("Import", "Import logic triggered for: " + file.getName());
            } catch (Exception e) {
                showAlert("Error", "Import failed: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}