import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Table {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbname;
    private Connection connection;
    private static Properties properties;

    Table() throws SQLException {
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

    protected final void doInsertQuery(PreparedStatement statement)
            throws
            SQLException {
        statement.executeUpdate();
        statement.close();
    }
    protected final ResultSet doSelectQuery(PreparedStatement statement)
            throws
            SQLException {
        ResultSet result = statement.executeQuery();
        statement.close();
        return result;
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
