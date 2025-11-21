package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ProfileController {

    private MainApp mainApp; 
    private int currentCustomerID;

    private final CustomerService customerService = new CustomerService();

    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private Button btnSave;

    // ĐỂ MAINAPP TRUYỀN THAM CHIẾU
    public void setMainApp(MainApp mainApp) {
        System.out.println("setMainApp CALLED in ProfileController");
        this.mainApp = mainApp;
    }

    // NHẬN CUSTOMER ID từ LoginController
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
            System.out.println("⚠ Customer NOT FOUND → load blank fields");
            return;
        }

        txtName.setText(c.getCustomerName());
        txtPhone.setText(c.getMobile());
        txtAddress.setText(c.getIdentityCard());
    }

    private void saveProfile() {
        Customer c = customerService.getCustomerById(currentCustomerID);

        if (c == null) {
            showAlert("Error", "Customer not found!");
            return;
        }

        c.setCustomerName(txtName.getText());
        c.setMobile(txtPhone.getText());
        c.setIdentityCard(txtAddress.getText());

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
