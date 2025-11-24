package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminDashboardController {

    private MainApp mainApp;

    @FXML
    private Button btnCarManagement;

    @FXML
    private Button btnCustomerManagement;

    @FXML
    private Button btnRentalManagement;

    @FXML
    private Button btnReportManagement;

    @FXML
    private Button btnLogout;

    // MainApp được truyền từ MainApp khi load Dashboard
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        btnCarManagement.setOnAction(e -> {
            if (mainApp != null) mainApp.showCarManagement();
        });

        btnCustomerManagement.setOnAction(e -> {
            if (mainApp != null) mainApp.showCustomerManagement();
        });

        btnRentalManagement.setOnAction(e -> {
            if (mainApp != null) mainApp.showCarRentalManagement();
        });

        btnReportManagement.setOnAction(e -> {
            if (mainApp != null) mainApp.showReportDashboard();
        });

        btnLogout.setOnAction(e -> {
            if (mainApp != null) mainApp.showLogin();
        });
    }
}
