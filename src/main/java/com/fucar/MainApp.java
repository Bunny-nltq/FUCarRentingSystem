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
        showLogin();  // màn hình đầu tiên
    }

    // ===================== LOGIN =====================
    public void showLogin() {
        loadScreenWithController("/fxml/Login.fxml", "Login", LoginController.class);
    }

    // ===================== REGISTER =====================
    public void showRegister() {
        loadScreenWithController("/fxml/Register.fxml", "Register", RegisterController.class);
    }

    // ===================== ADMIN DASHBOARD =====================
    public void showAdminDashboard() {
        loadScreenWithController("/fxml/AdminDashboard.fxml",
                "Admin Dashboard", AdminDashboardController.class);
    }

    // ===================== CUSTOMER DASHBOARD (CÓ CUSTOMER ID) =====================
    public void showCustomerDashboard(int customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerDashboard.fxml"));
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

    // ===================== CUSTOMER PROFILE =====================
    public void showProfileManagement(int customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Profile.fxml"));
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

    // ===================== OTHER MANAGEMENT SCREENS =====================
    public void showCarManagement() {
        loadScreenWithController("/fxml/CarManagement.fxml",
                "Car Management", CarManagementController.class);
    }

    public void showCustomerManagement() {
        loadScreenWithController("/fxml/CustomerManagement.fxml",
                "Customer Management", CustomerManagementController.class);
    }

    public void showCarRentalManagement() {
        loadScreenWithController("/fxml/CarRentalManagement.fxml",
                "Car Rental Management", CarRentalManagementController.class);
    }

    public void showReviewManagement() {
        loadScreenWithController("/fxml/Review.fxml",
                "Review Management", ReviewController.class);
    }

    // ===================== CHUNG: LOAD SCREEN CÓ CONTROLLER =====================
    private <T> void loadScreenWithController(String fxmlPath, String title, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            T controller = loader.getController();
            try {
                controllerClass
                        .getMethod("setMainApp", MainApp.class)
                        .invoke(controller, this);
            } catch (NoSuchMethodException ignored) {
                // controller không có setMainApp → bỏ qua
            }

            Scene scene = new Scene(root);
            addCss(scene, "/css/style.css");

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Lỗi load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // ===================== CSS =====================
    private void addCss(Scene scene, String cssPath) {
        try {
            String css = getClass().getResource(cssPath).toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("⚠ CSS không tồn tại: " + cssPath);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
