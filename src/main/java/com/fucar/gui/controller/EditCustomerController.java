package com.fucar.gui.controller;

import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCustomerController {

    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAddress;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final CustomerService customerService = new CustomerService();
    private Integer editingCustomerId;

    public void setCustomerId(Integer customerId) {
        this.editingCustomerId = customerId;
        loadCustomerData();
    }

    private void loadCustomerData() {
        Customer c = customerService.getCustomerById(editingCustomerId);
        if (c == null) return;

        txtName.setText(c.getCustomerName());
        txtPhone.setText(c.getMobile());
        txtEmail.setText(c.getEmail());
        txtAddress.setText(c.getIdentityCard());  // tạm dùng Address = IdentityCard
    }

    @FXML
    private void initialize() {
        btnSave.setOnAction(e -> saveCustomer());
        btnCancel.setOnAction(e -> closeForm());
    }

    private void saveCustomer() {
        Customer c = customerService.getCustomerById(editingCustomerId);
        if (c == null) return;

        c.setCustomerName(txtName.getText());
        c.setMobile(txtPhone.getText());
        c.setEmail(txtEmail.getText());
        c.setIdentityCard(txtAddress.getText());

        customerService.updateCustomer(c);

        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
