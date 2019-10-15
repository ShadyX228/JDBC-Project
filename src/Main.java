import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        Database DB = new Database();
        System.out.println(DB.selectGroup(421));
        for(Student student : DB.selectStudent(Criteria.BIRTH,"2019-10-23")) {
            System.out.println(student);
        }
        for(Teacher teacher : DB.selectTeacher(Criteria.GENDER,"MALE")) {
            System.out.println(teacher);
        }

        DB.deleteStudent(Criteria.BIRTH,"2019-10-23");


    }
}