package com.example.hobby_airsoft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/api";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        /*try{
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new SQLException("No se pudo conectar a la base de datos");
        }
        
        */
        System.out.println("me estan llamando");
        return null;
    }
}
