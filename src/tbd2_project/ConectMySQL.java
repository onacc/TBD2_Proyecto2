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
    public static Connection conectarMySQL() {
    Connection con = null;
    try {
        con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        System.out.println("Conexión exitosa");
        } catch (SQLException e) {
        e.printStackTrace(); // Corrección aquí
        }
        return con; // Opcional: para que el método devuelva la conexión
    }
    //para oracle
     public static Connection conectarOracle() {
        Connection con = null;
        try {
            
            Class.forName("oracle.jdbc.OracleDriver");

            
            con = DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASSWORD);
            System.out.println("Conexión exitosa a Oracle");
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver de Oracle JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar a Oracle.");
            e.printStackTrace();
        }
        return con;
    }
  
    public ConectMySQL() {
    }

    
    // Variables de conexión
    private static final String ORACLE_URL = "jdbc:oracle:thin:@oracledb.cn6ikiuqw4bl.us-east-1.rds.amazonaws.com:1521:ORCL";
    private static final String ORACLE_USER = "admin";
    private static final String ORACLE_PASSWORD = "12151024";

    private static final String MYSQL_URL = "jdbc:mysql://database-1.cn6ikiuqw4bl.us-east-1.rds.amazonaws.com";
    private static final String MYSQL_USER = "admin";
    private static final String MYSQL_PASSWORD = "12151024";


   
}
