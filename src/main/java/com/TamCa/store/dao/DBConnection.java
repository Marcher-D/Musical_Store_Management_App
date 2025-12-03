package com.TamCa.store.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// responsible for the connection btw the project and the DB
public class DBConnection {
    //! check the URL, NAME, USER & PASSWORD before using it!
    private static final String BASE_DB_URL = "jdbc:mysql://localhost:3306"; 
    private static final String DATABASE_NAME = "musical_store_db";
    private static final String CONNECTION_PARAMS = "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"; 
    
    private static final String DB_USER = "root";       
    private static final String DB_PASSWORD = "Peter@18122005";

    public static Connection getConnection() throws SQLException {
        String url = BASE_DB_URL + "/" + DATABASE_NAME + CONNECTION_PARAMS; //* building the url
        try {
            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD); //* create a connection with the database
        } 
        catch (SQLException e) {
            System.err.println("Error connecting to the Database: " + e.getMessage());
            throw e;
        }
    }
}