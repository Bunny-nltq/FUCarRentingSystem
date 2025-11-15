package com.fucar;

import com.fucar.gui.controller.LoginController;
import com.fucar.gui.controller.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // Mở màn hình Login mặc định
        showLogin();
    }

    // ----------------- LOGIN -----------------
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            // Lấy controller và truyền MainApp
            LoginController controller = loader.getController();
            controller.setMainApp(this);
            
            

            Scene scene = new Scene(root);
            // Load CSS nếu có
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------- REGISTER -----------------
    public void showRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Register");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------- DASHBOARDS -----------------
    public void showAdminDashboard() {
        loadScreen("/fxml/AdminDashboard.fxml", "Admin Dashboard");
    }

    public void showCustomerDashboard() {
        loadScreen("/fxml/CustomerDashboard.fxml", "Customer Dashboard");
    }

    // ----------------- MANAGEMENT SCREENS -----------------
    public void showCarManagement() {
        loadScreen("/fxml/CarManagement.fxml", "Car Management");
    }

    public void showCustomerManagement() {
        loadScreen("/fxml/CustomerManagement.fxml", "Customer Management");
    }

    public void showCarRentalManagement() {
        loadScreen("/fxml/CarRentalManagement.fxml", "Car Rental Management");
    }

    public void showReviewManagement() {
        loadScreen("/fxml/Review.fxml", "Review Management");
    }

    // ----------------- HÀM CHUNG LOAD SCREEN -----------------
    private void loadScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
