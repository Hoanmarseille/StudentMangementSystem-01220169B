package com.system.ui;

import com.system.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class SettingsController {
    @FXML private TextField gpaThresholdField;

    // This allows the Service to know what the 'Active' cutoff is
    public static double activeThreshold = 2.0;

    private StudentService studentService;

    public void setStudentService(StudentService service) {
        this.studentService = service;
        gpaThresholdField.setText(String.valueOf(activeThreshold));
    }

    @FXML
    private void handleSaveSettings() {
        try {
            activeThreshold = Double.parseDouble(gpaThresholdField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Status logic updated. Threshold: " + activeThreshold);
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Error feedback
            System.out.println("Invalid number format for GPA.");
        }
    }
}