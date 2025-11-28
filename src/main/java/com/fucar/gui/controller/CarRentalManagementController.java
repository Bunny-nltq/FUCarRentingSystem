package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.CarRental;
import com.fucar.service.CarRentalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public class CarRentalManagementController {

    private MainApp mainApp;

    @FXML private TableView<CarRental> rentalTable;

    @FXML private TableColumn<CarRental, Integer> colRentalID;
    @FXML private TableColumn<CarRental, String> colCustomer;
    @FXML private TableColumn<CarRental, String> colCar;
    @FXML private TableColumn<CarRental, LocalDate> colStartDate;
    @FXML private TableColumn<CarRental, LocalDate> colEndDate;
    @FXML private TableColumn<CarRental, String> colStatus;
    @FXML private TableColumn<CarRental, Double> colPrice;

    @FXML private Button btnApprove, btnReject, btnDelete, btnRefresh;

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

        btnApprove.setOnAction(e -> approveRental());
        btnReject.setOnAction(e -> rejectRental());
        btnDelete.setOnAction(e -> deleteRental());
        btnRefresh.setOnAction(e -> loadRentals());
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
                        c.getValue().getStartDate()
                )
        );

        colEndDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getEndDate()
                )
        );

        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStatus()
                )
        );

        colPrice.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(
                        c.getValue().getActualPrice()
                ).asObject()
        );

        rentalTable.setItems(rentalList);
    }

    // ============================
    // LOAD RENTAL FROM DATABASE
    // ============================
    private void loadRentals() {
        try {
            rentalList.setAll(rentalService.getAll());
            rentalTable.refresh();
        } catch (Exception e) {
            showAlert("Error", "Error loading rentals: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================
    // APPROVE RENTAL
    // ============================
    private void approveRental() {
        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a rental to approve.");
            return;
        }

        if (!selected.getStatus().equals("PENDING")) {
            showAlert("Warning", "Only pending rentals can be approved.");
            return;
        }

        try {
            selected.setStatus("APPROVED");
            rentalService.update(selected);
            loadRentals();
            showAlert("Success", "Rental approved successfully.");
        } catch (Exception e) {
            showAlert("Error", "Error approving rental: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================
    // REJECT RENTAL
    // ============================
    private void rejectRental() {
        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a rental to reject.");
            return;
        }

        if (!selected.getStatus().equals("PENDING")) {
            showAlert("Warning", "Only pending rentals can be rejected.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reject Rental");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to reject this rental request?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                selected.setStatus("REJECTED");
                rentalService.update(selected);
                loadRentals();
                showAlert("Success", "Rental rejected successfully.");
            } catch (Exception e) {
                showAlert("Error", "Error rejecting rental: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ============================
    // DELETE RENTAL
    // ============================
    private void deleteRental() {

        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a rental to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Rental");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this rental record?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                rentalService.delete(selected.getRentalId());
                rentalList.remove(selected);
                showAlert("Success", "Rental deleted successfully.");
            } catch (Exception e) {
                showAlert("Error", "Error deleting rental: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ============================
    // ALERT SUPPORT
    // ============================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
