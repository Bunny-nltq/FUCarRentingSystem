package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.service.AccountService;
import com.fucar.service.CustomerService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private MainApp mainApp;

    private final AccountService accountService = new AccountService();
    private final CustomerService customerService = new CustomerService();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    public void handleLogin() {

        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please enter email & password.");
            return;
        }

        // Kiểm tra account
        Account acc = accountService.login(email, pass);

        if (acc == null) {
            showError("❌ Sai tài khoản hoặc mật khẩu");
            return;
        }

        System.out.println("Đăng nhập thành công!");

        // ========================
        // PHÂN QUYỀN
        // ========================
        if (acc.getRole() != null && acc.getRole().equalsIgnoreCase("ADMIN")) {
            System.out.println("→ Admin login detected. Redirecting to Admin Dashboard...");
            mainApp.showAdminDashboard();   // ⭐⭐ ĐIỀU HƯỚNG ADMIN Ở ĐÂY
            return;
        }

        // ========================
        // CUSTOMER LOGIN
        // ========================
        Customer customer = customerService.findByAccountId(acc.getAccountId());

        if (customer == null) {
            showError("❌ Không tìm thấy Customer có AccountID = " + acc.getAccountId());
            return;
        }

        int customerId = customer.getCustomerID();

        System.out.println("→ Customer login. Redirecting to Customer Dashboard...");
        mainApp.showCustomerDashboard(customerId);
    }

    @FXML
    public void goRegister() {
        mainApp.showRegister();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Login Failed");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
