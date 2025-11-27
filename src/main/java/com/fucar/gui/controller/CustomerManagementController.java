package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Customer;
import com.fucar.service.CustomerService;
import com.fucar.service.CarRentalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;

public class CustomerManagementController {

    private MainApp mainApp;

    private final CustomerService customerService = new CustomerService();
    private final CarRentalService rentalService = new CarRentalService();

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colID;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colBirthday;
    @FXML private TableColumn<Customer, String> colIdentityCard;
    @FXML private TableColumn<Customer, String> colLicenceNumber;
    @FXML private TableColumn<Customer, String> colLicenceDate;


    @FXML private StackPane rightPane;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        setupTableColumns();
        loadCustomerData();
    }

    private void setupTableColumns() {
        colID.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCustomerID()).asObject()
        );

        colName.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getCustomerName())
        );

        colEmail.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail())
        );

        colPhone.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getMobile())
        );

        colBirthday.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getBirthday() == null ? "" : c.getValue().getBirthday().toString()
            )
        );

        colIdentityCard.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getIdentityCard())
        );

        colLicenceNumber.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getLicenceNumber())
        );

        colLicenceDate.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getLicenceDate() == null ? "" : c.getValue().getLicenceDate().toString()
            )
        );
    }


    public void loadCustomerData() {
        customerList.setAll(customerService.getAllCustomers());
        customerTable.setItems(customerList);
    }

    public void reloadAfterForm() {
        loadCustomerData();
        customerTable.refresh();
        clearRightPane();                             // ⭐ Form tự đóng sau khi lưu
    }

    public void clearRightPane() {
        rightPane.getChildren().clear();
    }

    @FXML
    private void handleAddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCustomer.fxml"));
            Parent form = loader.load();

            AddCustomerController controller = loader.getController();
            controller.setParentController(this);

            rightPane.getChildren().setAll(form);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void handleEditCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a customer to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditCustomer.fxml"));
            Parent form = loader.load();

            EditCustomerController controller = loader.getController();
            controller.setParentController(this);
            controller.setCustomerId(selected.getCustomerID());

            rightPane.getChildren().setAll(form);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCustomer() {
        Customer c = customerTable.getSelectionModel().getSelectedItem();

        if (c == null) {
            showAlert("Warning", "Please select a customer to delete.");
            return;
        }

        if (rentalService.existsByCustomerId(c.getCustomerID())) {
            showAlert("Error", "Customer has rental history. Cannot delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete customer: " + c.getCustomerName() + " ?", ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            customerService.deleteCustomer(c.getCustomerID());
            reloadAfterForm();
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
