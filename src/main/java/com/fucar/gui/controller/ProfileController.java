package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ProfileController {

    private MainApp mainApp;
    private int currentCustomerID;

    private final CustomerService customerService = new CustomerService();

    // ========== FXML COMPONENTS ==========
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private DatePicker dateBirthday;
    @FXML private TextField txtLicenceNumber;
    @FXML private DatePicker dateLicenceDate;
    @FXML private TextField txtEmail;
    @FXML private Button btnSave;
    @FXML private Button btnBack;

    // ----------- Nhận MainApp -----------
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // ----------- Nhận CustomerID -----------
    public void setCustomerId(int id) {
        this.currentCustomerID = id;
        loadCustomerInfo();
    }

    // ----------- Khởi tạo -----------
    @FXML
    public void initialize() {
        btnSave.setOnAction(e -> saveProfile());
    }

    // ============================================
    // LOAD THÔNG TIN TỪ DATABASE LÊN PROFILE
    // ============================================
    private void loadCustomerInfo() {
        Customer c = customerService.getCustomerById(currentCustomerID);

        if (c == null) {
            System.out.println("⚠ Customer NOT FOUND");
            return;
        }

        txtName.setText(c.getCustomerName());
        txtPhone.setText(c.getMobile());
        txtAddress.setText(c.getIdentityCard());
        txtEmail.setText(c.getEmail());

        if (c.getBirthday() != null)
            dateBirthday.setValue(c.getBirthday());

        txtLicenceNumber.setText(c.getLicenceNumber());

        if (c.getLicenceDate() != null)
            dateLicenceDate.setValue(c.getLicenceDate());
    }

    // ============================================
    // SAVE THAY ĐỔI VÀO DATABASE
    // ============================================
    private void saveProfile() {
        Customer c = customerService.getCustomerById(currentCustomerID);

        if (c == null) {
            showAlert("Error", "Customer not found!");
            return;
        }

        // Gán dữ liệu mới vào object
        c.setCustomerName(txtName.getText());
        c.setMobile(txtPhone.getText());
        c.setIdentityCard(txtAddress.getText());
        c.setEmail(txtEmail.getText());

        LocalDate bd = dateBirthday.getValue();
        if (bd != null) c.setBirthday(bd);

        c.setLicenceNumber(txtLicenceNumber.getText());

        LocalDate ld = dateLicenceDate.getValue();
        if (ld != null) c.setLicenceDate(ld);

        // Gọi service cập nhật DB
        customerService.updateCustomer(c);

        showAlert("Success", "Profile updated successfully!");
    }

    // ============================================
    // NÚT BACK → QUAY VỀ HOME
    // ============================================
    @FXML
    private void goBack(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/CustomerDashboard.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ============================================
    // ALERT HỖ TRỢ
    // ============================================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
}
