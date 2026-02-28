package com.system.ui;

import com.system.domain.Student;
import com.system.service.StudentService;
import com.system.util.CSVUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;




public class StudentController {
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colProgramme;
    @FXML private TableColumn<Student, Integer> colLevel;
    @FXML private TableColumn<Student, Double> colGpa;
    @FXML private TableColumn<Student, String> colStatus;
    // Table components
    @FXML private TableView<Student> studentTable;
    @FXML private TextField searchField;

    // Form components (matches the FXML above)
    @FXML private TextField idField, nameField, programmeField, gpaField;
    @FXML private ComboBox<Integer> levelBox;

    private StudentService studentService;
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        // These strings MUST match the variable names in your Student.java class exactly
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colProgramme.setCellValueFactory(new PropertyValueFactory<>("programme"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colGpa.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        levelBox.getItems().addAll(100, 200, 300, 400, 500, 600, 700);
    }




    public void setStudentService(StudentService service) {
        this.studentService = service;
        loadStudentData();
    }

    @FXML
    private void handleSave() {
        try {
            Student s = new Student();
            s.setStudentId(idField.getText());
            s.setFullName(nameField.getText());
            s.setProgramme(programmeField.getText());
            s.setLevel(levelBox.getValue() != null ? levelBox.getValue() : 0);
            s.setGpa(Double.parseDouble(gpaField.getText()));
            s.setStatus("Active");

            if (isEditMode) {
                studentService.updateStudent(s);
            } else {
                studentService.saveStudent(s);
            }

            clearFields();
            loadStudentData();
            showInfo("Success", "Student record saved successfully.");
        } catch (Exception e) {
            showError("Save Failed: " + e.getMessage());
        }


    }

    @FXML
    private void showEditDialog() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            isEditMode = true;
            idField.setText(selected.getStudentId());
            idField.setEditable(false); // ID is primary key [cite: 90]
            nameField.setText(selected.getFullName());
            programmeField.setText(selected.getProgramme());
            levelBox.setValue(selected.getLevel());
            gpaField.setText(String.valueOf(selected.getGpa()));
        } else {
            showError("Please select a student from the table first.");
        }
    }

    @FXML
    private void clearFields() {
        isEditMode = false;
        idField.clear();
        idField.setEditable(true);
        nameField.clear();
        programmeField.clear();
        gpaField.clear();
        levelBox.setValue(null);
    }

    public void loadStudentData() {
        try {
            // Fetch from Service -> Repository -> SQLite
            List<Student> students = studentService.fetchAllStudents();

            // Wrap the list in an ObservableList so JavaFX can "see" the changes
            ObservableList<Student> data = FXCollections.observableArrayList(students);
            studentTable.setItems(data);
            studentTable.refresh(); // Force a UI redraw
        } catch (Exception e) {
            showError("Could not refresh table: " + e.getMessage());
        }
        try {
            List<Student> students = studentService.fetchAllStudents();
            System.out.println("DEBUG: Found " + students.size() + " students in database.");

            if (!students.isEmpty()) {
                System.out.println("DEBUG: First student name is: " + students.get(0).getFullName());
            }

            studentTable.setItems(FXCollections.observableArrayList(students));
        } catch (Exception e) {
            System.out.println("DEBUG ERROR: " + e.getMessage());
            showError("Could not load data: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a student to delete.");
            return;
        }

        // Requirement 5.1.4: Delete with confirmation [cite: 44]
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Deleting Student: " + selected.getFullName());
        alert.setContentText("Are you sure you want to permanently delete this record?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    studentService.removeStudent(selected.getStudentId());
                    loadStudentData();
                    clearFields();
                    showInfo("Deleted", "Student record removed successfully.");
                } catch (Exception e) {
                    showError("Delete failed: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            loadStudentData();
            return;
        }

        try {
            // Requirement 5.1.5: Search by ID or name
            List<Student> results = studentService.search(query);
            studentTable.setItems(javafx.collections.FXCollections.observableArrayList(results));
        } catch (Exception e) {
            showError("Search failed: " + e.getMessage());
        }

    }

    // Helper methods for UI feedback [cite: 135]
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void showAddDialog() {
        // Instead of opening a window, we just reset the form on the same screen
        isEditMode = false;
        clearFields(); // Clears text fields so the user can add a fresh record
        idField.setEditable(true); // Ensure ID can be typed for new students [cite: 80]
        idField.requestFocus(); // Put the cursor in the ID field for a snappy experience
    }
    @FXML
    private void handleExport() {
        try {
            List<Student> students = studentService.fetchAllStudents();
            // You can use a FileChooser here for a professional touch
            CSVUtil.exportToCSV(students, "data/exported_students.csv");
            showInfo("Export Successful", "Data saved to data/exported_students.csv");
        } catch (Exception e) {
            showError("Export Failed: " + e.getMessage());
        }
    }

    // Add this inside your StudentController class

    // Existing loadStudentData and handleDelete methods...
}