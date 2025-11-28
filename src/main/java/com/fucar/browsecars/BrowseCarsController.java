package com.fucar.browsecars;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.entity.CarRental;
import com.fucar.service.CarRentalService;
import com.fucar.service.CarService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BrowseCarsController {

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> cmbFilter;

    @FXML
    private Button btnSearch;

    @FXML
    private VBox carsContainer;

    @FXML
    private ScrollPane scrollPane;

    // Details Section
    @FXML
    private VBox detailsSection;

    @FXML
    private Label lblCarName;

    @FXML
    private Label lblProducer;

    @FXML
    private Label lblYear;

    @FXML
    private Label lblColor;

    @FXML
    private Label lblCapacity;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblLicensePlate;

    @FXML
    private Label lblImportDate;

    @FXML
    private TextArea txtDescription;

    // Rental Section
    @FXML
    private VBox rentalSection;

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

    private MainApp mainApp;
    private int loggedCustomerId;
    private CarService carService;
    private CarRentalService rentalService;
    private ObservableList<Car> carsList;
    private Car selectedCar;

    // ===============================
    // INITIALIZATION
    // ===============================
    @FXML
    public void initialize() {
        carService = new CarService();
        rentalService = new CarRentalService();
        carsList = FXCollections.observableArrayList();

        // Setup filter options
        cmbFilter.setItems(FXCollections.observableArrayList(
                "All Cars",
                "Available",
                "Compact",
                "Sedan",
                "SUV"
        ));
        cmbFilter.setValue("All Cars");

        // Setup rental spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1);
        spinnerDays.setValueFactory(valueFactory);

        // Set today as default start date
        dpStartDate.setValue(LocalDate.now());
        dpEndDate.setValue(LocalDate.now().plusDays(1));

        // Listen to date changes
        dpStartDate.valueProperty().addListener((obs, oldVal, newVal) -> updateEndDate());
        dpEndDate.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        spinnerDays.valueProperty().addListener((obs, oldVal, newVal) -> updateEndDate());

        // Load all cars on startup
        loadCars();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setCustomerId(int id) {
        this.loggedCustomerId = id;
    }

    // ===============================
    // LOAD CARS
    // ===============================
    private void loadCars() {
        try {
            List<Car> cars = carService.getAllCars();
            carsList.setAll(cars);
            displayCars(carsList);
        } catch (Exception e) {
            showError("Error loading cars: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // DISPLAY CARS
    // ===============================
    private void displayCars(List<Car> cars) {
        carsContainer.getChildren().clear();

        if (cars == null || cars.isEmpty()) {
            Label noDataLabel = new Label("No cars found");
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #999;");
            carsContainer.getChildren().add(noDataLabel);
            return;
        }

        for (Car car : cars) {
            HBox carCard = createCarCard(car);
            carsContainer.getChildren().add(carCard);
        }
    }

    // ===============================
    // CREATE CAR CARD (CLICKABLE)
    // ===============================
    private HBox createCarCard(Car car) {
        HBox card = new HBox(15);
        card.setStyle("-fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 12; -fx-background-color: #ecf0f1; -fx-border-width: 2; -fx-cursor: hand;");
        card.setPrefHeight(100);

        // Left side: Car info
        VBox infoBox = new VBox(6);
        infoBox.setPrefWidth(300);
        infoBox.setStyle("-fx-cursor: hand;");

        // Car name
        Label lblCarName = new Label(car.getCarName());
        lblCarName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Car details - compact view
        Label lblDetails = new Label(
            (car.getProducer() != null ? car.getProducer().getName() : "N/A") + " • " +
            car.getCarModelYear() + " • " +
            car.getCapacity() + " seats"
        );
        lblDetails.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

        Label lblStatus = new Label("Status: " + car.getStatus());
        String statusColor = "AVAILABLE".equals(car.getStatus()) ? "#27ae60" : "#e74c3c";
        lblStatus.setStyle("-fx-font-size: 11px; -fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(lblCarName, lblDetails, lblStatus);

        // Right side: Price and button
        VBox actionBox = new VBox(8);
        actionBox.setStyle("-fx-alignment: CENTER; -fx-cursor: hand;");

        Label lblPrice = new Label("$" + String.format("%.2f", car.getRentPrice()));
        lblPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Label lblPriceDay = new Label("/ day");
        lblPriceDay.setStyle("-fx-font-size: 10px; -fx-text-fill: #7f8c8d;");

        Button btnSelect = new Button("Select");
        btnSelect.setStyle("-fx-padding: 6 15; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 3; -fx-font-size: 11px;");
        btnSelect.setPrefWidth(80);
        btnSelect.setOnAction(e -> selectCar(car));

        actionBox.getChildren().addAll(lblPrice, lblPriceDay, btnSelect);

        card.getChildren().addAll(infoBox, actionBox);

        // Make entire card clickable to select car
        card.setOnMouseClicked(e -> selectCar(car));

        return card;
    }

    // ===============================
    // SELECT CAR - Display details inline
    // ===============================
    private void selectCar(Car car) {
        this.selectedCar = car;

        // Update car details
        lblCarName.setText(car.getCarName());
        lblProducer.setText(car.getProducer() != null ? car.getProducer().getName() : "N/A");
        lblYear.setText(String.valueOf(car.getCarModelYear()));
        lblColor.setText(car.getColor());
        lblCapacity.setText(car.getCapacity() + " seats");
        lblLicensePlate.setText(car.getLicensePlate());
        lblImportDate.setText(car.getImportDate() != null ? car.getImportDate().toString() : "N/A");
        lblPrice.setText("$" + String.format("%.2f", car.getRentPrice()) + " / day");
        lblStatus.setText(car.getStatus());
        String statusColor = "AVAILABLE".equals(car.getStatus()) ? "#27ae60" : "#e74c3c";
        lblStatus.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");
        txtDescription.setText(car.getDescription() != null ? car.getDescription() : "No description available");

        // Update rental form
        dpStartDate.setValue(LocalDate.now());
        dpEndDate.setValue(LocalDate.now().plusDays(1));
        spinnerDays.getValueFactory().setValue(1);
        updateTotalPrice();

        // Enable/disable rent button based on availability
        btnRent.setDisable(!car.getStatus().equals("AVAILABLE"));

        // Highlight selected car card
        refreshCarCardStyles();
    }

    // ===============================
    // REFRESH CAR CARD STYLES
    // ===============================
    private void refreshCarCardStyles() {
        for (javafx.scene.Node node : carsContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox card = (HBox) node;
                if (card.getUserData() == selectedCar) {
                    card.setStyle("-fx-border-color: #27ae60; -fx-border-radius: 5; -fx-padding: 12; -fx-background-color: #d5f4e6; -fx-border-width: 3; -fx-cursor: hand;");
                } else {
                    card.setStyle("-fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 12; -fx-background-color: #ecf0f1; -fx-border-width: 2; -fx-cursor: hand;");
                }
            }
        }
    }

    // ===============================
    // SEARCH FUNCTIONALITY
    // ===============================
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        String filterType = cmbFilter.getValue();

        List<Car> filteredCars = carsList.stream()
                .filter(car -> searchText.isEmpty() || 
                        car.getCarName().toLowerCase().contains(searchText) ||
                        (car.getProducer() != null && car.getProducer().getName().toLowerCase().contains(searchText)))
                .filter(car -> {
                    if ("Available".equals(filterType)) {
                        return "AVAILABLE".equals(car.getStatus());
                    }
                    return true;
                })
                .toList();

        displayCars(filteredCars);
    }

    @FXML
    private void handleFilterChange() {
        handleSearch();
    }

    // ===============================
    // RENTAL DATE MANAGEMENT
    // ===============================
    private void updateEndDate() {
        if (dpStartDate.getValue() != null) {
            int days = spinnerDays.getValue();
            dpEndDate.setValue(dpStartDate.getValue().plusDays(days));
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        if (selectedCar != null && dpStartDate.getValue() != null && dpEndDate.getValue() != null) {
            long days = ChronoUnit.DAYS.between(dpStartDate.getValue(), dpEndDate.getValue());
            if (days < 0) days = 0;
            if (days == 0) days = 1;

            double totalPrice = selectedCar.getRentPrice() * days;
            lblTotalPrice.setText("Total: $" + String.format("%.2f", totalPrice) + " for " + days + " day(s)");
        }
    }

    // ===============================
    // HANDLE RENT INLINE
    // ===============================
    @FXML
    private void handleRentInline() {
        try {
            if (selectedCar == null) {
                showError("Please select a car first");
                return;
            }

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

            if (!selectedCar.getStatus().equals("AVAILABLE")) {
                showError("This car is not available for rental");
                return;
            }

            // Create rental
            CarRental rental = new CarRental();
            rental.setPickupDate(startDate);
            rental.setReturnDate(endDate);

            long days = ChronoUnit.DAYS.between(startDate, endDate);
            if (days == 0) days = 1;

            double totalPrice = selectedCar.getRentPrice() * days;
            rental.setRentPrice(totalPrice);
            rental.setStatus("PENDING");

            rentalService.createRental(rental, loggedCustomerId, selectedCar.getCarID());

            showSuccess("Rental request created successfully!\nYour booking is pending admin approval.");
            
            // Reset form
            selectedCar = null;
            clearRentalForm();
            loadCars();

        } catch (Exception e) {
            showError("Error creating rental: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // CLEAR RENTAL FORM
    // ===============================
    private void clearRentalForm() {
        lblCarName.setText("Select a car");
        lblProducer.setText("N/A");
        lblYear.setText("N/A");
        lblColor.setText("N/A");
        lblCapacity.setText("N/A");
        lblLicensePlate.setText("N/A");
        lblImportDate.setText("N/A");
        lblPrice.setText("$0.00 / day");
        lblStatus.setText("N/A");
        txtDescription.setText("");
        
        dpStartDate.setValue(LocalDate.now());
        dpEndDate.setValue(LocalDate.now().plusDays(1));
        spinnerDays.getValueFactory().setValue(1);
        lblTotalPrice.setText("Total: $0.00 for 0 day(s)");
        btnRent.setDisable(true);
    }

    // ===============================
    // UTILITIES
    // ===============================
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

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        cmbFilter.setValue("All Cars");
        selectedCar = null;
        clearRentalForm();
        loadCars();
    }
}
