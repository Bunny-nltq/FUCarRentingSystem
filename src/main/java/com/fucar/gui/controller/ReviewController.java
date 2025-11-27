package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.entity.Customer;
import com.fucar.entity.Review;
import com.fucar.service.CarService;
import com.fucar.service.CustomerService;
import com.fucar.service.ReviewService;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ReviewController {

    @FXML private ComboBox<Car> cbCars;
    @FXML private Slider sliderRating;
    @FXML private TextArea txtComment;

    private MainApp mainApp;
    private int customerId;

    private final CarService carService = new CarService();
    private final CustomerService customerService = new CustomerService();
    private final ReviewService reviewService = new ReviewService();

    // ================================
    // Set MainApp
    // ================================
    public void setMainApp(MainApp app) {
        this.mainApp = app;
    }

    // ================================
    // Set Customer ID (được gọi từ login)
    // ================================
    public void setCustomerId(int id) {
        this.customerId = id;
        loadCars();
    }

    // ================================
    // LOAD CAR LIST
    // ================================
    private void loadCars() {
        List<Car> cars = carService.getAllCars();
        cbCars.getItems().setAll(cars);

        cbCars.setPromptText("Select a car...");
    }

    // ================================
    // SUBMIT REVIEW (SAVE TO DB)
    // ================================
    @FXML
    private void submitReview() {

        // 1. Validate car selection
        Car car = cbCars.getValue();
        if (car == null) {
            showAlert("Please select a car.");
            return;
        }

        // 2. Validate rating
        int rating = (int) sliderRating.getValue();
        if (rating < 1 || rating > 5) {
            showAlert("Rating must be between 1 and 5.");
            return;
        }

        // 3. Validate comment
        String comment = txtComment.getText().trim();
        if (comment.isEmpty()) {
            showAlert("Please enter a comment.");
            return;
        }

        // 4. Lấy customer theo ID
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            showAlert("Customer not found.");
            return;
        }

        // 5. Tạo Review entity đúng chuẩn
        Review review = new Review();
        review.setCustomer(customer);
        review.setCar(car);
        review.setReviewStar(rating);  // vẫn dùng reviewStar trong DB
        review.setComment(comment);

        // 6. Lưu vào database
        reviewService.addReview(review);

        showAlert("Review submitted successfully!");

        // Clear text area after submit
        txtComment.clear();
        sliderRating.setValue(3); // reset rating to neutral
        cbCars.setValue(null);    // clear combo selection
    }

    // ================================
    // Alert popup
    // ================================
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
