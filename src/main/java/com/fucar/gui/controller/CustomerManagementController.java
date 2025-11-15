package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CustomerManagementController {

    private MainApp mainApp;

    @FXML
    private TableView<?> customerTable;

    @FXML
    private Button btnAddCustomer, btnEditCustomer, btnDeleteCustomer;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnAddCustomer.setOnAction(e -> System.out.println("Add Customer clicked"));
        btnEditCustomer.setOnAction(e -> System.out.println("Edit Customer clicked"));
        btnDeleteCustomer.setOnAction(e -> System.out.println("Delete Customer clicked"));
    }
}
