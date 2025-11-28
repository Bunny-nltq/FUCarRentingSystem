package com.fucar.gui.controller;

import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditCustomerController {

    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtIdentityCard;
    @FXML private DatePicker dpBirthday;
    @FXML private TextField txtLicenceNumber;
    @FXML private DatePicker dpLicenceDate;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final CustomerService customerService = new CustomerService();

    private Integer editingCustomerId;

    private CustomerManagementController parentController;

    public void setParentController(CustomerManagementController parent) {
        this.parentController = parent;
    }

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
        txtIdentityCard.setText(c.getIdentityCard());

        dpBirthday.setValue(c.getBirthday());
        txtLicenceNumber.setText(c.getLicenceNumber());
        dpLicenceDate.setValue(c.getLicenceDate());
    }

    @FXML
    private void initialize() {
        btnSave.setOnAction(e -> saveCustomer());
        btnCancel.setOnAction(e -> parentController.clearRightPane());
    }

    private void saveCustomer() {
        Customer c = customerService.getCustomerById(editingCustomerId);
        if (c == null) return;

        c.setCustomerName(txtName.getText());
        c.setMobile(txtPhone.getText());
        c.setEmail(txtEmail.getText());
        c.setIdentityCard(txtIdentityCard.getText());

        c.setBirthday(dpBirthday.getValue());
        c.setLicenceNumber(txtLicenceNumber.getText());
        c.setLicenceDate(dpLicenceDate.getValue());

        customerService.updateCustomer(c);

        if (parentController != null) {
            parentController.reloadAfterForm();
        }
    }
}