package com.fucar.browsecars;

import java.time.LocalDate;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.entity.CarRental;
import com.fucar.service.CarRentalService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class RentCarController {

    @FXML
    private Label lblCarName;

    @FXML
    private Label lblPrice;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;

    @FXML
    private Spinner<Integer> spinnerDays;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Button btnRent;

    private Car car;
    private int customerId;
    private MainApp mainApp;
    private CarRentalService rentalService;

    @FXML
    public void initialize() {
        rentalService = new CarRentalService();

        // Setup spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1);
        spinnerDays.setValueFactory(valueFactory);

        // Set today as default start date
        dpStartDate.setValue(LocalDate.now());
        dpEndDate.setValue(LocalDate.now().plusDays(1));

        // Listen to date changes
        dpStartDate.valueProperty().addListener((obs, oldVal, newVal) -> updateEndDate());
        dpEndDate.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        spinnerDays.valueProperty().addListener((obs, oldVal, newVal) -> updateEndDate());
    }

    public void setCar(Car car) {
        this.car = car;
        lblCarName.setText(car.getCarName() + " - " + car.getLicensePlate());
        lblPrice.setText("$" + String.format("%.2f", car.getRentPrice()) + " / day");
        updateTotalPrice();
    }

    public void setCustomerId(int id) {
        this.customerId = id;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void updateEndDate() {
        if (dpStartDate.getValue() != null) {
            int days = spinnerDays.getValue();
            dpEndDate.setValue(dpStartDate.getValue().plusDays(days));
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        if (dpStartDate.getValue() != null && dpEndDate.getValue() != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(dpStartDate.getValue(), dpEndDate.getValue());
            if (days < 0) days = 0;
            if (days == 0) days = 1;

            double totalPrice = car.getRentPrice() * days;
            lblTotalPrice.setText("Total: $" + String.format("%.2f", totalPrice) + " for " + days + " day(s)");
        }
    }

    @FXML
    private void handleRent() {
        try {
            LocalDate startDate = dpStartDate.getValue();
            LocalDate endDate = dpEndDate.getValue();

            // Validation
            if (startDate == null || endDate == null) {
                showError("Please select start and end dates");
                return;
            }

            if (startDate.isAfter(endDate)) {
                showError("Start date must be before end date");
                return;
            }

            if (startDate.isBefore(LocalDate.now())) {
                showError("Start date cannot be in the past");
                return;
            }

            // Create rental
            CarRental rental = new CarRental();
            rental.setPickupDate(startDate);
            rental.setReturnDate(endDate);

            long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            if (days == 0) days = 1;

            double totalPrice = car.getRentPrice() * days;
            rental.setRentPrice(totalPrice);
            rental.setStatus("PENDING");

            rentalService.createRental(rental, customerId, car.getCarID());

            showSuccess("Rental request created successfully! Waiting for approval.");
            handleClose();

        } catch (Exception e) {
            showError("Error creating rental: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) btnRent.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
