package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.service.AccountService;
import com.fucar.service.CustomerService;

import javafx.fxml.FXML;
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

        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        // Kiểm tra account
        Account acc = accountService.login(email, pass);

        if (acc == null) {
            System.out.println("❌ Sai tài khoản hoặc mật khẩu");
            return;
        }

        System.out.println("Đăng nhập thành công!");

        // ========================
        // PHÂN QUYỀN
        // ========================
        if (acc.getRole().equalsIgnoreCase("ADMIN")) {
            mainApp.showAdminDashboard();
            return;
        }

        // ========================
        // CUSTOMER LOGIN
        // ========================
        Customer customer = customerService.findByAccountId(acc.getAccountId()); // HÀM NÀY PHẢI TỒN TẠI

        if (customer == null) {
            System.out.println("❌ Không tìm thấy Customer với AccountID = " + acc.getAccountId());
            return;
        }

        // Lấy ID đúng theo entity của bạn
        int customerId = customer.getCustomerID();

        // Gửi sang MainApp → mở Dashboard và truyền customerId
        mainApp.showCustomerDashboard(customerId);
    }

    @FXML
    public void goRegister() {
        mainApp.showRegister();
    }
}
