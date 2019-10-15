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
        /*Properties properties;

        properties = new Properties();
        FileInputStream input = new FileInputStream(
                "connection.properties"
        );

        properties.load(input);
        String driver = properties.getProperty("jdbc.driver");
        String url = driver + "://" + properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String password = properties.getProperty("jdbc.password");
        String dbname = properties.getProperty("jdbc.dbname");
        Connection connection = DriverManager.getConnection(url,user,password);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = "20/06/1995";
        //LocalDate birth = LocalDate.parse(date,formatter);
        LocalDate birth = LocalDate.of(1995,06,20+1);
        System.out.println(formatter.format(birth));
        System.out.println(java.sql.Date.valueOf(birth));


        String query = "INSERT INTO studentgroupteacher.teacher (Name, Birthday, Sex) VALUES (?, ?, NULL);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,"sag");
        statement.setObject(2,java.sql.Date.valueOf(birth));
        statement.execute();*/

        Database DB = new Database();
        DB.addStudent(new Student("sss", 1995,06,20,Gender.MALE,java.sql.Types.NULL));
        DB.addStudent(new Student("ssss", 1995,06,20,Gender.MALE,java.sql.Types.NULL));
        DB.addStudent(new Student("sss", 1995,06,20,Gender.MALE,java.sql.Types.NULL));
        DB.addStudent(new Student("sss1", 1995,06,21,Gender.MALE,java.sql.Types.NULL));
        DB.addStudent(new Student("sss2", 1995,06,21,Gender.MALE,java.sql.Types.NULL));
        DB.addStudent(new Student("sss2", 1995,06,21,Gender.FEMALE,java.sql.Types.NULL));
        DB.addStudent(new Student("sss2", 1995,06,21,Gender.FEMALE,java.sql.Types.NULL));

        for(Student s : DB.selectStudent(Criteria.BIRTH,"1995-06-20")) {
            System.out.println(s);
        }
        DB.deleteStudent(Criteria.BIRTH,"1995-06-21");
        //DB.updateStudent(101,Criteria.NAME,"ssss");
        //DB.updateStudent(101,Criteria.BIRTH,"1995-12-06");
        //DB.updateStudent(101,Criteria.GENDER,"FEMALE");
        //DB.deleteStudent(Criteria.ID,"160");
        //DB.deleteStudent(Criteria.NAME,"sss");
        //DB.deleteStudent(Criteria.GENDER,"FEMALE");
        //DB.deleteStudent(Criteria.GENDER,"FEMALE");
    }
}
/*управление id перенести на бд(смотри auto_increment для mysql)
сделать birthday в student и teacher датой,а не строкой
зачем пол задан как char? сделать enum
что за магические цифры id в main? как ты определяешь у кого какой id?
не увидел связи между преподавателем и группой
прочитать про sql-инъекции и методы борьбы с ними*/