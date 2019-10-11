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

        Database DB = new Database();
        //DB.addGroup(421);
        DB.addStudent("Ivan Petrovich", "2019-01-01",'M',1);
        DB.addStudent("Habib Petrovich", "2019-01-01",'M',1);
        DB.addStudent("Modest Petrovich", "2019-01-03",'M',1);


        // id в базе и в коллекции не совпадают
        // варианты: переделать ArrayList в Map, уведомить пользователя об этой особенности
        DB.deleteStudent("student_id","2", 2);
        DB.deleteStudent("student_id","3", 2);

        DB.selectStudent("student_id","1");


        DB.addStudent("Mohammed", "2019-01-04",'M',1);
        DB.addStudent("Mohammed Petrovich", "2019-01-05",'M',1);
        DB.addStudent("Habib Petrovich", "2019-01-06",'M',1);

        //DB.selectStudent("student_id","2");
        //DB.selectStudent("student_id","3");


        System.out.println();

    }
}
