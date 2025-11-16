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
        showLogin(); // Mở màn hình Login mặc định
    }

    // ===================== LOGIN =====================
    public void showLogin() {
        loadScreenWithController("/fxml/Login.fxml", "Login", LoginController.class);
    }

    // ===================== REGISTER =====================
    public void showRegister() {
        loadScreenWithController("/fxml/Register.fxml", "Register", RegisterController.class);
    }

    // ===================== DASHBOARDS =====================
    public void showAdminDashboard() {
        loadScreen("/fxml/AdminDashboard.fxml", "Admin Dashboard");
    }

    public void showCustomerDashboard() {
        loadScreen("/fxml/CustomerDashboard.fxml", "Customer Dashboard");
    }

    // ===================== MANAGEMENT SCREENS =====================
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

    // ===================== HÀM CHUNG LOAD SCREEN =====================

    /**
     * Load screen có controller (Login, Register)
     */
    private <T> void loadScreenWithController(String fxmlPath, String title, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // truyền MainApp vào controller (nếu có method setMainApp)
            T controller = loader.getController();
            try {
                controllerClass.getMethod("setMainApp", MainApp.class).invoke(controller, this);
            } catch (NoSuchMethodException ignored) {
                // Controller không có setMainApp thì bỏ qua
            }

            Scene scene = new Scene(root);
            addCss(scene, "/css/style.css");

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Lỗi khi load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Load screen bình thường không cần controller
     */
    private void loadScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            addCss(scene, "/css/style.css");

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Lỗi khi load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Thêm CSS vào scene
     */
    private void addCss(Scene scene, String cssPath) {
        try {
            String css = getClass().getResource(cssPath).toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Không tìm thấy CSS: " + cssPath);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
