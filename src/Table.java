import java.io.FileInputStream;
import java.io.IOException;
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
                "connection.properties"
        );

        properties.load(input);
        driver = properties.getProperty("jdbc.driver");
        url = driver + "://" + properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
        dbName = properties.getProperty("jdbc.dbname");
        connection = doConnection();
    }

    public void setTableName(TableType tableName) {
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
        String query = "SELECT * FROM " + getDBName() + "." + getTableName() + " " +
                "ORDER BY " + getTableName() + "." + getTableName() + "_id DESC LIMIT 1";
        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();
        if(res.next()) {
            return res.getInt(1);
        }
        return 0;
    }

    public Connection getConnection() {
        return connection;
    }
    public String getDBName() {
        return dbName;
    }
    public String getTableName() {
        return tableName.getValue();
    }
}