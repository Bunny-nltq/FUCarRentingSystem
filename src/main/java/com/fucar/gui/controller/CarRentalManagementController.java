package com.fucar.gui.controller;

import com.fucar.MainApp;
import com.fucar.entity.Account;
import com.fucar.entity.CarRental;
import com.fucar.entity.Customer;
import com.fucar.service.CarRentalService;
import com.fucar.service.CustomerService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CarRentalManagementController {

    @FXML private TableView<CarRental> tableRentals;
    @FXML private TableColumn<CarRental, String> colCarName;
    @FXML private TableColumn<CarRental, String> colStartDate;
    @FXML private TableColumn<CarRental, String> colEndDate;
    @FXML private TableColumn<CarRental, String> colStatus;
    @FXML private TableColumn<CarRental, Double> colCost;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnBack;

    
    CustomerService  customerService = new CustomerService();
    private Account loggedUser;  // Lưu thông tin Account hiện tại
//    private Customer customer ;
    private MainApp mainApp;     // Tham chiếu MainApp
    private final CarRentalService service = new CarRentalService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Setter MainApp
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // Setter Account và load dữ liệu
    public void setLoggedUser(Account loggedUser) {
        this.loggedUser = loggedUser;
        loadData();
    }
    
 
    // Thêm method này để MainApp gọi được
    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
        loadRentalsForAccount(); // ví dụ load dữ liệu thuê xe
    }

    private void loadRentalsForAccount() {
    	 if (accountID == null) return;
    	    List<CarRental> rentals = service.getRentalsByAccount(accountID);
    	    tableRentals.setItems(FXCollections.observableArrayList(rentals));
    }

    @FXML
    private void initialize() {
        // Mapping columns
        colCarName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getCarName()));
        colStartDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPickupDate().format(dateFormatter)));
        colEndDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReturnDate().format(dateFormatter)));

        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getStatus()));
        colStatus.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equalsIgnoreCase("RENTED")) setTextFill(Color.RED);
                    else if (status.equalsIgnoreCase("AVAILABLE")) setTextFill(Color.GREEN);
                    else setTextFill(Color.BLACK);
                }
            }
        });

        colCost.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getRentPrice()));
        colCost.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double cost, boolean empty) {
                super.updateItem(cost, empty);
                if (empty || cost == null) setText(null);
                else setText(String.format("$%,.2f", cost));
            }
        });

        // Button actions
        btnBack.setOnAction(e -> goBack());
        btnDelete.setOnAction(e -> deleteRental());
        btnEdit.setOnAction(e -> editRental());
        
     
    }
      
    
    // Load danh sách rentals của người dùng
//    public void loadData() {
//        if (loggedUser == null) return;
//        List<CarRental> rentals = service.getRentalsByAccount(loggedUser.getAccountID());
//        tableRentals.setItems(FXCollections.observableArrayList(rentals));
//    }
    
    
    public void loadData() {
        if (loggedUser == null) return;
        
        
        // 1. Tìm Customer tương ứng với AccountID
        Customer customer = customerService.findByAccountId(loggedUser.getAccountID());
        
        // 2. Nếu Customer tồn tại thì lấy CarRental
        if (customer != null) {
            List<CarRental> rentals = service.getRentalsByAccount(customer.getCustomerId());
            tableRentals.setItems(FXCollections.observableArrayList(rentals));
        } else {
            tableRentals.setItems(FXCollections.observableArrayList());
        }
    }
 
    


    
    

    // Quay về Customer Dashboard
    public void goBack() {
        if (mainApp != null && loggedUser != null) {
            mainApp.showCustomerDashboard(loggedUser);
        }
    }

    // Xóa rental
    public void deleteRental() {
        CarRental selected = tableRentals.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.deleteRental(selected.getRentalID());
                tableRentals.getItems().remove(selected);
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Rental deleted successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete rental: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No selection", "Please select a rental to delete.");
        }
    }

    // Sửa rental
    public void editRental() {
        CarRental selected = tableRentals.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAlert(Alert.AlertType.INFORMATION, "Edit", "Edit rental feature coming soon.");
            // TODO: mở EditRental.fxml, truyền selected vào
        } else {
            showAlert(Alert.AlertType.WARNING, "No selection", "Please select a rental to edit.");
        }
    }

    // Hiển thị Alert
    public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public Integer accountID;

  
  

}
