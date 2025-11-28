package com.fucar.gui.controller;

import com.fucar.entity.Review;
import com.fucar.service.ReviewService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReviewManagementController {

    @FXML private TableView<Review> tableReviews;

    @FXML private TableColumn<Review, Integer> colID;
    @FXML private TableColumn<Review, Integer> colCustomer;
    @FXML private TableColumn<Review, Integer> colCar;
    @FXML private TableColumn<Review, Integer> colRating;
    @FXML private TableColumn<Review, String> colComment;
    @FXML private TableColumn<Review, Object> colDate;

    private final ReviewService service = new ReviewService();

    @FXML
    public void initialize() {

        colID.setCellValueFactory(new PropertyValueFactory<>("reviewID"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colCar.setCellValueFactory(new PropertyValueFactory<>("carID"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        tableReviews.getItems().setAll(service.getAllReviews());
    }
}