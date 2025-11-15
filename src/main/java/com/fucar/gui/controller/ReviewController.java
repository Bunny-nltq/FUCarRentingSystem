package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class ReviewController {

    private MainApp mainApp;

    @FXML
    private TableView<?> reviewTable;

    @FXML
    private Button btnAddReview, btnEditReview, btnDeleteReview;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        btnAddReview.setOnAction(e -> System.out.println("Add Review clicked"));
        btnEditReview.setOnAction(e -> System.out.println("Edit Review clicked"));
        btnDeleteReview.setOnAction(e -> System.out.println("Delete Review clicked"));
    }
}
