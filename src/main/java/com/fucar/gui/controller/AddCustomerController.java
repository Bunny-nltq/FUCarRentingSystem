package com.fucar.gui.controller;

import com.fucar.entity.Customer;
import com.fucar.entity.Account;
import com.fucar.service.CustomerService;
import com.fucar.service.AccountService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddCustomerController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtIdentityCard;
    @FXML private DatePicker dpBirthday;
    @FXML private TextField txtLicenceNumber;
    @FXML private DatePicker dpLicenceDate;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private CustomerManagementController parent;

    private final CustomerService customerService = new CustomerService();
    private final AccountService accountService = new AccountService();

    public void setParentController(CustomerManagementController parent) {
        this.parent = parent;
    }

    @FXML
    private void initialize() {
        btnSave.setOnAction(e -> save());
        btnCancel.setOnAction(e -> parent.clearRightPane());
    }

    private void save() {
        try {

            if (txtName.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtPhone.getText().isEmpty()) {

                showAlert("Validation Error", "Please fill in all required fields.");
                return;
            }

            if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showAlert("Email Error", "Invalid email format.");
                return;
            }

            String defaultPassword = "123456";

            Account acc = new Account();
            acc.setEmail(txtEmail.getText());
            acc.setRole("CUSTOMER");
            acc.setAccountName(txtName.getText());
            acc.setPasswordHash(accountService.hashPassword(defaultPassword));
            accountService.save(acc);

            Customer c = new Customer();
            c.setCustomerName(txtName.getText());
            c.setEmail(txtEmail.getText());
            c.setMobile(txtPhone.getText());
            c.setIdentityCard(txtIdentityCard.getText());
            c.setPassword(defaultPassword);

            c.setBirthday(dpBirthday.getValue());
            c.setLicenceNumber(txtLicenceNumber.getText());
            c.setLicenceDate(dpLicenceDate.getValue());

            c.setAccount(acc);

            customerService.addCustomer(c);

            if (parent != null) parent.reloadAfterForm();

            showAlertInfo("Success", "Customer created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showAlertInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}
