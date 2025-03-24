/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ROG STRIX
 */
public class MySQL {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "@JaniduChamika2001";
    private static final String DATABASE = "luxurioz";
    private static Connection connection;

    private static Statement getConnection() throws Exception {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DATABASE, USERNAME, PASSWORD);
        }
        return connection.createStatement();

    }

    public static void iud(String query) {
        try {
            getConnection().executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet search(String query) throws Exception {
        return getConnection().executeQuery(query);
    }
}
