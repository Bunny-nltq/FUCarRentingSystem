package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminDashboardController {

    private MainApp mainApp;

    @FXML
    private Button btnCarManagement, btnCustomerManagement, btnRentalManagement, btnReviewManagement;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnCarManagement.setOnAction(e -> mainApp.showCarManagement());
        btnCustomerManagement.setOnAction(e -> mainApp.showCustomerManagement());
        btnRentalManagement.setOnAction(e -> mainApp.showCarRentalManagement());
        btnReviewManagement.setOnAction(e -> mainApp.showReviewManagement());
    }
}
