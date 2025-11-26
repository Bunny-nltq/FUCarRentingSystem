package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CustomerDashboardController {

    private MainApp mainApp;
    private Account loggedUser;
     private Customer customer ;
    @FXML
    private Button btnBrowseCars, btnMyRentals, btnMyReviews;

    // Setter MainApp
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // Setter Account đăng nhập
    public void setLoggedUser(Account acc) {
        this.loggedUser = acc;
    }

    @FXML
    private void initialize() {
        // Nút duyệt xe
        btnBrowseCars.setOnAction(e -> {
            if (mainApp != null) mainApp.showCarManagement();
        });

        // Nút xem đánh giá
        btnMyReviews.setOnAction(e -> {
            if (mainApp != null) mainApp.showReviewManagement();
        });

        // Nút xem rental
        btnMyRentals.setOnAction(e -> getMyRentals());
    }

    // Mở CarRentalManagement, truyền Account hiện tại
    @FXML
    private void getMyRentals() {
        if (mainApp != null && loggedUser != null) {
            mainApp.showCarRentalManagement(loggedUser);
        }
    }
}
