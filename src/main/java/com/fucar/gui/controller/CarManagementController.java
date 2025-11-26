package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Car;
import com.fucar.entity.CarProducer;
import com.fucar.repository.CarProducerRepository;
import com.fucar.service.CarService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CarManagementController {

    private MainApp mainApp;
    private final CarService carService = new CarService();
    private final CarProducerRepository producerRepo = new CarProducerRepository();

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, Integer> colId;
    @FXML
    private TableColumn<Car, String> colName;
    @FXML
    private TableColumn<Car, Integer> colYear;
    @FXML
    private TableColumn<Car, String> colColor;
    @FXML
    private TableColumn<Car, Integer> colCapacity;
    @FXML
    private TableColumn<Car, String> colPrice;

    @FXML
    private Button btnAddCar, btnEditCar, btnDeleteCar;

    private ObservableList<Car> carList = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        // thiết lập columns (giả sử getter tên chuẩn)
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCarID()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCarName()));
        colYear.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCarModelYear()));
        colColor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getColor()));
        colCapacity.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCapacity()));
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getRentalPrice() == null ? "" : data.getValue().getRentalPrice().toString()));

        loadData();

        btnAddCar.setOnAction(e -> onAddCar());
        btnEditCar.setOnAction(e -> onEditCar());
        btnDeleteCar.setOnAction(e -> onDeleteCar());
    }

    private void loadData() {
        List<Car> cars = carService.getAllCars();
        carList.setAll(cars);
        carTable.setItems(carList);
    }

    private void onAddCar() {
        // đơn giản: show input dialog (bạn có thể dùng FXML popup)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Car");
        dialog.setHeaderText("Nhập tên xe (format: name,year,color,capacity,price,producerId)");
        dialog.setContentText("Ví dụ: Toyota Camry,2020,White,5,1200,1");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                Car c = new Car();
                c.setCarName(parts[0].trim());
                c.setCarModelYear(Integer.parseInt(parts[1].trim()));
                c.setColor(parts[2].trim());
                c.setCapacity(Integer.parseInt(parts[3].trim()));
                c.setRentalPrice(new BigDecimal(parts[4].trim()));
                // get producer if provided
                if (parts.length >= 6) {
                    Integer pid = Integer.parseInt(parts[5].trim());
                    producerRepo.findById(pid).ifPresent(c::setProducer);
                }
                carService.createCar(c);
                loadData();
                showInfo("Thêm thành công");
            } catch (Exception ex) {
                showError("Lỗi thêm xe: " + ex.getMessage());
            }
        });
    }

    private void onEditCar() {
        Car selected = carTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Chọn một xe để sửa");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(String.format("%s,%d,%s,%d,%s",
                selected.getCarName(),
                selected.getCarModelYear() == null ? 0 : selected.getCarModelYear(),
                selected.getColor(),
                selected.getCapacity() == null ? 0 : selected.getCapacity(),
                selected.getRentalPrice() == null ? "0" : selected.getRentalPrice().toString()));
        dialog.setTitle("Edit Car");
        dialog.setHeaderText("Sửa thông tin (name,year,color,capacity,price)");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                selected.setCarName(parts[0].trim());
                selected.setCarModelYear(Integer.parseInt(parts[1].trim()));
                selected.setColor(parts[2].trim());
                selected.setCapacity(Integer.parseInt(parts[3].trim()));
                selected.setRentalPrice(new BigDecimal(parts[4].trim()));
                carService.updateCar(selected);
                loadData();
                showInfo("Cập nhật thành công");
            } catch (Exception ex) {
                showError("Lỗi cập nhật: " + ex.getMessage());
            }
        });
    }

    private void onDeleteCar() {
        Car selected = carTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Chọn một xe để xóa");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa xe này?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> opt = confirm.showAndWait();
        if (opt.isPresent() && opt.get() == ButtonType.YES) {
            try {
                carService.deleteCar(selected.getCarID());
                loadData();
                showInfo("Xóa thành công");
            } catch (Exception ex) {
                showError("Không thể xóa: " + ex.getMessage());
            }
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
