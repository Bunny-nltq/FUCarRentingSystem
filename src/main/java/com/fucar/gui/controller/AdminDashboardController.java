package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;  // ✅ Thêm dòng này

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminDashboardController {

    private MainApp mainApp;
    private Account loggedUser;  // Lưu Account đăng nhập

    @FXML
    private Button btnCarManagement, btnCustomerManagement, btnRentalManagement, btnReviewManagement;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setLoggedUser(Account loggedUser) {  // Setter cho MainApp gọi
        this.loggedUser = loggedUser;
    }

    @FXML
    private void initialize() {
        btnCarManagement.setOnAction(e -> mainApp.showCarManagement());
        btnCustomerManagement.setOnAction(e -> mainApp.showCustomerManagement());
        btnRentalManagement.setOnAction(e -> mainApp.showCarRentalManagement(loggedUser));
        btnReviewManagement.setOnAction(e -> mainApp.showReviewManagement());
    }
}
