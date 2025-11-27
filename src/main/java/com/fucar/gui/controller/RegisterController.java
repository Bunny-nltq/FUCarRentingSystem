package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.service.AuthService;
import com.fucar.service.CustomerService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class RegisterController {

    private MainApp mainApp;

    @FXML private TextField txtAccountName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;

    @FXML private Button btnRegister;
    @FXML private Button btnGoLogin;

    // =====================================================
    // MAINAPP SETTER
    // =====================================================
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        btnRegister.setOnAction(e -> handleRegister());

        btnGoLogin.setOnAction(e -> {
            if (mainApp != null) mainApp.showLogin();
        });
    }

    // =====================================================
    // HANDLE REGISTER
    // =====================================================
    @FXML
    private void handleRegister() {

        String accountName = txtAccountName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        // ---------------- VALIDATION ----------------
        if (accountName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Name, Email and Password cannot be empty!");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("❌ Invalid email format!");
            return;
        }

        if (!password.equals(confirm)) {
            System.out.println("❌ Passwords do not match!");
            return;
        }

        if (password.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters!");
            return;
        }

        // =====================================================
        // 1️⃣ TẠO ACCOUNT bằng AuthService
        // =====================================================
        AuthService authService = new AuthService();

        Account newAccount = authService.registerAndReturn(
                email, password, "CUSTOMER", accountName
        );

        if (newAccount == null) {
            System.out.println("❌ Email already exists!");
            return;
        }

        System.out.println("✅ Created Account ID = " + newAccount.getAccountId());


        // =====================================================
        // 2️⃣ TẠO CUSTOMER TƯƠNG ỨNG
        // =====================================================
        CustomerService customerService = new CustomerService();
        Customer c = new Customer();

        c.setCustomerName(accountName);
        c.setEmail(email);

        // Các field NOT NULL phải gán mặc định
        c.setMobile("N/A");
        c.setIdentityCard("N/A");
        c.setLicenceNumber("N/A");

        c.setBirthday(null);
        c.setLicenceDate(null);

        c.setAccount(newAccount); // 🔥 BẮT BUỘC – FK

        customerService.addCustomer(c);

        System.out.println("✅ Customer created and linked to AccountID " + newAccount.getAccountId());


        // =====================================================
        // REDIRECT → LOGIN
        // =====================================================
        if (mainApp != null) {
            mainApp.showLogin();
        } else {
            System.err.println("⚠ mainApp NULL — bạn chưa gọi setMainApp()");
        }
    }
}
