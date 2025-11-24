package com.fucar.gui.controller;

import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.service.AccountService;
import com.fucar.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomerController {

    private final CustomerService customerService = new CustomerService();
    private final AccountService accountService = new AccountService();

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    @FXML
    public void initialize() {
        btnSave.setOnAction(e -> saveCustomer());
        btnCancel.setOnAction(e -> close());
    }

    private void saveCustomer() {

        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        // ============================
        // 1) TẠO ACCOUNT
        // ============================
        Account acc = new Account();
        acc.setEmail(email);
        acc.setAccountName(name);
        acc.setRole("CUSTOMER");

        acc.setPasswordHash(accountService.hashPassword("123456")); // default

        accountService.save(acc);

        // ============================
        // 2) TẠO CUSTOMER
        // ============================
        Customer c = new Customer();
        c.setCustomerName(name);
        c.setEmail(email);
        c.setMobile(phone);

        c.setPassword("123456");  // NOT NULL column
        c.setIdentityCard("N/A");
        c.setLicenceNumber("N/A");
        c.setBirthday(null);
        c.setLicenceDate(null);

        c.setAccount(acc);

        customerService.addCustomer(c);

        close();
    }

    private void close() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
