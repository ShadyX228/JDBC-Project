package dbmodules.dao;

import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Table;
import dbmodules.types.Gender;
import dbmodules.types.TableType;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class TableDAO {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbName;
    private TableType tableName;
    private static Connection connection;
    private static Properties properties;

    public TableDAO(TableType tableName) throws IOException, SQLException {
        properties = new Properties();
        InputStream input = getClass().getResourceAsStream("/connection.properties");;

        properties.load(input);
        driver = properties.getProperty("jdbc.driver");
        url = driver + "://" + properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
        dbName = properties.getProperty("jdbc.dbname");
        connection = doConnection();
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
