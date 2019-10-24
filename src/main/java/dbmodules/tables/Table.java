package dbmodules.tables;

import dbmodules.types.TableType;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class Table {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbName;
    private TableType tableName;
    private static Connection connection;
    private static Properties properties;

    Table() throws SQLException, IOException {
        properties = new Properties();
        FileInputStream input = new FileInputStream(
                new File("connection.properties\\")
        );

        properties.load(input);
        driver = properties.getProperty("jdbc.driver");
        url = driver + "://" + properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
        dbName = properties.getProperty("jdbc.dbname");
        connection = doConnection();
    }

    protected void setTableName(TableType tableName) {
        this.tableName = tableName;
    }

    private Connection doConnection()
            throws
            SQLException {
            return DriverManager.getConnection(
                    url,
                    user,
                    password
            );
    }
    protected int setId() throws SQLException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName() + " " +
                "ORDER BY " + getTableName() + "."
                + getTableName() + "_id DESC LIMIT 1";
        //System.out.println(query);
        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();
        if(res.next()) {
            //System.out.println(query + " " + res.getInt(1));
            return res.getInt(1);
        }
        return 0;
    }

    protected Connection getConnection() {
        return connection;
    }
    protected String getDBName() {
        return dbName;
    }
    protected String getTableName() {
        return tableName.getValue();
    }
}