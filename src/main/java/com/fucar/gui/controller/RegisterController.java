package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.service.AuthService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class RegisterController {

    private MainApp mainApp;

    @FXML
    private TextField txtAccountName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnGoLogin;

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
            System.out.println("Name, Email and Password cannot be empty!");
            return;
        }

        // 2. Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Invalid email format!");
            return;
        }

        // 3. Check password trùng
        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match!");
            return;
        }

        // 4. Password tối thiểu 6 ký tự
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters!");
            return;
        }

        // 5. Call AuthService
        AuthService authService = new AuthService();
        boolean success = authService.register(email, password, "CUSTOMER", accountName);

        if (success) {
            System.out.println("Register successful! You can now login.");
            try {
                mainApp.showCustomerDashboard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Email already exists. Try another one.");
        }
    }
}
