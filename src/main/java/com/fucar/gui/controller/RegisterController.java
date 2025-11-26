package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    private MainApp mainApp;

    @FXML private TextField txtAccountName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnRegister;
    @FXML private Button btnGoLogin;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnRegister.setOnAction(event -> handleRegister());
        btnGoLogin.setOnAction(event -> mainApp.showLogin());
    }

    @FXML
    public void handleRegister() {
        String accountName = txtAccountName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        // 1. Validate trống
        if (accountName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name, Email and Password cannot be empty!");
            return;
        }

        // 2. Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid email format!");
            return;
        }

        // 3. Check password trùng
        if (!password.equals(confirm)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Passwords do not match!");
            return;
        }

        // 4. Password tối thiểu 6 ký tự
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Password must be at least 6 characters!");
            return;
        }

        // 5. Call AuthService
        AuthService authService = new AuthService();
        Account account = authService.registerAndReturnAccount(email, accountName, password, "CUSTOMER");

        if (account != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Register successful! You can now login.");
            // chuyển đến dashboard Customer với Account vừa tạo
            mainApp.showCustomerDashboard(account);
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Email already exists. Try another one.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
