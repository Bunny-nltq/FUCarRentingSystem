package com.fucar.browsecars;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CarDetailsController {

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

    private Car car;
    private MainApp mainApp;

    public void setCar(Car car) {
        this.car = car;
        displayCarDetails();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void displayCarDetails() {
        if (car == null) return;

        lblCarName.setText(car.getCarName());
        lblProducer.setText(car.getProducer() != null ? car.getProducer().getProducerName() : "N/A");
        lblYear.setText(String.valueOf(car.getCarModelYear()));
        lblColor.setText(car.getColor());
        lblCapacity.setText(car.getCapacity() + " seats");
        lblLicensePlate.setText(car.getLicensePlate());
        lblImportDate.setText(car.getImportDate() != null ? car.getImportDate().toString() : "N/A");
        lblPrice.setText("$" + String.format("%.2f", car.getRentPrice()) + " / day");
        txtDescription.setText(car.getDescription() != null ? car.getDescription() : "No description available");

        String statusColor = "AVAILABLE".equals(car.getStatus()) ? "#27ae60" : "#e74c3c";
        lblStatus.setText(car.getStatus());
        lblStatus.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) lblCarName.getScene().getWindow();
        stage.close();
    }
}
