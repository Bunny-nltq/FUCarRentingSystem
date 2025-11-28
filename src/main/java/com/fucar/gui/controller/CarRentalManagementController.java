package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.entity.CarRental;
import com.fucar.entity.Customer;
import com.fucar.repository.CarRepository;
import com.fucar.repository.CustomerRepository;
import com.fucar.service.CarRentalService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class CarRentalManagementController {

    // ========== FORM FIELDS ==========
    @FXML private ComboBox<Customer> cmbCustomer;
    @FXML private ComboBox<Car> cmbCar;
    @FXML private DatePicker dpPickupDate;
    @FXML private DatePicker dpReturnDate;
    @FXML private TextField txtRentPrice;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Button btnSave;
    @FXML private Button btnClear;

    // ========== TABLE ==========
    @FXML private TableView<CarRental> rentalTable;
    @FXML private TableColumn<CarRental, Integer> colRentalID;
    @FXML private TableColumn<CarRental, String> colCustomer;
    @FXML private TableColumn<CarRental, String> colCar;
    @FXML private TableColumn<CarRental, String> colPickupDate;
    @FXML private TableColumn<CarRental, String> colReturnDate;
    @FXML private TableColumn<CarRental, String> colRentPrice;
    @FXML private TableColumn<CarRental, String> colStatus;

    private MainApp mainApp;
    private final CarRentalService rentalService = new CarRentalService();
    private final CustomerRepository customerRepo = new CustomerRepository();
    private final CarRepository carRepo = new CarRepository();
    private final ObservableList<CarRental> rentalList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private CarRental selectedRental = null; // For editing

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        setupComboBoxes();
        setupTableColumns();
        setupListeners();
        loadAllRentals();
    }

    // ========== SETUP COMBOBOXES ==========
    private void setupComboBoxes() {
        // Load Customers
        List<Customer> customers = customerRepo.findAll();
        cmbCustomer.setItems(FXCollections.observableArrayList(customers));
        cmbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer != null ? customer.getCustomerName() + " - " + customer.getMobile() : "";
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });

        // Load Available Cars
        List<Car> cars = carRepo.findAll();
        cmbCar.setItems(FXCollections.observableArrayList(cars));
        cmbCar.setConverter(new StringConverter<Car>() {
            @Override
            public String toString(Car car) {
                return car != null ? car.getCarName() + " - " + car.getLicensePlate() : "";
            }

            @Override
            public Car fromString(String string) {
                return null;
            }
        });

        // Load Status options
        cmbStatus.setItems(FXCollections.observableArrayList(
                "PENDING", "RENTED", "RETURNED", "COMPLETED", "CANCELLED"
        ));
        cmbStatus.setValue("PENDING");
    }

    // ========== SETUP TABLE COLUMNS ==========
    private void setupTableColumns() {
        colRentalID.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getRentalId()).asObject());

        colCustomer.setCellValueFactory(data -> {
            String customerName = data.getValue().getCustomer() != null
                    ? data.getValue().getCustomer().getCustomerName()
                    : "N/A";
            return new SimpleStringProperty(customerName);
        });

        colCar.setCellValueFactory(data -> {
            String carInfo = data.getValue().getCar() != null
                    ? data.getValue().getCar().getCarName() + " (" + data.getValue().getCar().getLicensePlate() + ")"
                    : "N/A";
            return new SimpleStringProperty(carInfo);
        });

        colPickupDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPickupDate().format(dateFormatter)));

        colReturnDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getReturnDate().format(dateFormatter)));

        colRentPrice.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%,.0f đ", data.getValue().getRentPrice())));

        colStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus()));

        // Custom cell factory for Status with colors
        colStatus.setCellFactory(column -> new TableCell<CarRental, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toUpperCase()) {
                        case "PENDING":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        case "RENTED":
                            setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                            break;
                        case "RETURNED":
                            setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                            break;
                        case "COMPLETED":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "CANCELLED":
                            setStyle("-fx-text-fill: #95a5a6; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: #000000;");
                    }
                }
            }
        });

        rentalTable.setItems(rentalList);
    }

    // ========== SETUP LISTENERS ==========
    private void setupListeners() {
        // Auto-calculate price when dates or car change
        dpPickupDate.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
        dpReturnDate.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
        cmbCar.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());

        // When selecting a row in table, load to form for editing
        rentalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadRentalToForm(newVal);
            }
        });
    }

    // ========== AUTO-CALCULATE PRICE ==========
    private void calculatePrice() {
        LocalDate pickup = dpPickupDate.getValue();
        LocalDate returnDate = dpReturnDate.getValue();
        Car car = cmbCar.getValue();

        if (pickup != null && returnDate != null && car != null && !pickup.isAfter(returnDate)) {
            long days = ChronoUnit.DAYS.between(pickup, returnDate);
            if (days <= 0) days = 1;

            double dailyPrice = car.getRentPrice() != null ? car.getRentPrice() : 0.0;
            double totalPrice = dailyPrice * days;

            txtRentPrice.setText(String.format("%,.0f", totalPrice));
        } else {
            txtRentPrice.setText("");
        }
    }

    // ========== LOAD ALL RENTALS ==========
    private void loadAllRentals() {
        try {
            List<CarRental> rentals = rentalService.getAll();
            rentalList.setAll(rentals);
            System.out.println("✓ Loaded " + rentals.size() + " rentals");
        } catch (Exception e) {
            System.err.println("❌ Error loading rentals: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load rentals: " + e.getMessage());
        }
    }

    // ========== LOAD RENTAL TO FORM ==========
    private void loadRentalToForm(CarRental rental) {
        selectedRental = rental;

        cmbCustomer.setValue(rental.getCustomer());
        cmbCar.setValue(rental.getCar());
        dpPickupDate.setValue(rental.getPickupDate());
        dpReturnDate.setValue(rental.getReturnDate());
        txtRentPrice.setText(String.format("%,.0f", rental.getRentPrice()));
        cmbStatus.setValue(rental.getStatus());

        btnSave.setText("Update Rental");
    }

    // ========== SAVE (ADD OR UPDATE) ==========
    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) return;

        try {
            Customer customer = cmbCustomer.getValue();
            Car car = cmbCar.getValue();
            LocalDate pickupDate = dpPickupDate.getValue();
            LocalDate returnDate = dpReturnDate.getValue();
            String status = cmbStatus.getValue();
            double rentPrice = Double.parseDouble(txtRentPrice.getText().replace(",", ""));

            if (selectedRental == null) {
                // ADD NEW RENTAL
                CarRental newRental = new CarRental();
                newRental.setCustomer(customer);
                newRental.setCar(car);
                newRental.setPickupDate(pickupDate);
                newRental.setReturnDate(returnDate);
                newRental.setRentPrice(rentPrice);
                newRental.setStatus(status);

                rentalService.createRental(newRental);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rental added successfully!");
            } else {
                // UPDATE EXISTING RENTAL
                selectedRental.setCustomer(customer);
                selectedRental.setCar(car);
                selectedRental.setPickupDate(pickupDate);
                selectedRental.setReturnDate(returnDate);
                selectedRental.setRentPrice(rentPrice);
                selectedRental.setStatus(status);

                rentalService.updateRental(selectedRental);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rental updated successfully!");
            }

            loadAllRentals();
            handleClear();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save rental: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== VALIDATE INPUT ==========
    private boolean validateInput() {
        if (cmbCustomer.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a customer.");
            return false;
        }
        if (cmbCar.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a car.");
            return false;
        }
        if (dpPickupDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select pickup date.");
            return false;
        }
        if (dpReturnDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select return date.");
            return false;
        }
        if (dpPickupDate.getValue().isAfter(dpReturnDate.getValue())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Pickup date must be before return date.");
            return false;
        }
        if (cmbStatus.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select status.");
            return false;
        }
        return true;
    }

    // ========== CLEAR FORM ==========
    @FXML
    private void handleClear() {
        selectedRental = null;
        cmbCustomer.setValue(null);
        cmbCar.setValue(null);
        dpPickupDate.setValue(null);
        dpReturnDate.setValue(null);
        txtRentPrice.setText("");
        cmbStatus.setValue("PENDING");
        btnSave.setText("Save Rental");
        rentalTable.getSelectionModel().clearSelection();
    }

    // ========== EDIT ==========
    @FXML
    private void handleEdit() {
        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a rental to edit.");
            return;
        }
        // Already loaded to form by listener
    }

    // ========== DELETE ==========
    @FXML
    private void handleDelete() {
        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a rental to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Rental ID: " + selected.getRentalId());
        confirm.setContentText("Are you sure you want to delete this rental?\n\n" +
                "Customer: " + selected.getCustomer().getCustomerName() + "\n" +
                "Car: " + selected.getCar().getCarName() + "\n" +
                "Pickup Date: " + selected.getPickupDate().format(dateFormatter) + "\n\n" +
                "This action will set the car status back to AVAILABLE.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                rentalService.deleteRental(selected.getRentalId());
                loadAllRentals();
                handleClear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rental deleted successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete rental: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ========== SHOW ALERT ==========
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 