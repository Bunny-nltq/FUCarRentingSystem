package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CarManagementController {

    private MainApp mainApp;

    @FXML
    private TableView<?> carTable;

    @FXML
    private Button btnAddCar, btnEditCar, btnDeleteCar;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnAddCar.setOnAction(e -> System.out.println("Add Car clicked"));
        btnEditCar.setOnAction(e -> System.out.println("Edit Car clicked"));
        btnDeleteCar.setOnAction(e -> System.out.println("Delete Car clicked"));
    }
}
