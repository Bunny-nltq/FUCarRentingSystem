package com.fucar.gui.controller;

import com.fucar.MainApp;
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
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirm = txtConfirmPassword.getText();

        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match!");
            return;
        }

        System.out.println("Register attempt: " + email + " / " + password);
        // TODO: Thêm logic lưu user vào DB
    }
}
