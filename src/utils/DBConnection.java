package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bookflow_db"; // Change this to your actual database URL
    private static final String USER = "root"; // Change this to your actual username
    private static final String PASSWORD = "pass"; // Change this to your actual password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); // Establish a connection to the database
    }
}
