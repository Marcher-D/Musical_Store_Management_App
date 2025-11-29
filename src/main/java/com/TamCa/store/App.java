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
    
    // Database connection constants used for the test method
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";       
    private static final String DB_PASSWORD = "Peter@18122005"; // *** REPLACE WITH YOUR ACTUAL PASSWORD ***
    private static final String DATABASE_NAME = "musical_store_db";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/Login.fxml"));
        Parent root = fxmlLoader.load();

        // Size for the Login screen
        Scene scene = new Scene(root, 800, 500); 

        // Load CSS for styling
        String css = this.getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Login - Musical Store Management");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Independent method to test MySQL connection.
     * @return true if connection is successful, false if it fails.
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
        // Run connection test before starting JavaFX
        testDbConnection(); 
        
        launch();
    }
}