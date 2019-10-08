import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        Scanner in = new Scanner(System.in);

        Method[] methods = Database.class.getDeclaredMethods();
        System.out.println("Available methods: ");
        for(Method met : methods) {
            if(met.isAnnotationPresent(withConnection.class)) {
                System.out.println(met.getName());
            }
        }

        String method = " ";
        while(!method.equals("exit")) {
            System.out.println("Enter method name or exit to stop: ");
            method = in.next();
            if(!method.equals("exit"))
                testDatabase.execute(method);
        }


    }
}