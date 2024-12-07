/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tbd2_project;
import java.sql.*;
/**
 *
 * @author gcano
 */
public class ConectMySQL {

    // Para MySQL
    public static Connection conectar() {
    Connection con = null;
    try {
        con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        System.out.println("Conexión exitosa");
        } catch (SQLException e) {
        e.printStackTrace(); // Corrección aquí
        }
        return con; // Opcional: para que el método devuelva la conexión
    }
    public ConectMySQL() {
    }

    
    // Variables de conexión
    private static final String ORACLE_URL = "jdbc:oracle:thin:@<oracle_host>:<port>:<sid>";
    private static final String ORACLE_USER = "<oracle_user>";
    private static final String ORACLE_PASSWORD = "<oracle_password>";

    private static final String MYSQL_URL = "jdbc:mysql://database-1.cn6ikiuqw4bl.us-east-1.rds.amazonaws.com";
    private static final String MYSQL_USER = "admin";
    private static final String MYSQL_PASSWORD = "12151024";

    // Conexión Oracle
    public static Connection getOracleConnection() throws SQLException {
        try {
            // Cargar el driver de Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se pudo cargar el driver de Oracle.");
            throw new SQLException("No se pudo cargar el driver de Oracle", e);
        }
    }

    // Conexión MySQL
    public static Connection getMySQLConnection() throws SQLException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se pudo cargar el driver de MySQL.");
            throw new SQLException("No se pudo cargar el driver de MySQL", e);
        }
    }
}
