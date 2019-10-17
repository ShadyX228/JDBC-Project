import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.IDN;
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
                System.out.print(method.getName() + " ");
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

        getMethods("Student");
        getMethods("Teacher");
        getMethods("Group");

        String methodName = "";
        while(!methodName.equals("exit")) {
            System.out.print("\nEnter method's name or \"exit\" to stop: ");
            methodName = input.next();
            switch (methodName) {
                // student methods begin
                case "addStudent":
                    System.out.print("Adding student. \nEnter name: ");
                    String name = input.next();

                    System.out.print("Entering birthday. Enter year: ");
                    int year = input.nextInt();

                    System.out.print("Enter month: ");
                    int month = input.nextInt();

                    System.out.print("Enter day: ");
                    int day = input.nextInt();

                    System.out.print("Enter gender (MALE/FEMALE): ");
                    String genderInput = input.next();
                    Gender gender = Gender.valueOf(genderInput);

                    System.out.print("Enter student's group number: ");
                    int number = input.nextInt();
                    Group group = DB.selectGroup(number);
                    //System.out.println(group.getId());
                    int group_id = group.getId();

                    DB.addStudent(new Student(name, year, month, day, gender, group_id));

                    //System.out.println(name + " " + year + " " + month + " " + day);
                    break;

                case "selectStudent":
                    System.out.print("Selecting students. \nEnter criteria. Available variants: ");
                    for(Criteria criteria1 : Criteria.values()) {
                        System.out.print(criteria1 + " ");
                    }
                    System.out.print(": ");
                    String crit = input.next();
                    Criteria criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    String critVal = input.next();

                    for(Student student : DB.selectStudent(criteria, critVal)) {
                        System.out.println(student);
                    }

                    break;

                case "deleteStudent":
                    System.out.print("Deleting studetns. \nEnter criteria. Available variants: ");
                    for(Criteria criteria2 : Criteria.values()) {
                        System.out.print(criteria2 + " ");
                    }
                    System.out.print(": ");
                    crit = input.next();
                    criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    critVal = input.next();

                    DB.deleteStudent(criteria, critVal);
                    break;

                case "updateStudent":
                    System.out.print("Updating student. \nEnter id: ");
                    int id = input.nextInt();
                    Student student;
                    for(Student element : DB.selectStudent(Criteria.ID,Integer.toString(id))) {
                        student = element;
                        break;
                    }
                    System.out.print("Enter criteria wich need to update. Available variants: ");
                    for(Criteria criteria2 : Criteria.values()) {
                        System.out.print(criteria2 + " ");
                    }
                    crit = input.next();
                    criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    critVal = input.next();
                    DB.updateStudent(id, criteria, critVal);
                    break;
                // student methods end

                // teacher methods begin
                case "addTeacher":
                    System.out.print("Adding teacher. \nEnter name: ");
                    name = input.next();

                    System.out.print("Entering birthday. Enter year: ");
                    year = input.nextInt();

                    System.out.print("Enter month: ");
                    month = input.nextInt();

                    System.out.print("Enter day: ");
                    day = input.nextInt();

                    System.out.print("Enter gender (MALE/FEMALE): ");
                    genderInput = input.next();
                    gender = Gender.valueOf(genderInput);

                    DB.addTeacher(new Teacher(name, year, month, day, gender));
                    break;

                 case "addSelectTeacher":

                     break;

                case "deleteTeacher" :

                    break;

                case "updateTeacher" :

                    break;

                case "selectTeachersGroup" :

                    break;

                case "putTeacherGroup" :

                    break;


                // teacher methods end


                // General methods
                case "printTables":
                    DB.printTables();
                    break;

                case "exit":
                    System.out.print("Shutting down.");
                    break;

                default:
                    System.out.println("No method with selected name.");
            }
        }


        /*DB.addStudent(new Student("Andrei Petrovich",1995,06,20,Gender.MALE,java.sql.Types.NULL));
        for(Student student : DB.selectStudent(Criteria.BIRTH, "1995-06-20")) {
            System.out.println(student);
        }*/
        //DB.deleteStudent(Criteria.BIRTH,"2019-10-23");


    }
}
/*связи между преподавателем и группой так и нет
сделай возможность добавлять/просматривать/удалять данные через консоль*/