import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    private static int nextId = 0;
    public static void main(String[] args) throws SQLException {
        Database DB = new Database();
        DB.addGroup(421);
        DB.selectGroup("group_id",1);
        System.out.print("\nTrying to access to empty students table: ");
        DB.selectStudent("student_id","1");

        System.out.print("\nAdding students and teachers... ");
        DB.addStudent("Habib Petrovich","1998-04-09",'M',1);
        DB.addStudent("Surkhay Petrovich","1997-05-22",'M',1);
        DB.addStudent("Madina Petrovna","1999-02-01",'F',1);
        DB.addStudent("Mahmud Petrov","1998-01-01",'M',1);
        DB.addStudent("Mamed Petrovas","1998-05-12",'M',1);
        DB.addStudent("Tagir Petrov","1998-02-03",'M',1);

        DB.addTeacher("Conor Mcgregor","1956-01-05",'M');
        DB.addTeacher("Nurmagomed Nariev","1972-02-05",'M');
        DB.addTeacher("Galayev Ded","1981-05-05",'M');
        System.out.println("Success.");

        System.out.println("\nAll students selecting: ");
        for(int i = 1;i <= 6;i++) {
            DB.selectStudent("student_id",String.valueOf(i));
        }

        System.out.println("\nAll teachers selecting: ");
        for(int i = 2;i < 4;i++) {
            DB.selectTeacher("teacher_id",String.valueOf(i));
        }

        System.out.println("\nDeleting students: ");
        for(int i = 3;i < 6;i++) {
            DB.deleteStudent(i);
        }

        System.out.println("\nDeleting teachers: ");
        for(int i = 1;i <= 2;i++) {
            DB.deleteTeacher(i);
        }

        System.out.println("\nPrinting all DB data: ");
        DB.printTables();

        System.out.print("\nAdding more students and teachers...");
        DB.addStudent("Mahmud Petrov","1998-01-01",'M',1);
        DB.addStudent("Mamed Petrovas","1998-05-12",'M',1);
        DB.addStudent("Tagir Petrov","1998-02-03",'M',1);
        System.out.println("Success.");

        System.out.println("\nSome students selecting: ");
        for(int i = 1;i <= 6;i++) {
            DB.selectStudent("student_id",String.valueOf(i));
        }

        System.out.println("\nSome teachers selecting: ");
        for(int i = 2;i < 4;i++) {
            DB.selectTeacher("teacher_id",String.valueOf(i));
        }

        System.out.println("\nPrinting all DB data: ");
        DB.printTables();


        /*long startTime = System.currentTimeMillis();

        Group g421 = new Group(421);
        Student s1 = new Student(
                "I I",
               LocalDate.of(2019, 1, 22),
                'M',
                1
        );
        s1.selectByCriteria("Birthday", "2019-01-22");
        s1.updateByCriteria("Birthday", "2019-02-22");
        s1.selectByCriteria("Birthday", "2019-02-22");
        s1.deleteByCriteria("group_id","1");

        Teacher t1 = new Teacher(
                "I I I",
                LocalDate.of(2019, 1, 22),
                'M'
        );
        t1.selectByCriteria("Birthday", "2019-01-22");
        t1.updateByCriteria("Birthday", "2019-02-22");
        t1.selectByCriteria("Birthday", "2019-02-22");
        t1.deleteByCriteria("teacher_id","1");
        g421.deleteByCriteria("Number",421);*/

        /*String b = "2019-01-22";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        LocalDate birth = LocalDate.parse(b);
        System.out.println(birth.format(df));*/

        //ArrayList<Integer> i = new ArrayList<Integer>();

        /*Database DB = new Database();
        DB.addGroup(421);
        DB.addStudent("Ivan Petrovich", "2019-01-01",'M',1);
        DB.addStudent("Habib Petrovich", "2019-04-01",'M',1);
        DB.addStudent("Modest Petrovich", "2019-03-21",'M',1);
        DB.addStudent("Mohammed Petrovich", "2019-12-05",'M',1);
        DB.addStudent("Habib Petrovich", "2019-01-06",'M',1);

        // id в базе и в коллекции не совпадают
        // варианты: переделать ArrayList в Map, уведомить пользователя об этой особенности
        // челик 2 удалился, место в коллекции сдвинулось
        DB.deleteStudent(2);
        // челик 3 теперь имеет место в коллекции 2
        // но в бд он все еще числится как 3
        // вызов delete проходит
        DB.deleteStudent(3);




        DB.addStudent("Mohammed", "2019-01-04",'M',1);
        // а вызов этого - нет
        DB.addStudent("Mohammed Petrovich", "2019-01-05",'M',1);
        DB.addStudent("Habib Petrovich", "2019-01-06",'M',1);

        DB.selectStudent("student_id","2");
        DB.selectStudent("student_id","3");

        DB.addStudent("Mohammed Petrovich", "2019-01-05",'M',1);
        DB.addStudent("Habib Petrovich", "2019-01-06",'M',1);

        DB.selectStudent("student_id","9");
        DB.selectStudent("student_id","10");

        System.out.println();*/
    }
}
