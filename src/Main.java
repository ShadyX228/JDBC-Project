import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
            throws
            SQLException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            IOException  {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream("connection.properties");
        properties.load(input);

        Database testDatabase = new Database(
                properties.getProperty("jdbc.driver"),
                properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.user"),
                properties.getProperty("jdbc.password"),
                properties.getProperty("jdbc.dbname")
        );

        String method;
        Scanner in = new Scanner(System.in);

        System.out.println("Enter method name: ");
        method = in.next();

        testDatabase.execute(method);


    }
}