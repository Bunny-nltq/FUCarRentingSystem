package com.fucar.gui.controller;

import com.fucar.entity.Review;
import com.fucar.service.ReviewService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class ReviewManagementController {

    @FXML private TableView<Review> tableReviews;

    @FXML private TableColumn<Review, Integer> colID;
    @FXML private TableColumn<Review, String> colCustomer;
    @FXML private TableColumn<Review, String> colCar;
    @FXML private TableColumn<Review, Integer> colRating;
    @FXML private TableColumn<Review, String> colComment;
    @FXML private TableColumn<Review, String> colDate;

    @FXML private Button btnDelete, btnRefresh;

    private final ReviewService service = new ReviewService();
    private final ObservableList<Review> reviewList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadReviews();

        btnDelete.setOnAction(e -> deleteReview());
        btnRefresh.setOnAction(e -> loadReviews());
    }

    // ==============================
    // SETUP TABLE COLUMNS
    // ==============================
    private void setupTableColumns() {
        colID.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getReviewID()).asObject()
        );

        colCustomer.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getCustomer() != null ? c.getValue().getCustomer().getCustomerName() : "Unknown"
            )
        );

        colCar.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getCar() != null ? c.getValue().getCar().getCarName() : "Unknown"
            )
        );

        colRating.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getRating()).asObject()
        );

        colComment.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getComment() != null ? c.getValue().getComment() : ""
            )
        );

        colDate.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getCreatedAt() != null ? c.getValue().getCreatedAt().toString() : ""
            )
        );

        tableReviews.setItems(reviewList);
    }

    // ==============================
    // LOAD REVIEWS
    // ==============================
    private void loadReviews() {
        try {
            List<Review> reviews = service.getAllReviews();
            reviewList.setAll(reviews);
            tableReviews.refresh();
        } catch (Exception e) {
            showAlert("Error", "Error loading reviews: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==============================
    // DELETE REVIEW
    // ==============================
    private void deleteReview() {
        Review selected = tableReviews.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a review to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Review");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this review?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.deleteReview(selected.getReviewID());
                reviewList.remove(selected);
                showAlert("Success", "Review deleted successfully.");
            } catch (Exception e) {
                showAlert("Error", "Error deleting review: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ==============================
    // ALERT SUPPORT
    // ==============================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
