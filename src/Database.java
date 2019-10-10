import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Database {
    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;
    private DBInfo dbinfo;

    Database() throws SQLException {
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        groups = new ArrayList<>();
        dbinfo = new DBInfo();
    }
    public void addStudent(String name, String birth, char sex, int group_id)
            throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        LocalDate birthParsed = LocalDate.parse(birth);
        students.add(
                new Student(
                        name,
                        birthParsed,
                        sex,
                        group_id
                )
        );
    }
    public void addTeacher(String name, String birth, char sex)
            throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        LocalDate birthParsed = LocalDate.parse(birth);
        teachers.add(
                new Teacher(
                        name,
                        birthParsed,
                        sex
                )
        );
    }
    public void addGroup(int number)
            throws SQLException {
        groups.add(
                new Group(
                        number
                )
        );
    }
    public void selectStudent(String criteria, String critValue)
            throws SQLException {
        if(!students.isEmpty()) {
            ArrayList<Integer> indexes = students.get(students.size()-1).selectByCriteria(criteria, critValue);
            for(int index : indexes){
                System.out.println(students.get(index-1));
            }
        }
        else {
            System.out.println("No students in database.");
        }
    }
}
