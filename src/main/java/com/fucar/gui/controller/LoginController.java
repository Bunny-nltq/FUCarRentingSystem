package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.service.AccountService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class LoginController {

    private MainApp mainApp;
    private final AccountService accountService = new AccountService(); // service xử lý login

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // ===================== LOGIN =====================
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter email and password.");
            return;
        }

        try {
            Account account = accountService.login(email, password);
            if (account != null) {
                // chuyển tới dashboard theo role
                if (account.getRole().equalsIgnoreCase("ADMIN")) {
                    mainApp.showAdminDashboard(account);
                } else {
                    mainApp.showCustomerDashboard(account);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Email or password is incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred during login.");
        }
    }

    // ===================== GO REGISTER =====================
    @FXML
    private void goRegister(ActionEvent event) {
        mainApp.showRegister();
    }

    // ===================== HELPER =====================
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
