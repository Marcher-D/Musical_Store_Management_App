package com.TamCa.store;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App extends Application {

    private static Scene scene;
    
    // Khai báo lại các hằng số kết nối DB để sử dụng trong phương thức test
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";       
    private static final String DB_PASSWORD = "passcode"; // *** THAY THẾ BẰNG MẬT KHẨU THỰC TẾ CỦA BẠN ***
    private static final String DATABASE_NAME = "musical_store_db";


    @Override
    public void start(Stage stage) throws IOException {
        // ĐỔI FILE DASHBOARD THÀNH LOGIN
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/Login.fxml"));
        Parent root = fxmlLoader.load();

        // Size cho màn hình Login
        Scene scene = new Scene(root, 800, 500); 

        // Nạp CSS (vẫn cần để styling cho Login.fxml)
        String css = this.getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Login - Musical Store Management");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Phương thức độc lập để kiểm tra kết nối MySQL.
     * @return true nếu kết nối thành công, false nếu thất bại.
     */
    public static boolean testDbConnection() {
        System.out.println("--- Connecting to Database ---");
        
        try (Connection conn = DriverManager.getConnection(
            DB_URL + DATABASE_NAME, DB_USER, DB_PASSWORD)) {
            
            System.out.println("SUCCESSFULLY CONNECTED!");
            return true;
            
        } catch (SQLException e) {
            System.err.println("FAILED TO CONNECT TO DATABASE!");
            System.err.println("ERROR DETAILED:");
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("--- FINISHED CHECKING DATABASE CONNECTION ---\n");
        }
    }


    public static void main(String[] args){
        // Chạy thử nghiệm kết nối trước khi khởi động JavaFX
        testDbConnection(); 
        
        launch();
    }
}