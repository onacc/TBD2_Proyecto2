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
    static String url = "jdbc:mysql://localhost:3306/mini_agenda";
    static String user = "root";
    static String pass = "nuestrapassword";

    // Para MySQL
    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión exitosa");
        } catch (SQLException e) {
            e.printStackTrace(); // Corrección aquí
        }
        return con; // Opcional: para que el método devuelva la conexión
    }
}
