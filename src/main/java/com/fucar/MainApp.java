package com.fucar;

import com.fucar.entity.Account;
import com.fucar.gui.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
	private Account loggedUser;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLogin();
    }
    
    
    public void setLoggedCustomerId(Account acc) {
        this.loggedUser = acc;
    }


    // ===============================
    // LOGIN
    // ===============================
    public void showLogin() {
        loadScreen("/fxml/Login.fxml", "Login",
                LoginController.class, "/css/style.css");
    }

    // ===============================
    // REGISTER
    // ===============================
    public void showRegister() {
        loadScreen("/fxml/Register.fxml", "Register",
                RegisterController.class, "/css/style.css");
    }

    // ===============================
    // ADMIN DASHBOARD
    // ===============================
    public void showAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/web.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Admin Dashboard");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // CUSTOMER DASHBOARD
    // ===============================
    public void showCustomerDashboard(Account loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerDashboard.fxml"));
            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoggedCustomer(loggedUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Customer Dashboard");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public void showCustomerRentals() {
        loadScreen("/fxml/CustomerRentals.fxml", "CustomerRentals",
                CustomerDashboardController.class, "/css/style.css");
    }
    
    

    // ===============================
    // PROFILE (customer)
    // ===============================
    public void showProfileManagement(int customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
            Parent root = loader.load();

            MainLayoutController controller = loader.getController();
            controller.setMainApp(this);
            controller.setCustomerId(customerId);
            controller.loadProfileContent();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("My Profile");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // MANAGEMENT (Admin)
    // ===============================
    public void showCustomerManagement() {
        loadScreen("/fxml/CustomerManagement.fxml", "Customer Management",
                CustomerManagementController.class, "/css/style.css");
    }

    public void showCarManagement() {
        loadScreen("/fxml/CarManagement.fxml", "Car Management",
                CarManagementController.class, "/css/style.css");
    }

    public void showCarRentalManagement() {
        loadScreen("/fxml/CarRentalManagement.fxml", "Car Rental Management",
                CarRentalManagementController.class, "/css/style.css");
    }

    public void showReviewManagement() {
        loadScreen("/fxml/Review.fxml", "Review Management",
                ReviewController.class, "/css/style.css");
    }

    public void showReportDashboard() {
        loadScreen("/fxml/ReportDashboard.fxml", "Rental Report Dashboard",
                ReportDashboardController.class, "/css/style.css");
    }

    // ==========================================================
    // GENERIC SCREEN LOADER
    // ==========================================================
    private <T> void loadScreen(String fxml, String title, Class<T> type, String css) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            T controller = loader.getController();
            try {
                type.getMethod("setMainApp", MainApp.class).invoke(controller, this);
            } catch (NoSuchMethodException ignored) {}

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(css).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
