package com.fucar.gui.controller;

import java.time.LocalDate;
import java.util.List;

import com.fucar.entity.Car;
import com.fucar.entity.CarProducer;
import com.fucar.repository.CarProducerRepository;
import com.fucar.service.CarService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCarDialogController {

    @FXML
    private TextField txtCarName;

    @FXML
    private ComboBox<CarProducer> cmbProducer;

    @FXML
    private Spinner<Integer> spnYear;

    @FXML
    private TextField txtColor;

    @FXML
    private Spinner<Integer> spnCapacity;

    @FXML
    private TextField txtLicensePlate;

    @FXML
    private DatePicker dpImportDate;

    @FXML
    private Spinner<Double> spnRentPrice;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnSave, btnCancel;

    private CarService carService;
    private CarProducerRepository producerRepo;
    private Car carToEdit;
    private boolean carAdded = false;

    @FXML
    private void initialize() {
        producerRepo = new CarProducerRepository();

        // Setup year spinner
        spnYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1990, 2030, LocalDate.now().getYear()));

        // Setup capacity spinner
        spnCapacity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 5));

        // Setup rent price spinner
        spnRentPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, 50, 1));

        // Setup status combo
        cmbStatus.setItems(FXCollections.observableArrayList(
                "AVAILABLE",
                "RENTED",
                "MAINTENANCE",
                "RETIRED"
        ));
        cmbStatus.setValue("AVAILABLE");

        // Setup import date
        dpImportDate.setValue(LocalDate.now());

        // Load producers
        loadProducers();

        // Button actions
        btnSave.setOnAction(e -> handleSave());
        btnCancel.setOnAction(e -> handleCancel());
    }

    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    public void setCarToEdit(Car car) {
        this.carToEdit = car;
        populateFields(car);
    }

    public boolean isCarAdded() {
        return carAdded;
    }

    // ===============================
    // LOAD PRODUCERS
    // ===============================
    private void loadProducers() {
        try {
            List<CarProducer> producers = producerRepo.findAll();
            ObservableList<CarProducer> producerList = FXCollections.observableArrayList(producers);
            cmbProducer.setItems(producerList);

            if (!producers.isEmpty()) {
                cmbProducer.setValue(producers.get(0));
            }
        } catch (Exception e) {
            showError("Error loading producers: " + e.getMessage());
        }
    }

    // ===============================
    // POPULATE FIELDS FOR EDITING
    // ===============================
    private void populateFields(Car car) {
        txtCarName.setText(car.getCarName());
        cmbProducer.setValue(car.getProducer());
        spnYear.getValueFactory().setValue(car.getCarModelYear());
        txtColor.setText(car.getColor());
        spnCapacity.getValueFactory().setValue(car.getCapacity());
        txtLicensePlate.setText(car.getLicensePlate());
        dpImportDate.setValue(car.getImportDate());
        spnRentPrice.getValueFactory().setValue(car.getRentPrice());
        cmbStatus.setValue(car.getStatus());
        txtDescription.setText(car.getDescription() != null ? car.getDescription() : "");
    }

    // ===============================
    // SAVE CAR
    // ===============================
    @FXML
    private void handleSave() {
        if (!validateFields()) {
            return;
        }

        try {
            if (carToEdit == null) {
                // Add new car
                Car newCar = new Car();
                newCar.setCarName(txtCarName.getText());
                newCar.setProducer(cmbProducer.getValue());
                newCar.setCarModelYear(spnYear.getValue());
                newCar.setColor(txtColor.getText());
                newCar.setCapacity(spnCapacity.getValue());
                newCar.setLicensePlate(txtLicensePlate.getText());
                newCar.setImportDate(dpImportDate.getValue());
                newCar.setRentPrice(spnRentPrice.getValue());
                newCar.setStatus(cmbStatus.getValue());
                newCar.setDescription(txtDescription.getText());

                carService.save(newCar);
                carAdded = true;
                showSuccess("Car added successfully");
            } else {
                // Edit existing car
                carToEdit.setCarName(txtCarName.getText());
                carToEdit.setProducer(cmbProducer.getValue());
                carToEdit.setCarModelYear(spnYear.getValue());
                carToEdit.setColor(txtColor.getText());
                carToEdit.setCapacity(spnCapacity.getValue());
                carToEdit.setLicensePlate(txtLicensePlate.getText());
                carToEdit.setImportDate(dpImportDate.getValue());
                carToEdit.setRentPrice(spnRentPrice.getValue());
                carToEdit.setStatus(cmbStatus.getValue());
                carToEdit.setDescription(txtDescription.getText());

                carService.update(carToEdit);
                carAdded = true;
                showSuccess("Car updated successfully");
            }

            handleClose();
        } catch (Exception e) {
            showError("Error saving car: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // VALIDATE FIELDS
    // ===============================
    private boolean validateFields() {
        if (txtCarName.getText().trim().isEmpty()) {
            showWarning("Please enter car name");
            return false;
        }

        if (cmbProducer.getValue() == null) {
            showWarning("Please select producer");
            return false;
        }

        if (txtColor.getText().trim().isEmpty()) {
            showWarning("Please enter color");
            return false;
        }

        if (txtLicensePlate.getText().trim().isEmpty()) {
            showWarning("Please enter license plate");
            return false;
        }

        if (spnRentPrice.getValue() <= 0) {
            showWarning("Rent price must be greater than 0");
            return false;
        }

        return true;
    }

    // ===============================
    // UTILITIES
    // ===============================
    @FXML
    private void handleCancel() {
        handleClose();
    }

    private void handleClose() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
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
