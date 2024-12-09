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
                    if (!tableName.equalsIgnoreCase("bitacora")) {
                        tables.add(tableName);
                    }

                    
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

//
  /* public void transferTables(ConectMySQL origin, ConectMySQL destination, ArrayList<String> tables) {
    try {
        for (String tableName : tables) {
            System.out.println("Transferring table: " + tableName);

            // Detect origin and destination database types
            boolean isOriginMySQL = origin.port.equals("3306");
            boolean isDestinationMySQL = destination.port.equals("3306");

            // Get table structure from origin database
            String createTableQuery = isOriginMySQL
                ? getMySQLTableSchema(origin, tableName)
                : getOracleTableSchema(origin, tableName);

            // Adapt schema for destination database
            if (!isDestinationMySQL) {
                createTableQuery = adaptSchemaForOracle(createTableQuery);
            } else if (!isOriginMySQL) {
                createTableQuery = adaptSchemaForMySQL(createTableQuery);
            }

            // Execute CREATE TABLE in destination database
            Statement destStmt = destination.conexion.createStatement();
            destStmt.executeUpdate(createTableQuery);

            // Transfer table data
            ResultSet data = getDataFromTable(origin, tableName);
            transferTableData(data, tableName, destination);

            System.out.println("Table " + tableName + " transferred successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}*/
    public void transferTables(ConectMySQL origin, ConectMySQL destination, ArrayList<String> tables) {
    try {
        // Ensure the correct databases are selected for MySQL
        selectDatabaseIfMySQL(origin);
        selectDatabaseIfMySQL(destination);

        for (String tableName : tables) {
            System.out.println("Transferring table: " + tableName);

            // Detect origin and destination database types
            boolean isOriginMySQL = origin.port.equals("3306");
            boolean isDestinationMySQL = destination.port.equals("3306");

            // Get table structure from origin database
            String createTableQuery = isOriginMySQL
                ? getMySQLTableSchema(origin, tableName)
                : getOracleTableSchema(origin, tableName);

            // Adapt schema for destination database
            if (!isDestinationMySQL) {
                createTableQuery = adaptSchemaForOracle(createTableQuery);
            } else if (!isOriginMySQL) {
                createTableQuery = adaptSchemaForMySQL(createTableQuery);
            }

            // Execute CREATE TABLE in destination database
            Statement destStmt = destination.conexion.createStatement();
            destStmt.executeUpdate(createTableQuery);

            // Transfer table data
            ResultSet data = getDataFromTable(origin, tableName);
            transferTableData(data, tableName, destination);

            System.out.println("Table " + tableName + " transferred successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private void selectDatabaseIfMySQL(ConectMySQL dbConnection) throws SQLException {
    if (dbConnection.port.equals("3306")) { // MySQL
        String dbName = dbConnection.getDbName();
        if (dbName != null && !dbName.isEmpty()) {
            Statement stmt = dbConnection.conexion.createStatement();
            stmt.execute("USE `" + dbName + "`");
            System.out.println("Database selected: " + dbName);
        } else {
            throw new SQLException("Database name is not specified for MySQL connection.");
        }
    }
}

private String adaptSchemaForOracle(String createTableQuery) {
    // Replace MySQL types with Oracle-compatible types
    createTableQuery = createTableQuery.replace("AUTO_INCREMENT", "GENERATED BY DEFAULT AS IDENTITY")
                                       .replace("INT", "NUMBER")
                                       .replace("DECIMAL", "NUMBER")
                                       .replace("DATETIME", "DATE")
                                       .replace("TEXT", "CLOB")
                                       .replace("VARCHAR", "VARCHAR2");

    // Replace backticks with double quotes for Oracle
    createTableQuery = createTableQuery.replace("`", "\"");

    // Remove MySQL-specific options
    createTableQuery = createTableQuery.replaceAll("ENGINE=InnoDB", "")
                                       .replaceAll("DEFAULT CHARSET=[^ ]+", "")
                                       .replaceAll("COLLATE=[^ ]+", "");

    // Remove any trailing commas before the closing parenthesis
    if (createTableQuery.endsWith(",")) {
        createTableQuery = createTableQuery.substring(0, createTableQuery.length() - 1);
    }

    // Ensure there's no extra comma before the closing parenthesis
    createTableQuery = createTableQuery.replaceAll(",\\s*\\)", ")");

    // Fix the placement of GENERATED BY DEFAULT AS IDENTITY correctly in the column definition
    createTableQuery = createTableQuery.replace("int NOT NULL GENERATED BY DEFAULT AS IDENTITY", "NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL");

    // Remove redundant GENERATED BY DEFAULT AS IDENTITY outside the column definitions
    createTableQuery = createTableQuery.replace(") GENERATED BY DEFAULT AS IDENTITY", ")");

    // Remove invalid identity assignment (e.g., GENERATED BY DEFAULT AS IDENTITY=3)
    createTableQuery = createTableQuery.replace("GENERATED BY DEFAULT AS IDENTITY=3", "GENERATED BY DEFAULT AS IDENTITY");

    // Handle VARCHAR2 correctly for Oracle
    createTableQuery = createTableQuery.replace("VARCHAR(50)", "VARCHAR2(50)").replace("VARCHAR(100)", "VARCHAR2(100)");

    // Fix the UNIQUE constraint formatting
    createTableQuery = createTableQuery.replace("UNIQUE KEY", "CONSTRAINT \"correo_unique\" UNIQUE");

    // Print the query to debug
    System.out.println("CREATE TABLE query: " + createTableQuery);
    return createTableQuery;
}
private String getMySQLTableSchema(ConectMySQL origin, String tableName) throws SQLException {
    Statement stmt = origin.conexion.createStatement();
    ResultSet resultSet = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
    if (resultSet.next()) {
        return resultSet.getString(2); // Return the CREATE TABLE statement
    }
    throw new SQLException("Failed to retrieve schema for table: " + tableName);
}

private String getOracleTableSchema(ConectMySQL origin, String tableName) throws SQLException {
    StringBuilder createQuery = new StringBuilder("CREATE TABLE " + tableName + " (");
    Statement stmt = origin.conexion.createStatement();
    ResultSet columns = stmt.executeQuery(
        "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH " +
        "FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'"
    );

    while (columns.next()) {
        String columnName = columns.getString("COLUMN_NAME");
        String dataType = columns.getString("DATA_TYPE");
        int dataLength = columns.getInt("DATA_LENGTH");
        createQuery.append(columnName).append(" ").append(dataType);
        if (dataType.equalsIgnoreCase("VARCHAR2") || dataType.equalsIgnoreCase("CHAR")) {
            createQuery.append("(").append(dataLength).append(")");
        }
        createQuery.append(",");
    }
    columns.close();
    createQuery.deleteCharAt(createQuery.length() - 1); // Remove trailing comma
    createQuery.append(")");
    return createQuery.toString();
}






















private String adaptSchemaForMySQL(String createTableQuery) {
    // Replace Oracle-specific datatypes with MySQL-compatible datatypes
    return createTableQuery.replace("NUMBER", "INT")
                           .replace("VARCHAR2", "VARCHAR")
                           .replace("CHAR", "CHAR")
                           .replace("DATE", "DATETIME")
                           .replace("CLOB", "TEXT")
                           .replace("BLOB", "LONGBLOB");
}


private ResultSet getDataFromTable(ConectMySQL origin, String tableName) throws SQLException {
    Statement stmt = origin.conexion.createStatement();
    return stmt.executeQuery("SELECT * FROM " + tableName);
}

private void transferTableData(ResultSet data, String tableName, ConectMySQL destination) throws SQLException {
    int columnCount = data.getMetaData().getColumnCount();
    StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

    for (int i = 0; i < columnCount; i++) {
        insertQuery.append("?,");
    }
    insertQuery.deleteCharAt(insertQuery.length() - 1);
    insertQuery.append(")");

    PreparedStatement insertStmt = destination.conexion.prepareStatement(insertQuery.toString());

    while (data.next()) {
        for (int i = 1; i <= columnCount; i++) {
            insertStmt.setObject(i, data.getObject(i));
        }
        insertStmt.executeUpdate();
    }
}


   
}
