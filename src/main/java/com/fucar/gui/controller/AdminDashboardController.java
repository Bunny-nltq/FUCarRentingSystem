package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class AdminDashboardController {

    @FXML
    private StackPane contentArea;

    private MainApp mainApp;

    public void setMainApp(MainApp app) {
        this.mainApp = app;
    }

    /**
     * Load view vào contentArea và tự động setMainApp cho controller con.
     */
    private void loadView(String fxml) {
        try {
            System.out.println("🔄 Loading view: " + fxml);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent screen = loader.load();

            Object ctrl = loader.getController();
            System.out.println("✅ Loaded controller: " + ctrl.getClass().getSimpleName());

            // Inject MainApp nếu controller có hàm setMainApp(MainApp)
            try {
                ctrl.getClass().getMethod("setMainApp", MainApp.class)
                        .invoke(ctrl, mainApp);
            } catch (Exception ignored) {}

            contentArea.getChildren().setAll(screen);
            System.out.println("✅ View loaded successfully");

        } catch (Exception e) {
            System.err.println("❌ Error loading view: " + fxml);
            e.printStackTrace();
        }
    }

    // ====================== MENU BUTTON EVENTS =======================

    @FXML
    private void handleCarManagement() {
        loadView("/fxml/CarManagement.fxml");
    	
    }

    @FXML
    private void handleCustomerManagement() {
        loadView("/fxml/CustomerManagement.fxml");
    }

    @FXML
    private void handleRentalManagement() {
        loadView("/fxml/CarRentalManagement.fxml");
    }

    @FXML
    private void handleReviewManagement() {
        loadView("/fxml/ReviewManagement.fxml");
    }

    @FXML
    private void handleLogout() {
        if (mainApp != null) {
            mainApp.showLogin();
        }
    }
}