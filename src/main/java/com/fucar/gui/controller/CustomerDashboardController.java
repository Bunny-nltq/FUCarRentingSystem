package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class CustomerDashboardController {

    @FXML
    private StackPane contentArea;  // nơi load các màn hình con

    private MainApp mainApp;
    private Account loggedUser;
    private int loggedCustomerId;

    // ===============================
    // SETUP
    // ===============================
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Truyền Account từ MainApp khi login
     */
    public void setLoggedCustomer(Account loggedUser) {
        this.loggedUser = loggedUser;

        // Lấy CustomerID từ Account → Customer
        if (loggedUser.getCustomer() != null) {
            this.loggedCustomerId = loggedUser.getCustomer().getCustomerID();
        }

        // Load profile mặc định
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
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent screen = loader.load();

            Object ctrl = loader.getController();

            // Inject MainApp nếu controller có setMainApp
            try {
                ctrl.getClass().getMethod("setMainApp", MainApp.class)
                        .invoke(ctrl, mainApp);
            } catch (NoSuchMethodException ignored) {}

            // Inject CustomerID nếu controller có setCustomerId
            try {
                ctrl.getClass().getMethod("setCustomerId", int.class)
                        .invoke(ctrl, loggedCustomerId);
            } catch (NoSuchMethodException ignored) {}

            // Hiển thị giao diện trong contentArea
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerRentals.fxml"));
            Parent screen = loader.load();

            CustomerRentalsController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setLoggedUser(loggedUser);
            controller.setCustomerId(loggedCustomerId);

            contentArea.getChildren().setAll(screen);
        } catch (Exception e) {
            System.err.println("❌ Lỗi load CustomerRentals.fxml");
            e.printStackTrace();
        }
    }
    
//    
//    private void hoadMyProfile() {
//        loadView("/fxml/Profile.fxml"); // Sử dụng hàm loadView() đã có
//    }

    
    @FXML
    private void handleBack() {
    	 if (mainApp != null && loggedUser != null) {
             mainApp.showCustomerDashboard(loggedUser);
         }
    }
    
//    @FXML
//    private void handleMyRentals() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerRentals.fxml"));
//            Parent screen = loader.load();
//
//            CustomerRentalsController controller = loader.getController();
//            controller.setMainApp(mainApp);
//            controller.setLoggedUser(loggedUser);  // truyền account
//            controller.setCustomerId(loggedUser.getCustomer().getCustomerID());
//
//            contentArea.getChildren().setAll(screen);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @FXML
    private void handleMyProfile() {
        loadMyProfile();
    }

    @FXML
    private void handleReview() {
        loadView("/fxml/Review.fxml");
    }

    @FXML
    private void handleLogout() {
        if (mainApp != null) {
            mainApp.showLogin();
        }
    }

    // ===============================
    // PRIVATE HELPERS
    // ===============================
    private void loadMyProfile() {
        loadView("/fxml/Profile.fxml");
    }

}
