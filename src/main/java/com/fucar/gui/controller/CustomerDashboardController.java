package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class CustomerDashboardController {

    @FXML
    private StackPane contentArea;

    private MainApp mainApp;
    private int loggedCustomerId;

    // ===============================
    // SETUP
    // ===============================
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setLoggedCustomerId(int id) {
        this.loggedCustomerId = id;

        // ⭐ Load trang mặc định: PROFILE
        loadMyProfile();
    }

    // ===============================
    // LOAD VIEW DÙNG CHUNG
    // ===============================
    private void loadView(String fxml) {
        try {
            java.net.URL url = getClass().getResource(fxml);

            if (url == null) {
                System.err.println("❌ Không tìm thấy FXML: " + fxml);
                System.err.println("   Kiểm tra đường dẫn: src/main/resources" + fxml);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent screen = loader.load();

            Object ctrl = loader.getController();

            // inject MainApp
            try {
                ctrl.getClass().getMethod("setMainApp", MainApp.class)
                        .invoke(ctrl, mainApp);
            } catch (Exception ignored) {}

            // inject customerId
            try {
                ctrl.getClass().getMethod("setCustomerId", int.class)
                        .invoke(ctrl, loggedCustomerId);
            } catch (Exception ignored) {}

            // hiển thị giao diện
            contentArea.getChildren().setAll(screen);

        } catch (Exception e) {
            System.err.println("❌ Lỗi load FXML: " + fxml);
            e.printStackTrace();
        }
    }

    // ===============================
    // MENU HANDLERS
    // ===============================
    @FXML
    private void handleBrowseCars() {
        loadView("/fxml/CustomerBrowseCars.fxml");
    }

    @FXML
    private void handleMyRentals() {
        loadView("/fxml/CustomerRentals.fxml");
    }

    @FXML
    private void handleMyProfile() {
        loadMyProfile();
    }

    private void loadMyProfile() {
        loadView("/fxml/Profile.fxml");
    }

    @FXML
    private void handleReview() {
        loadView("/fxml/Review.fxml"); // ⭐ TẢI REVIEW
    }

    @FXML
    private void handleLogout() {
        if (mainApp != null)
            mainApp.showLogin();
    }
}
