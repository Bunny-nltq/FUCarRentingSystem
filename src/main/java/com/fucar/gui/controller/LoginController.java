package com.fucar.gui.controller;

import com.fucar.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginController {

    // ----------------- THAM CHIẾU MAINAPP -----------------
    private MainApp mainApp; // dòng bạn muốn thêm

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp; // dòng bạn muốn thêm
    }

    // ----------------- FXML CONTROLS -----------------
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    // ----------------- XỬ LÝ SỰ KIỆN -----------------
    @FXML
    public void handleLogin() {
        System.out.println("Login: " + txtEmail.getText());
        // Ví dụ: chuyển sang dashboard nếu đăng nhập thành công
        // mainApp.showCustomerDashboard();
    }

    @FXML
    public void goRegister() {
        System.out.println("Go to Register");
        mainApp.showRegister(); // mở màn hình Register
    }
}
