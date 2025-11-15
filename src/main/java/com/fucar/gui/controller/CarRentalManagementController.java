package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CarRentalManagementController {

    private MainApp mainApp;

    @FXML
    private TableView<?> rentalTable;

    @FXML
    private Button btnAddRental, btnEditRental, btnDeleteRental;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnAddRental.setOnAction(e -> System.out.println("Add Rental clicked"));
        btnEditRental.setOnAction(e -> System.out.println("Edit Rental clicked"));
        btnDeleteRental.setOnAction(e -> System.out.println("Delete Rental clicked"));
    }
}
