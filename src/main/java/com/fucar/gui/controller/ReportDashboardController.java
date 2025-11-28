package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.CarRental;
import com.fucar.service.CarRentalService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ReportDashboardController {

    private MainApp mainApp;
    private final CarRentalService rentalService = new CarRentalService();

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button btnGenerate;
    @FXML private TableView<CarRental> reportTable;

    public void setMainApp(MainApp app) {
        this.mainApp = app;
    }

    @FXML
    public void initialize() {
        btnGenerate.setOnAction(e -> generateReport());
    }

    private void generateReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start == null || end == null) {
            showAlert("Please select both start and end dates.");
            return;
        }

        List<CarRental> list = rentalService.filterByDate(start, end);
        reportTable.getItems().setAll(list);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}