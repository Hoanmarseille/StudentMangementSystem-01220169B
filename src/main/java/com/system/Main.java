package com.system;

import com.system.repository.SQLiteStudentRepository;
import com.system.service.StudentService;
import com.system.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Setup the Backend (Repository and Service)
        SQLiteStudentRepository repository = new SQLiteStudentRepository();

        // Ensure the 'data' folder and 'students' table exist before loading UI
        repository.initializeDatabase();

        StudentService service = new StudentService(repository);

        // 2. Load the Main Layout (The one with the Sidebar and BorderPane)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/ui/MainLayout.fxml"));
        Parent root = loader.load();

        // 3. Setup the Main Controller
        // This controller will handle swapping between Dashboard and Student views
        MainController mainController = loader.getController();
        mainController.setStudentService(service);

        // 4. Configure and Show the Primary Stage
        primaryStage.setTitle("Student Management System Plus");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}