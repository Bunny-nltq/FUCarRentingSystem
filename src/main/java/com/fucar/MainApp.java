package com.fucar;

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

    // =================================================================
    // LOGIN
    // =================================================================
    public void showLogin() {
        loadScreen("/fxml/Login.fxml", "Login", LoginController.class);
    }

    // =================================================================
    // REGISTER
    // =================================================================
    public void showRegister() {
        loadScreen("/fxml/Register.fxml", "Register", RegisterController.class);
    }

    // =================================================================
    // ADMIN DASHBOARD
    // =================================================================
    public void showAdminDashboard() {
        loadScreen("/fxml/AdminDashboard.fxml",
                "Admin Dashboard", AdminDashboardController.class);
    }

    // =================================================================
    // CUSTOMER DASHBOARD
    // =================================================================
    public void showCustomerDashboard(int customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/CustomerDashboard.fxml")
            );

            if (loader.getLocation() == null)
                throw new RuntimeException("Không tìm thấy CustomerDashboard.fxml");

            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoggedCustomerId(customerId);

            Scene scene = new Scene(root);
            addCss(scene, "/css/style.css");

            primaryStage.setTitle("Customer Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Lỗi load CustomerDashboard.fxml");
            e.printStackTrace();
        }
    }

    // =================================================================
    // PROFILE
    // =================================================================
    public void showProfileManagement(int customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Profile.fxml"));

            if (loader.getLocation() == null)
                throw new RuntimeException("Không tìm thấy Profile.fxml");

            Parent root = loader.load();

            ProfileController controller = loader.getController();
            controller.setMainApp(this);
            controller.setCustomerId(customerId);

            Scene scene = new Scene(root);
            addCss(scene, "/css/style.css");

            primaryStage.setTitle("My Profile");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Lỗi load Profile.fxml");
            e.printStackTrace();
        }
    }

    // =================================================================
    // MANAGEMENT SCREENS
    // =================================================================
    public void showCarManagement() {
        loadScreen("/fxml/CarManagement.fxml",
                "Car Management", CarManagementController.class);
    }

    public void showCustomerManagement() {
        loadScreen("/fxml/CustomerManagement.fxml",
                "Customer Management", CustomerManagementController.class);
    }

    public void showCarRentalManagement() {
        loadScreen("/fxml/CarRentalManagement.fxml",
                "Car Rental Management", CarRentalManagementController.class);
    }

    public void showReviewManagement() {
        loadScreen("/fxml/Review.fxml",
                "Review Management", ReviewController.class);
    }

    public void showReportDashboard() {
        loadScreen("/fxml/ReportDashboard.fxml",
                "Rental Report Dashboard", ReportDashboardController.class);
    }

    // =================================================================
    // GENERIC SCREEN LOADER (CHUẨN NHẤT)
    // =================================================================
    private <T> void loadScreen(String fxmlPath, String title, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null)
                throw new RuntimeException("Không tìm thấy file FXML: " + fxmlPath);

            Parent root = loader.load();

            T controller = loader.getController();

            // Gọi setMainApp nếu có
            try {
                controllerClass.getMethod("setMainApp", MainApp.class)
                        .invoke(controller, this);
            } catch (NoSuchMethodException ignored) {}

            Scene scene = new Scene(root);
            addCss(scene, "/css/style.css");

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // =================================================================
    // POPUP WINDOWS
    // =================================================================
    public void showAddCustomerForm(CustomerManagementController parentController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCustomer.fxml"));
            Parent view = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(view));

            // GỌI REFRESH khi popup đóng
            stage.setOnHiding(event -> {
                parentController.reloadAfterPopup();
            });

            stage.show();

        } catch (Exception e) {
            System.err.println("❌ Lỗi load AddCustomer.fxml");
            e.printStackTrace();
        }
    }



    public void showEditCustomerForm(Integer customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditCustomer.fxml"));

            if (loader.getLocation() == null)
                throw new RuntimeException("Không tìm thấy EditCustomer.fxml");

            Parent view = loader.load();

            EditCustomerController controller = loader.getController();
            controller.setCustomerId(customerId);

            Stage stage = new Stage();
            stage.setTitle("Edit Customer");
            stage.setScene(new Scene(view));
            stage.show();

        } catch (Exception e) {
            System.err.println("❌ Lỗi load EditCustomer.fxml");
            e.printStackTrace();
        }
    }

    // =================================================================
    // CSS
    // =================================================================
    private void addCss(Scene scene, String cssPath) {
        try {
            scene.getStylesheets().add(
                    getClass().getResource(cssPath).toExternalForm()
            );
        } catch (Exception e) {
            System.err.println("⚠ Không tìm thấy CSS: " + cssPath);
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
