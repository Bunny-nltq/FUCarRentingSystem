package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.service.AccountService;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private MainApp mainApp;
    private final AccountService accountService = new AccountService();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    public void handleLogin() {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        Account acc = accountService.login(email, pass);

        if (acc == null) {
            System.out.println("Sai tài khoản hoặc mật khẩu");
            return;
        }

        // PHÂN QUYỀN
        if (acc.getRole().equals("ADMIN")) {
            mainApp.showAdminDashboard();
        } else {
            mainApp.showCustomerDashboard();
        }
    }

    @FXML
    public void goRegister() {
        mainApp.showRegister();
    }
}
