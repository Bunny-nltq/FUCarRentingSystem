package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.CarRental;
import com.fucar.service.CarRentalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class CarRentalManagementController {

    private MainApp mainApp;

    @FXML private TableView<CarRental> rentalTable;

    @FXML private TableColumn<CarRental, Integer> colRentalID;
    @FXML private TableColumn<CarRental, String> colCustomer;
    @FXML private TableColumn<CarRental, String> colCar;
    @FXML private TableColumn<CarRental, LocalDate> colStartDate;
    @FXML private TableColumn<CarRental, LocalDate> colEndDate;

    @FXML private Button btnAddRental, btnEditRental, btnDeleteRental;

    // DÙNG ĐÚNG SERVICE
    private final CarRentalService rentalService = new CarRentalService();

    private final ObservableList<CarRental> rentalList = FXCollections.observableArrayList();

    // -------- MainApp Setter --------
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // -------- Initialize Table --------
    @FXML
    private void initialize() {

        setupTable();
        loadRentals();

        btnAddRental.setOnAction(e -> addRental());
        btnEditRental.setOnAction(e -> editRental());
        btnDeleteRental.setOnAction(e -> deleteRental());
    }

    // ============================
    // SETUP TABLE COLUMN
    // ============================
    private void setupTable() {

        colRentalID.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(
                        c.getValue().getRentalId()
                ).asObject()
        );

        colCustomer.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getCustomer() != null
                                ? c.getValue().getCustomer().getCustomerName()
                                : "Unknown"
                )
        );

        colCar.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getCar() != null
                                ? c.getValue().getCar().getCarName()
                                : "Unknown"
                )
        );

        colStartDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getPickupDate()
                )
        );

        colEndDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getReturnDate()
                )
        );

        rentalTable.setItems(rentalList);
    }

    // ============================
    // LOAD RENTAL FROM DATABASE
    // ============================
    private void loadRentals() {
        rentalList.setAll(rentalService.getAll());
    }

    // ============================
    // ADD RENTAL
    // ============================
    private void addRental() {
        showAlert("Info", "Add Rental Clicked (chưa làm form)");
    }

    // ============================
    // EDIT RENTAL
    // ============================
    private void editRental() {
        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Please select a rental to edit.");
            return;
        }

        showAlert("Edit", "Edit rental: " + selected.getRentalId());
    }

    // ============================
    // DELETE RENTAL
    // ============================
    private void deleteRental() {

        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Please select a rental to delete.");
            return;
        }

        rentalService.delete(selected.getRentalId());
        rentalList.remove(selected);

        showAlert("Success", "Rental deleted.");
    }

    // ============================
    // ALERT SUPPORT
    // ============================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
