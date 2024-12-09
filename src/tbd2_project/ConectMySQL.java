/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tbd2_project;
import java.awt.List;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author gcano
 */
public class ConectMySQL {

   
    
      
    private String host;
    private String dbName;
    private String user;
    private String password;
    private String port;
    private String URL;
    public Connection conexion;
    public Statement query;
    public ArrayList<String> tables = new ArrayList();
    
    public ConectMySQL() {
    }
    
    public ConectMySQL(String host, String dbName, String user, String password, String port) {
        this.host = host;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        this.port = port;
        
    }

    public ConectMySQL(String user, String password, String port) {
        this.user = user;
        this.password = password;
        this.port = port;
    }
    

    public String getDbName() {
        return dbName;
    }

    public String getPort() {
        return port;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    
    public void conectar(){
 
            try {
                if (port.equals("3306")) { // MySQL
                    String url = "jdbc:mysql://" + this.dbName + ":" + this.port + "/" + this.dbName + 
                                 "?useSSL=false&serverTimezone=UTC";
                                URL = "jdbc:mysql://database-1.cn6ikiuqw4bl.us-east-1.rds.amazonaws.com";
                                this.dbName = "SQL";
                    conexion = DriverManager.getConnection(URL, this.user, this.password);
                    System.out.println("conectado a mysql");
                } else if (port.equals("1521")) { // Oracle
                    String url = "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.dbName;
                                URL = "jdbc:oracle:thin:@oracledb.cn6ikiuqw4bl.us-east-1.rds.amazonaws.com:1521:ORCL";
                                this.dbName = "ORACLE";
                    conexion = DriverManager.getConnection(URL, this.user, this.password);
                    System.out.println("conectado a oracle");
                } else {
                    throw new SQLException("Puerto no reconocido: " + port);
                }
                query = conexion.createStatement();
                // System.out.println("Conexión exitosa!");
            } catch (SQLException e) {
                e.printStackTrace();
    
}

    }
    
    public void desconectar(){
        try{
            query.close();
            conexion.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> getTables() {
    return tables; 
}

    //
    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }

    public ArrayList<String> getTablas() {
        ArrayList<String> tablas = new ArrayList<>();
        String queryTablas = "";
        tables = new ArrayList();
        try {
            // If the connection is established and valid, proceed with querying
            if (conexion != null && !conexion.isClosed()) {
                // Explicitly select the database for MySQL
                if (port.equals("3306")) { // MySQL
                    if (conexion.getCatalog() == null || conexion.getCatalog().isEmpty()) {
                        Statement stmt = conexion.createStatement();
                        stmt.execute("USE `" + dbName + "`"); // Explicitly select the database with backticks
                    }
                    queryTablas = "SHOW TABLES"; // Query for MySQL
                } else if (port.equals("1521")) { // Oracle
                    queryTablas = "SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = USER"; // Query for Oracle
                } else {
                    throw new SQLException("Puerto no reconocido para recuperación de tablas: " + port);
                }
                
                // Execute the query to retrieve table names
                ResultSet resultSet = query.executeQuery(queryTablas);
                
                while (resultSet.next()) {
                    String tableName = resultSet.getString(1); // Get table name
                    tablas.add(tableName);
                    tables.add(tableName);
                    System.out.println("Tabla: " + tableName);
                    
                    // To print rows of the table
                    Statement stmt = conexion.createStatement();
                    ResultSet tuplas = stmt.executeQuery("SELECT * FROM " + tableName);
                    while (tuplas.next()) {
                        int columnCount = tuplas.getMetaData().getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(tuplas.getString(i) + " ");
                        }
                        System.out.println();
                    }
                    tuplas.close();
                }
                resultSet.close();
            } else {
                throw new SQLException("La conexión no es válida o está cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tablas; // Return the list of tables
    }




   
}
