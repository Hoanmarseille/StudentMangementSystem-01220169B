package com.system.ui;

import com.system.domain.Student;
import com.system.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.List;

public class ReportController {
    @FXML
    private TableView<Student> goodStandingTable;
    @FXML private TableView<Student> atRiskTable;

    private StudentService studentService;

    public void setStudentService(StudentService service) {
        this.studentService = service;
        refreshReport();
    }

    private void refreshReport() {
        try {
            var data = studentService.generateAcademicReportData();
            // Using .getOrDefault to prevent NullPointer if a category is empty
            goodStandingTable.getItems().setAll(data.getOrDefault("Good Standing", List.of()));
            atRiskTable.getItems().setAll(data.getOrDefault("At Risk", List.of()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportReport() {
        // Here you can reuse your CSVUtil logic to save this report to a .txt file
        System.out.println("Exporting report...");
    }
}