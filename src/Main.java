import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static ArrayList<Method> getMethods(String table) {
        System.out.print("Available methods for "+ table + "'s table: ");
        ArrayList<Method> result = new ArrayList<>();
        for(Method method : Database.class.getDeclaredMethods()) {
            if(Modifier.toString(method.getModifiers()).contains("public") && method.getName().contains(table)) {
                System.err.print(method.getName() + " ");
                result.add(method);
            }
        }
        System.out.println();
        return result;
    }
    public static void main(String[] args) throws SQLException, IOException {
        Database DB;
        Scanner input = new Scanner(System.in);
        System.out.println("Will you want to see table's content? Y/N");
        char check = input.next().charAt(0);
        if(check == 'Y') {
            DB = new Database(true);
        } else {
            DB = new Database(false);
        }

        /*getMethods("Student");
        getMethods("Teacher");
        getMethods("Group");

        System.out.println("\nEnter method's name: ");
        String methodName = input.next();
        switch (methodName) {
            case "addStudent":
                System.out.println("Adding student. \nEnter name: ");
                String name = input.next();

                System.out.println("Entering birthday. Enter year: ");
                int year = input.nextInt();

                System.out.println("Enter month: ");
                int month = input.nextInt();

                System.out.println("Enter day: ");
                int day = input.nextInt();

                System.out.println(name + " " + year + " " + month + " " + day);

                break;
            default:
                System.out.println("No method with selected name. Select method's name from list that above.");
        }


        /*DB.addStudent(new Student("Andrei Petrovich",1995,06,20,Gender.MALE,java.sql.Types.NULL));
        for(Student student : DB.selectStudent(Criteria.BIRTH, "1995-06-20")) {
            System.out.println(student);
        }*/
        //DB.deleteStudent(Criteria.BIRTH,"2019-10-23");*/


    }
}