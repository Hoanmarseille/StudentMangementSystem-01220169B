package com.system.ui;

import com.system.service.StudentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainController {
    @FXML private BorderPane mainContainer;
    private StudentService studentService;

    // This is called by Main.java to pass the service down
    public void setStudentService(StudentService service) {
        this.studentService = service;
        showDashboard(); // Initial view
    }

    @FXML
    private void showDashboard() {
        loadView("/com/system/ui/DashboardView.fxml");
    }

    @FXML
    private void showStudentList() {
        loadView("/com/system/ui/StudentsView.fxml");
    }

    @FXML
    private void showDataTools() {
        loadView("/com/system/ui/DataToolsView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            // Pass the service to the sub-controller dynamically
            // ... inside loadView after loader.load()
            Object controller = loader.getController();

            if (controller instanceof StudentController) {
                ((StudentController) controller).setStudentService(studentService);
            } else if (controller instanceof DashboardController) {
                ((DashboardController) controller).setStudentService(studentService);
            } else if (controller instanceof DataToolsController) {
                ((DataToolsController) controller).setStudentService(studentService);
            } else if (controller instanceof SettingsController) {
                ((SettingsController) controller).setStudentService(studentService);
            } else if (controller instanceof ReportController) {
                ((ReportController) controller).setStudentService(studentService);
            }
            mainContainer.setCenter(view);
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
    // Inside MainController.java
    @FXML // This is the 'glue' that connects FXML to Java
    private void showSettings() {
        System.out.println(">>> DEBUG: Settings Clicked!");
        loadView("/com/system/ui/SettingView.fxml");
    }

    public void showAcademicReport() {
        loadView("/com/system/ui/AcademicReport.fxml"); // Ensure this path is 100% correct
    }
}
// Inside MainController.java
