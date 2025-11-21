package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CustomerDashboardController {

    private MainApp mainApp;
    private int loggedCustomerId;

    @FXML
    private Button btnBrowseCars, btnMyRentals, btnMyProfile;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // Gọi từ MainApp sau khi login thành công
    public void setLoggedCustomerId(int id) {
        this.loggedCustomerId = id;
    }

    @FXML
    private void initialize() {
        btnBrowseCars.setOnAction(e -> mainApp.showCarManagement());
        btnMyRentals.setOnAction(e -> mainApp.showCarRentalManagement());

        btnMyProfile.setOnAction(e -> {
            if (mainApp != null) {
                mainApp.showProfileManagement(loggedCustomerId);
            } else {
                System.err.println("MainApp is NULL! Bạn chưa setMainApp cho controller.");
            }
        });
    }
}
