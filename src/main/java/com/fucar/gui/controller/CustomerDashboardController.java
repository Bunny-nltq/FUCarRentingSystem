package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CustomerDashboardController {

    private MainApp mainApp;

    @FXML
    private Button btnBrowseCars, btnMyRentals, btnMyReviews;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnBrowseCars.setOnAction(e -> mainApp.showCarManagement());
        btnMyRentals.setOnAction(e -> mainApp.showCarRentalManagement());
        btnMyReviews.setOnAction(e -> mainApp.showReviewManagement());
    }
}
