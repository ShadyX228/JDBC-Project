import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBInfo {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbname;
    private static Connection connection;
    private static Properties properties;

    DBInfo() throws SQLException {
        driver = properties.getProperty("jdbc.driver");
        url = driver + "://" + properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
        dbname = properties.getProperty("jdbc.dbname");
        connection = doConnection();
    }

    static {
        try {
            properties = new Properties();
            FileInputStream input = new FileInputStream("connection.properties");
            properties.load(input);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
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
    public Connection getConnection() {
        return connection;
    }
    public String getDBName() {
        return dbname;
    }
}
