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

    // =====================================================================
    // MAINAPP SETTER
    // =====================================================================
    public void setMainApp(MainApp mainApp) {
        System.out.println("setMainApp CALLED in RegisterController");
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        btnRegister.setOnAction(event -> handleRegister());

        btnGoLogin.setOnAction(event -> {
            if (mainApp != null)
                mainApp.showLogin();
            else
                System.err.println("⚠ mainApp NULL khi bấm Go Login");
        });
    }

    // =====================================================================
    // HANDLE REGISTER
    // =====================================================================
    @FXML
    public void handleRegister() {

        String accountName = txtAccountName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        // ---------- VALIDATION ----------
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

        // ---------- CREATE ACCOUNT ----------
        AuthService authService = new AuthService();
        Account newAccount = authService.registerAndReturn(email, password, "CUSTOMER", accountName);

        if (newAccount == null) {
            System.out.println("❌ Email already exists. Try another one.");
            return;
        }

        System.out.println("✅ Account created! ID = " + newAccount.getAccountId());


        // =====================================================================
        // AUTO CREATE CUSTOMER (VÌ ĐỀ YÊU CẦU TẤT CẢ FIELD NOT NULL)
        // =====================================================================
        CustomerService customerService = new CustomerService();

        Customer customer = new Customer();
        customer.setCustomerName(accountName);
        customer.setEmail(email);

        // Vì đề yêu cầu tất cả field NOT NULL → phải cấp giá trị mặc định
        customer.setMobile("N/A");
        customer.setBirthday("N/A");
        customer.setIdentityCard("N/A");
        customer.setLicenceNumber("N/A");
        customer.setLicenceDate("N/A");

        // Nếu không dùng password trong Customer → nhưng cột NOT NULL → gán mặc định
        customer.setPassword("default");

        customer.setAccount(newAccount);  // FK

        customerService.addCustomer(customer);

        System.out.println("✅ Customer created for AccountID = " + newAccount.getAccountId());


        // ---------- REDIRECT ----------
        if (mainApp == null) {
            System.err.println("⚠ ERROR: mainApp is NULL! Bạn quên gọi setMainApp() trong MainApp.");
            return;
        }

        mainApp.showLogin();
    }
}
