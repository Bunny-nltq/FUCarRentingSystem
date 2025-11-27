package com.fucar.browsecars;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.service.CarService;
import com.fucar.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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

    private MainApp mainApp;
    private int loggedCustomerId;
    private CarService carService;
    private ObservableList<Car> carsList;

    // ===============================
    // INITIALIZATION
    // ===============================
    @FXML
    public void initialize() {
        carService = new CarService();
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
    // CREATE CAR CARD
    // ===============================
    private HBox createCarCard(Car car) {
        HBox card = new HBox(20);
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-width: 1;");
        card.setPrefHeight(150);

        // Left side: Car info
        VBox infoBox = new VBox(8);
        infoBox.setPrefWidth(350);

        // Car name
        Label lblCarName = new Label(car.getCarName());
        lblCarName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Car details
        Label lblProducer = new Label("Producer: " + (car.getProducer() != null ? car.getProducer().getProducerName() : "N/A"));
        lblProducer.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label lblYear = new Label("Year: " + car.getCarModelYear());
        lblYear.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label lblColor = new Label("Color: " + car.getColor());
        lblColor.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label lblCapacity = new Label("Capacity: " + car.getCapacity() + " seats");
        lblCapacity.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label lblStatus = new Label("Status: " + car.getStatus());
        String statusColor = "AVAILABLE".equals(car.getStatus()) ? "#27ae60" : "#e74c3c";
        lblStatus.setStyle("-fx-font-size: 12px; -fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(lblCarName, lblProducer, lblYear, lblColor, lblCapacity, lblStatus);

        // Right side: Price and action buttons
        VBox actionBox = new VBox(10);
        actionBox.setStyle("-fx-alignment: CENTER_RIGHT;");
        actionBox.setPrefWidth(200);

        Label lblPrice = new Label("$" + String.format("%.2f", car.getRentPrice()) + " / day");
        lblPrice.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Button btnViewDetails = new Button("View Details");
        btnViewDetails.setStyle("-fx-padding: 8 20; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5;");
        btnViewDetails.setPrefWidth(150);
        btnViewDetails.setOnAction(e -> viewCarDetails(car));

        Button btnRent = new Button("Rent Now");
        btnRent.setStyle("-fx-padding: 8 20; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-border-radius: 5;");
        btnRent.setPrefWidth(150);
        btnRent.setDisable(!car.getStatus().equals("AVAILABLE"));
        btnRent.setOnAction(e -> rentCar(car));

        actionBox.getChildren().addAll(lblPrice, btnViewDetails, btnRent);

        card.getChildren().addAll(infoBox, new Pane(new javafx.scene.control.Separator()));
        card.getChildren().add(actionBox);

        return card;
    }

    // ===============================
    // SEARCH FUNCTIONALITY
    // ===============================
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        String filterType = cmbFilter.getValue();

        List<Car> filteredCars = carsList.stream()
                .filter(car -> car.getCarName().toLowerCase().contains(searchText) ||
                        (car.getProducer() != null && car.getProducer().getProducerName().toLowerCase().contains(searchText)))
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
    // VIEW CAR DETAILS
    // ===============================
    private void viewCarDetails(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CarDetails.fxml"));
            Parent root = loader.load();

            CarDetailsController controller = loader.getController();
            controller.setCar(car);
            controller.setMainApp(mainApp);

            Stage stage = new Stage();
            stage.setTitle("Car Details - " + car.getCarName());
            stage.setScene(new Scene(root, 600, 500));
            stage.showAndWait();
        } catch (Exception e) {
            showError("Error loading car details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===============================
    // RENT CAR
    // ===============================
    private void rentCar(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RentCar.fxml"));
            Parent root = loader.load();

            RentCarController controller = loader.getController();
            controller.setCar(car);
            controller.setCustomerId(loggedCustomerId);
            controller.setMainApp(mainApp);

            Stage stage = new Stage();
            stage.setTitle("Rent Car - " + car.getCarName());
            stage.setScene(new Scene(root, 600, 500));
            stage.showAndWait();

            // Reload cars after rental
            loadCars();
        } catch (Exception e) {
            showError("Error opening rent car dialog: " + e.getMessage());
            e.printStackTrace();
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

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        cmbFilter.setValue("All Cars");
        loadCars();
    }
}
