package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.Service.AuthService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class RegisterController {

    private MainApp mainApp;

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
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        // 1. Check password trùng
        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match!");
            return;
        }

        // 2. Validate email trống
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email and password cannot be empty!");
            return;
        }

        // 3. Sử dụng AuthService để lưu user
        AuthService authService = new AuthService();
        boolean success = authService.register(email, password, "CUSTOMER"); // role mặc định CUSTOMER

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
