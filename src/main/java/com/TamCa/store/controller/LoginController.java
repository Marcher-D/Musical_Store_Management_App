package com.TamCa.store.controller; // DÙNG TEAM NAME CỦA BRO

import com.TamCa.store.service.InventoryManager; // DÙNG TEAM NAME CỦA BRO
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    private InventoryManager inventoryManager;

    public LoginController() {
        // Khởi tạo Service ngay khi Controller được tạo
        inventoryManager = new InventoryManager(); 
    }

    @FXML
    private void handleLogin(javafx.event.ActionEvent event) {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert("Error", "Please enter username and password.");
            return;
        }

        // Gọi DB kiểm tra
        String role = inventoryManager.checkLogin(user, pass);

        if (role != null) {
            System.out.println("Login Success! Role: " + role);
            // CHUYỂN CẢNH SANG DASHBOARD
            try {
                switchToDashboard(event);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("System Error", "Failed to load Dashboard screen.");
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void switchToDashboard(javafx.event.ActionEvent event) throws IOException {
        // 1. Load Dashboard FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
        Parent root = loader.load();

        // 2. Lấy Stage hiện tại (cái cửa sổ đang hiển thị)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // 3. Tạo Scene mới (1000x700 là size của Dashboard)
        Scene scene = new Scene(root, 1000, 700);
        
        // 4. Nạp CSS (Quan trọng để lấy Gradient và styling chuẩn)
        String css = getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        // 5. Đổi Scene và Show
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
