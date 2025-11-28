package com.fucar.gui.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.service.CarService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CarManagementController {

    private MainApp mainApp;
    private CarService carService;
    private ObservableList<Car> carList;

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, Integer> colId;

    @FXML
    private TableColumn<Car, String> colName;

    @FXML
    private TableColumn<Car, Integer> colYear;

    @FXML
    private TableColumn<Car, String> colColor;

    @FXML
    private TableColumn<Car, Integer> colCapacity;

    @FXML
    private TableColumn<Car, String> colStatus;

    @FXML
    private TableColumn<Car, Double> colPrice;

    @FXML
    private Button btnAddCar, btnEditCar, btnDeleteCar, btnRefresh;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        carService = new CarService();
        carList = FXCollections.observableArrayList();

        // Setup table columns
        colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getCarID()));
        colName.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getCarName()));
        colYear.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getCarModelYear()));
        colColor.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getColor()));
        colCapacity.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getCapacity()));
        colStatus.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getStatus()));
        colPrice.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getRentPrice()));

        carTable.setItems(carList);

        // Button actions
        btnAddCar.setOnAction(e -> handleAddCar());
        btnEditCar.setOnAction(e -> handleEditCar());
        btnDeleteCar.setOnAction(e -> handleDeleteCar());
        btnRefresh.setOnAction(e -> loadCars());

        // Load cars on startup
        loadCars();
    }

    // ===============================
    // LOAD CARS FROM DATABASE
    // ===============================
    private void loadCars() {
        try {
            List<Car> cars = carService.getAllCars();
            carList.setAll(cars);
        } catch (Exception e) {
            showError("Error loading cars: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // ADD CAR
    // ===============================
    private void handleAddCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCarDialog.fxml"));
            Parent root = loader.load();

            AddCarDialogController controller = loader.getController();
            controller.setCarService(carService);

            Stage stage = new Stage();
            stage.setTitle("Add New Car");
            stage.setScene(new Scene(root, 600, 500));
            stage.showAndWait();

            // Refresh table if car was added
            if (controller.isCarAdded()) {
                loadCars();
            }
        } catch (IOException e) {
            showError("Error opening Add Car dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // EDIT CAR
    // ===============================
    private void handleEditCar() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showWarning("Please select a car to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCarDialog.fxml"));
            Parent root = loader.load();

            AddCarDialogController controller = loader.getController();
            controller.setCarService(carService);
            controller.setCarToEdit(selectedCar);

            Stage stage = new Stage();
            stage.setTitle("Edit Car");
            stage.setScene(new Scene(root, 600, 500));
            stage.showAndWait();

            // Refresh table
            loadCars();
        } catch (IOException e) {
            showError("Error opening Edit Car dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // DELETE CAR
    // ===============================
    private void handleDeleteCar() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showWarning("Please select a car to delete");
            return;
        }

        // Check if car can be deleted
        if (!carService.canDeleteCar(selectedCar.getCarID())) {
            // Car is in rental transactions - can only update status
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cannot Delete Car");
            alert.setHeaderText("Car is in Rental Transactions");
            alert.setContentText(
                    "This car is involved in rental transactions and cannot be deleted.\n" +
                    "You can only update its status to 'MAINTENANCE' or 'RETIRED'.\n\n" +
                    "Current status: " + selectedCar.getStatus()
            );
            alert.showAndWait();

            // Offer to change status
            showStatusChangeDialog(selectedCar);
            return;
        }

        // Car can be deleted - ask for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Car");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete '" + selectedCar.getCarName() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                carService.deleteCar(selectedCar.getCarID());
                showSuccess("Car deleted successfully");
                loadCars();
            } catch (Exception e) {
                showError("Error deleting car: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ===============================
    // CHANGE CAR STATUS
    // ===============================
    private void showStatusChangeDialog(Car car) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Car Status");
        alert.setHeaderText("Select New Status");

        // Create combo box for status selection
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(
                "AVAILABLE",
                "RENTED",
                "MAINTENANCE",
                "RETIRED"
        ));
        comboBox.setValue(car.getStatus());

        alert.getDialogPane().setContent(comboBox);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String newStatus = comboBox.getValue();
                car.setStatus(newStatus);
                carService.update(car);
                showSuccess("Car status updated to: " + newStatus);
                loadCars();
            } catch (Exception e) {
                showError("Error updating car status: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
