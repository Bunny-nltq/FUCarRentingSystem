package com.fucar;

import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.gui.controller.*;
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
        showLogin();
    }
    
   

    // ===================== LOGIN =====================
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setMainApp(this); // truyền MainApp

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("FU Car Renting System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== REGISTER =====================
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

    // ===================== DASHBOARDS =====================
    // Admin
    public void showAdminDashboard(Account loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoggedUser(loggedUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Admin Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Customer
    public void showCustomerDashboard(Account loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerDashboard.fxml"));
            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoggedUser(loggedUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Customer Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== MANAGEMENT SCREENS =====================
    public void showCarManagement() {
        loadScreen("/fxml/CarManagement.fxml", "Car Management");
    }

    public void showCustomerManagement() {
        loadScreen("/fxml/CustomerManagement.fxml", "Customer Management");
    }

 // Mở màn hình quản lý thuê xe, truyền thông tin người dùng
    public void showCarRentalManagement(Account loggedUser) {
        if (loggedUser == null) {
        	System.out.println("cumtomer null");
        	return; // tránh lỗi null
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CarRentalManagement.fxml"));
            Parent root = loader.load();

            // Lấy controller và thiết lập dữ liệu
            CarRentalManagementController controller = loader.getController();
            controller.setMainApp(this);            // truyền MainApp
//            controller.setAccountID(loggedUser.getAccountID()); // truyền accountID để load rentals
            controller.setLoggedUser(loggedUser);
            // Thiết lập Scene và Stylesheet
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Car Rental Management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mở màn hình quản lý Review
    public void showReviewManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Review Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    // ===================== HÀM CHUNG LOAD SCREEN =====================
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
