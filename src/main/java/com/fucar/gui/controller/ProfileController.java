package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ProfileController {

    private MainApp mainApp;
    private int currentCustomerID;

    private final CustomerService customerService = new CustomerService();

    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtIdentityCard;
    @FXML private DatePicker dateBirthday;
    @FXML private TextField txtLicenceNumber;
    @FXML private DatePicker dateLicenceDate;
    @FXML private TextField txtEmail;
    @FXML private Button btnSave;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // ⭐ HÀM NÀY BẮT BUỘC PHẢI ĐƯỢC GỌI
    public void setCustomerId(int id) {
        this.currentCustomerID = id;
        loadCustomerInfo();
    }

    @FXML
    public void initialize() {
        btnSave.setOnAction(e -> saveProfile());
    }

    private void loadCustomerInfo() {
        Customer c = customerService.getCustomerById(currentCustomerID);

        if (c == null) {
            System.out.println("⚠ Customer NOT FOUND");
            return;
        }

        txtName.setText(c.getCustomerName());
        txtPhone.setText(c.getMobile());
        txtIdentityCard.setText(c.getIdentityCard());
        txtEmail.setText(c.getEmail());

        if (c.getBirthday() != null)
            dateBirthday.setValue(c.getBirthday());

        txtLicenceNumber.setText(c.getLicenceNumber());

        if (c.getLicenceDate() != null)
            dateLicenceDate.setValue(c.getLicenceDate());
    }

    private void saveProfile() {
        Customer c = customerService.getCustomerById(currentCustomerID);
        if (c == null) {
            showAlert("Error", "Customer not found!");
            return;
        }

        c.setCustomerName(txtName.getText());
        c.setMobile(txtPhone.getText());
        c.setIdentityCard(txtIdentityCard.getText());
        c.setEmail(txtEmail.getText());

        if (dateBirthday.getValue() != null)
            c.setBirthday(dateBirthday.getValue());

        c.setLicenceNumber(txtLicenceNumber.getText());

        if (dateLicenceDate.getValue() != null)
            c.setLicenceDate(dateLicenceDate.getValue());

        customerService.updateCustomer(c);
        showAlert("Success", "Profile updated successfully!");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
