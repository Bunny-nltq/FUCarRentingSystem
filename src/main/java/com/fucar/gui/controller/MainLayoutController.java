package com.fucar.gui.controller;

import com.fucar.MainApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML
    private StackPane contentArea;

    private MainApp mainApp;
    private int customerId;

    // ==========================
    // MAINAPP + CUSTOMER ID SETUP
    // ==========================
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setCustomerId(int id) {
        this.customerId = id;
        loadProfileContent(); // load default
    }

    // ==========================
    // LOAD FXML DÙNG CHUNG
    // ==========================
    private Parent loadScreen(String path) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        return loader.load();
    }

    private void setContent(Parent screen) {
        contentArea.getChildren().setAll(screen);
    }

    // ==========================
    // LOAD PROFILE
    // ==========================
    public void loadProfileContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Profile.fxml"));
            Parent screen = loader.load();

            ProfileController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setCustomerId(customerId);

            setContent(screen);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // LOAD REVIEW
    // ==========================
    private void loadReviewContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review.fxml"));
            Parent screen = loader.load();

            ReviewController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setCustomerId(customerId);

            setContent(screen);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // LOAD OTHER PAGES
    // ==========================
    private void loadSimplePage(String fxml) {
        try {
            Parent screen = loadScreen(fxml);
            setContent(screen);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // HANDLERS
    // ==========================
    @FXML private void handleProfileClick() { loadProfileContent(); }

    @FXML private void handleCarClick() { loadSimplePage("/fxml/CarList.fxml"); }

    @FXML private void handleRentalsClick() { loadSimplePage("/fxml/RentalHistory.fxml"); }

    @FXML private void handleReviewClick() { loadReviewContent(); }

    @FXML private void handleLogoutClick() { mainApp.showLogin(); }
}
