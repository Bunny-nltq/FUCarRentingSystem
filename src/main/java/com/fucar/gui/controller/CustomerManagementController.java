package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import com.fucar.service.CarRentalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerManagementController {

    private MainApp mainApp;

    private final CustomerService customerService = new CustomerService();
    private final CarRentalService rentalService = new CarRentalService();

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colID;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;

    @FXML private Button btnAddCustomer, btnEditCustomer, btnDeleteCustomer;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        setupTableColumns();
        loadCustomerData();

        btnAddCustomer.setOnAction(e -> openAddCustomerForm());
        btnEditCustomer.setOnAction(e -> openEditCustomerForm());
        btnDeleteCustomer.setOnAction(e -> deleteCustomer());
    }

    // ================================
    // SETUP TABLE COLUMNS
    // ================================
    private void setupTableColumns() {
        colID.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getCustomerID()
                ).asObject()
        );

        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCustomerName()
                )
        );

        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEmail()
                )
        );

        colPhone.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMobile()
                )
        );
    }

    // ================================
    // LOAD DATA
    // ================================
    public void loadCustomerData() {
        customerList.setAll(customerService.getAllCustomers());
        customerTable.setItems(customerList);
    }

    // ================================
    // REFRESH SAU POPUP
    // ================================
    public void reloadAfterPopup() {
        loadCustomerData();
        customerTable.refresh();
    }

    // ================================
    // ADD CUSTOMER
    // ================================
    private void openAddCustomerForm() {
        if (mainApp != null)
            mainApp.showAddCustomerForm(this);  // ⭐ Gửi controller lên MainApp
    }

    // ================================
    // EDIT CUSTOMER
    // ================================
    private void openEditCustomerForm() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a customer to edit.");
            return;
        }

        mainApp.showEditCustomerForm(selected.getCustomerID());
    }

    // ================================
    // DELETE CUSTOMER
    // ================================
    private void deleteCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a customer to delete.");
            return;
        }

        boolean hasRental = rentalService.existsByCustomerId(selected.getCustomerID());

        if (hasRental) {
            showAlert("Error", "This customer has rental transactions and cannot be deleted.");
            return;
        }

        customerService.deleteCustomer(selected.getCustomerID());
        loadCustomerData();
        showAlert("Success", "Customer deleted successfully!");
    }

    // ================================
    // ALERT
    // ================================
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
