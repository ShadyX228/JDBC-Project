import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;
    private Table dbinfo;

    Database() throws SQLException {
        students = new ArrayList<Student>();
        teachers = new ArrayList<Teacher>();
        groups = new ArrayList<Group>();
        dbinfo = new Table();
    }
}
