package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.CarRental;
import com.fucar.service.CarRentalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.fucar.entity.Account;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.converter.DoubleStringConverter; // Cần import này

import java.time.LocalDate;
import java.util.Optional;

public class CustomerRentalsController {

    private MainApp mainApp;
    private int customerId; 
    private Account loggedUser; 

    private final CarRentalService rentalService = new CarRentalService();
    private final ObservableList<CarRental> rentalList = FXCollections.observableArrayList();

    @FXML private TableView<CarRental> rentalTable;
    // Đã chỉnh sửa tên FXID để đồng bộ với FXML mới
    @FXML private TableColumn<CarRental, Integer> colRentalID;
    @FXML private TableColumn<CarRental, Integer> colCustomerID; // THIẾU TRONG CODE CŨ
    @FXML private TableColumn<CarRental, Integer> colCarID;
    @FXML private TableColumn<CarRental, LocalDate> colPickupDate; // Đã đổi sang LocalDate
    @FXML private TableColumn<CarRental, LocalDate> colReturnDate; // Đã đổi sang LocalDate
    @FXML private TableColumn<CarRental, Double> colRentPrice;     // Đã đổi sang Double
    @FXML private TableColumn<CarRental, String> colStatus;
    @FXML private TableColumn<CarRental, String> colCreatedAt;     // THIẾU TRONG CODE CŨ

    @FXML private StackPane mainPane; 

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        // GỌI HÀM LOAD SAU KHI CÓ ID KHÁCH HÀNG
        loadRentals();
    }

    public void setLoggedUser(Account loggedUser) {
        this.loggedUser = loggedUser;
    }

    @FXML
    private void initialize() {
        // =============== ĐỔ DỮ LIỆU DANH SÁCH THUÊ XE =================

        // rentalId Property (Integer)
        colRentalID.setCellValueFactory(c -> c.getValue().rentalIdProperty().asObject()); 
        
        // customerID Property (Integer - Lấy từ Helper method)
        colCustomerID.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCustomerID()).asObject());
        
        // carID Property (Integer - Lấy từ Helper method)
        colCarID.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCarID()).asObject());
        
        // PickupDate Property (LocalDate)
        colPickupDate.setCellValueFactory(c -> c.getValue().startDateProperty()); 
        
        // ReturnDate Property (LocalDate)
        colReturnDate.setCellValueFactory(c -> c.getValue().endDateProperty()); 
        
        // RentPrice Property (Double)
        colRentPrice.setCellValueFactory(c -> c.getValue().rentPriceProperty().asObject());
        // Định dạng giá tiền
        colRentPrice.setCellFactory(column -> new TableCell<CarRental, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f đ", item)); // Định dạng VND
                }
            }
        });
        
        // Status Property (String)
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
        
        // CreatedAt Property (String - Cần getter đơn giản trong Entity)
        // LƯU Ý: Nếu muốn hiển thị LocalDateTime, bạn cần thêm Property getter trong Entity
        colCreatedAt.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCreatedAt().toLocalDate().toString())); 
        
        rentalTable.setItems(rentalList);
    }

    private void loadRentals() {
        // Lấy danh sách thuê xe của khách hàng này
        rentalList.setAll(rentalService.getRentalsByCustomer(customerId));
    }

    @FXML
    private void handleDeleteRental() {
        CarRental selected = rentalTable.getSelectionModel().getSelectedItem();
        // SỬA LỖI: Chỉ cho phép xóa các đơn chưa hoàn thành (ví dụ: PENDING, RENTED)
        if (selected == null) {
            showAlert("Warning", "Vui lòng chọn một đơn thuê để xóa.");
            return;
        }

        // Kiểm tra trạng thái xe
        if ("RETURNED".equalsIgnoreCase(selected.getStatus()) || "COMPLETED".equalsIgnoreCase(selected.getStatus())) {
             showAlert("Error", "Không thể xóa đơn thuê đã hoàn tất.");
             return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa đơn thuê ID: " + selected.getRentalId() + "?\n(Thao tác này sẽ trả xe về trạng thái AVAILABLE)", 
                ButtonType.YES, ButtonType.NO);
        
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            rentalService.deleteRental(selected.getRentalId());
            loadRentals(); // Load lại danh sách
            showAlert("Success", "Đơn thuê ID " + selected.getRentalId() + " đã được xóa thành công.");
        }
    }

    public void handleBack() {
        if (mainApp != null && loggedUser != null) {
            mainApp.showCustomerDashboard(loggedUser);
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}