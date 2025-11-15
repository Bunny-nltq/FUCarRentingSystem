package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.entity.Customer;
import com.fucar.entity.Review;
import com.fucar.service.ReviewService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewController {

    private MainApp mainApp;
    private final ReviewService reviewService = new ReviewService();

    private Car selectedCar;                // xe đang được xem review
    private Customer currentCustomer;       // khách hàng đang login

    @FXML
    private TableView<Review> reviewTable;

    @FXML
    private TableColumn<Review, Integer> colStar;

    @FXML
    private TableColumn<Review, String> colComment;

    @FXML
    private TableColumn<Review, LocalDateTime> colCreatedAt;

    @FXML
    private Button btnAddReview, btnEditReview, btnDeleteReview;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /** Gán xe và tải review */
    public void setCar(Car car) {
        this.selectedCar = car;
        loadReviews();
    }

    /** Nếu bạn có chức năng đăng nhập thì set user vào đây */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    @FXML
    private void initialize() {
        setupColumns();

        btnAddReview.setOnAction(e -> onAddReview());
        btnEditReview.setOnAction(e -> onEditReview());
        btnDeleteReview.setOnAction(e -> onDeleteReview());
    }

    /** Setup TableView Columns */
    private void setupColumns() {
        colStar.setCellValueFactory(new PropertyValueFactory<>("reviewStar"));
        colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    /** Load review theo CarID */
    private void loadReviews() {
        if (selectedCar == null) return;

        List<Review> list = reviewService.getReviewsByCar(selectedCar.getCarID());
        reviewTable.setItems(FXCollections.observableArrayList(list));
    }

    // ============================================================
    //  BUTTON EVENT HANDLERS
    // ============================================================

    /** Thêm Review */
    private void onAddReview() {
        if (selectedCar == null || currentCustomer == null) {
            alert("Thiếu thông tin xe hoặc khách hàng.");
            return;
        }

        Dialog<Review> dialog = createReviewDialog(null);

        dialog.showAndWait().ifPresent(review -> {
            reviewService.addReview(review);
            loadReviews();
            info("Đánh giá thành công!");
        });
    }

    /** Sửa Review */
    private void onEditReview() {
        Review selected = reviewTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            alert("Vui lòng chọn review cần sửa!");
            return;
        }

        Dialog<Review> dialog = createReviewDialog(selected);

        dialog.showAndWait().ifPresent(updatedReview -> {
            updatedReview.setReviewID(selected.getReviewID()); // giữ ID
            reviewService.addReview(updatedReview);
            loadReviews();
            info("Cập nhật thành công!");
        });
    }

    /** Xóa Review */
    private void onDeleteReview() {
        Review selected = reviewTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Vui lòng chọn review cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn muốn xóa review này?", ButtonType.OK, ButtonType.CANCEL);

        if (confirm.showAndWait().get() == ButtonType.OK) {
            reviewService.delete(selected.getReviewID());
            loadReviews();
            info("Xóa thành công!");
        }
    }

    // ============================================================
    //  DIALOG TẠO + SỬA REVIEW
    // ============================================================

    private Dialog<Review> createReviewDialog(Review oldReview) {
        Dialog<Review> dialog = new Dialog<>();
        dialog.setTitle(oldReview == null ? "Thêm Review" : "Sửa Review");

        ButtonType saveBtn = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        Spinner<Integer> starInput = new Spinner<>(1, 5, oldReview != null ? oldReview.getReviewStar() : 5);
        TextArea commentInput = new TextArea(oldReview != null ? oldReview.getComment() : "");
        commentInput.setPromptText("Nhập đánh giá...");

        dialog.getDialogPane().setContent(new VBox(10,
                new Label("Chọn số sao (1–5):"), starInput,
                new Label("Bình luận:"), commentInput
        ));

        dialog.setResultConverter(bt -> {
            if (bt == saveBtn) {
                Review r = new Review(
                        currentCustomer,
                        selectedCar,
                        starInput.getValue(),
                        commentInput.getText()
                );
                if (oldReview != null)
                    r.setReviewID(oldReview.getReviewID());

                return r;
            }
            return null;
        });

        return dialog;
    }

    // ============================================================
    //  UTILITIES
    // ============================================================

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg);
        a.show();
    }

    private void info(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
