package com.TamCa.store;

import com.TamCa.store.dao.DBConnection; // Import class kết nối mới
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/Login.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 800, 500); 

        // load CSS for styling
        String css = this.getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Login - Musical Store Management");
        stage.setScene(scene);
        stage.show();
    }

    // testing the connection as start
    public static boolean testDbConnection() {
        System.out.println("--- Connecting to Database ---");
        
        try (Connection conn = DBConnection.getConnection()) {
            
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
        testDbConnection(); 
        launch();
    }
}