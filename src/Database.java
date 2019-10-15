import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;
// допилить те же функции для group
// запилить функционал для интерактивной работы с бд
public class Database {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbname;
    private static Connection connection;
    private static Properties properties;

    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;

    Database() throws SQLException, IOException {
        properties = new Properties();
        FileInputStream input = new FileInputStream(
                "connection.properties"
        );

        properties.load(input);
        driver = properties.getProperty("jdbc.driver");
        url = driver + "://" + properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
        dbname = properties.getProperty("jdbc.dbname");
        connection = doConnection();

        students = new ArrayList<>();
        teachers = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }
    public ArrayList<Student> selectStudent(Criteria criteria, String value) throws SQLException {
        if(!students.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.student";
            PreparedStatement statement = getConnection().prepareStatement(query);
            ArrayList<Student> list = new ArrayList<>();

            switch (criteria) {
                case ID:
                    list.add(selectStudentById(Integer.parseInt(value)));
                    break;
                case NAME:
                    query = "SELECT * FROM studentgroupteacher.student" + " WHERE student.Name = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setString(1, value);

                    ResultSet res = statement.executeQuery();
                    while(res.next()) {
                        Student student = selectStudentById(res.getInt(1));
                        list.add(student);
                    }
                    break;
                case BIRTH:
                    LocalDate birth = LocalDate.of(
                            LocalDate.parse(value).getYear(),
                            LocalDate.parse(value).getMonth(),
                            LocalDate.parse(value).getDayOfMonth() + 1
                    );

                    query = "SELECT * FROM studentgroupteacher.student WHERE student.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));

                    res = statement.executeQuery();
                    while(res.next()) {
                        Student student = selectStudentById(res.getInt(1));
                        list.add(student);
                    }
                    break;
                case GENDER:
                    Gender gender = Gender.valueOf(value);

                    query = "SELECT * FROM studentgroupteacher.student WHERE student.Gender = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setObject(1, gender.getValue(), java.sql.Types.CHAR);

                    res = statement.executeQuery();
                    while(res.next()) {
                        Student student = selectStudentById(res.getInt(1));
                        list.add(student);
                    }
                    break;
                case GROUP:
                    int group_id = Integer.parseInt(value);
                    query = "SELECT * FROM studentgroupteacher.student WHERE student.group_id = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setInt(1, group_id);

                    res = statement.executeQuery();
                    while(res.next()) {
                        Student student = selectStudentById(res.getInt(1));
                        list.add(student);
                    }
                    break;
            }
            statement.close();
            return list;
        }
        return null;
    }
    public void updateStudent(int id, Criteria criteria, String value) throws SQLException {
        if(!students.isEmpty()) {
            Student student = selectStudentById(id);
            if(!Objects.isNull(student)) {
                String query = "SELECT * FROM studentgroupteacher.student";
                PreparedStatement statement = getConnection().prepareStatement(query);

                switch (criteria) {
                    case NAME:
                        student.setName(value);

                        query = "UPDATE studentgroupteacher.student SET " +
                                "student.Name = ? WHERE student.student_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setString(1, value);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                    case BIRTH:
                        LocalDate birth = LocalDate.of(
                                LocalDate.parse(value).getYear(),
                                LocalDate.parse(value).getMonth(),
                                LocalDate.parse(value).getDayOfMonth() + 1
                        );
                        student.setBirthday(birth);

                        query = "UPDATE studentgroupteacher.student SET " +
                                "student.Birthday = ? WHERE student.student_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setDate(1, java.sql.Date.valueOf(birth));
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                    case GENDER:
                        Gender newGender = Gender.valueOf(value);
                        student.setGender(newGender);

                        query = "UPDATE studentgroupteacher.student SET " +
                                "student.Gender = ? WHERE student.student_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, newGender.getValue(), java.sql.Types.CHAR);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                    case GROUP:
                        int group_id = Integer.parseInt(value);
                        student.setGroup(group_id);

                        query = "UPDATE studentgroupteacher.student SET " +
                                "student.group_id = ? WHERE student.student_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, group_id, java.sql.Types.CHAR);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                }
                statement.close();
            }
        }
    }
    public void deleteStudent(Criteria criteria, String value) throws SQLException {
        if(!students.isEmpty()) {
                String query = "SELECT * FROM studentgroupteacher.student";
                PreparedStatement statement = getConnection().prepareStatement(query);

                switch (criteria) {
                    case ID:
                        int id = Integer.parseInt(value);
                        Student student = selectStudentById(id);

                        query = "DELETE FROM studentgroupteacher.student WHERE student.student_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setInt(1,id);
                        statement.executeUpdate();

                        if(!Objects.isNull(student)) {
                            for(Student element : students) {
                                if(element.getId() == id) {
                                    students.remove(element);
                                    break;
                                }
                            }
                        }
                        break;
                    case NAME:
                        Iterator<Student> it = students.iterator();
                        while(it.hasNext()) {
                            Student element = it.next();
                            if(element.getName().equals(value)) {
                                it.remove();
                            }
                        }
                        query = "DELETE FROM studentgroupteacher.student WHERE student.Name = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setString(1, value);
                        statement.executeUpdate();
                        break;
                    case BIRTH:
                        LocalDate birth = LocalDate.of(
                                LocalDate.parse(value).getYear(),
                                LocalDate.parse(value).getMonth(),
                                LocalDate.parse(value).getDayOfMonth() + 1
                        );
                        query = "DELETE FROM studentgroupteacher.student WHERE student.Birthday = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setDate(1, java.sql.Date.valueOf(birth));
                        statement.executeUpdate();
                        break;
                    case GENDER:
                        Gender newGender = Gender.valueOf(value);

                        query = "DELETE FROM studentgroupteacher.student WHERE student.Gender = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, newGender.getValue(), java.sql.Types.CHAR);
                        statement.executeUpdate();
                        break;
                    case GROUP:
                        int group_id = Integer.parseInt(value);
                        it = students.iterator();
                        while(it.hasNext()) {
                            Student element = it.next();
                            if(element.getGroup() == group_id) {
                                it.remove();
                            }

                        }
                        query = "DELETE FROM studentgroupteacher.student WHERE student.group_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setInt(1, group_id);
                        statement.executeUpdate();
                        break;
                }
                statement.close();
        }
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    public ArrayList<Teacher> selectTeacher(Criteria criteria, String value) throws SQLException {
        if(!teachers.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.teacher";
            PreparedStatement statement = getConnection().prepareStatement(query);
            ArrayList<Teacher> list = new ArrayList<>();

            switch (criteria) {
                case ID:
                    list.add(selectTeacherById(Integer.parseInt(value)));
                    break;
                case NAME:
                    query = "SELECT * FROM studentgroupteacher.teacher WHERE teacher.Name = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setString(1, value);

                    ResultSet res = statement.executeQuery();
                    while(res.next()) {
                        Teacher teacher = selectTeacherById(res.getInt(1));
                        list.add(teacher);
                    }
                    break;
                case BIRTH:
                    LocalDate birth = LocalDate.of(
                            LocalDate.parse(value).getYear(),
                            LocalDate.parse(value).getMonth(),
                            LocalDate.parse(value).getDayOfMonth() + 1
                    );

                    query = "SELECT * FROM studentgroupteacher.teacher WHERE teacher.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));

                    res = statement.executeQuery();
                    while(res.next()) {
                        Teacher teacher = selectTeacherById(res.getInt(1));
                        list.add(teacher);
                    }
                    break;
                case GENDER:
                    Gender gender = Gender.valueOf(value);

                    query = "SELECT * FROM studentgroupteacher.teacher WHERE teacher.Gender = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setObject(1, gender.getValue(), java.sql.Types.CHAR);

                    res = statement.executeQuery();
                    while(res.next()) {
                        Teacher teacher = selectTeacherById(res.getInt(1));
                        list.add(teacher);
                    }
                    break;
            }
            statement.close();
            return list;
        }
        return null;
    }
    public void updateTeacher(int id, Criteria criteria, String value) throws SQLException {
        if(!teachers.isEmpty()) {
            Teacher teacher = selectTeacherById(id);
            if(!Objects.isNull(teacher)) {
                String query = "SELECT * FROM studentgroupteacher.teacher";
                PreparedStatement statement = getConnection().prepareStatement(query);

                switch (criteria) {
                    case NAME:
                        teacher.setName(value);

                        query = "UPDATE studentgroupteacher.teacher SET " +
                                "teacher.Name = ? WHERE teacher.teacher_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setString(1, value);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                    case BIRTH:
                        LocalDate birth = LocalDate.of(
                                LocalDate.parse(value).getYear(),
                                LocalDate.parse(value).getMonth(),
                                LocalDate.parse(value).getDayOfMonth() + 1
                        );
                        teacher.setBirthday(birth);

                        query = "UPDATE studentgroupteacher.teacher SET " +
                                "teacher.Birthday = ? WHERE teacher.teacher_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setDate(1, java.sql.Date.valueOf(birth));
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                    case GENDER:
                        Gender newGender = Gender.valueOf(value);
                        teacher.setGender(newGender);

                        query = "UPDATE studentgroupteacher.teacher SET " +
                                "teacher.Gender = ? WHERE teacher.teacher_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, newGender.getValue(), java.sql.Types.CHAR);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                }
                statement.close();
            }
        }
    }
    public void deleteTeacher(Criteria criteria, String value) throws SQLException {
        if(!teachers.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.teacher";
            PreparedStatement statement = getConnection().prepareStatement(query);

            switch (criteria) {
                case ID:
                    int id = Integer.parseInt(value);
                    Student student = selectStudentById(id);

                    query = "DELETE FROM studentgroupteacher.teacher WHERE teacher.teacher_id = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setInt(1,id);
                    statement.executeUpdate();

                    if(!Objects.isNull(student)) {
                        for(Teacher element : teachers) {
                            if(element.getId() == id) {
                                teachers.remove(element);
                                break;
                            }
                        }
                    }
                    break;
                case NAME:
                    Iterator<Teacher> it = teachers.iterator();
                    while(it.hasNext()) {
                        Teacher element = it.next();
                        if(element.getName().equals(value)) {
                            it.remove();
                        }
                    }
                    query = "DELETE FROM studentgroupteacher.teacher WHERE teacher.Name = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setString(1, value);
                    statement.executeUpdate();
                    break;
                case BIRTH:
                    LocalDate birth = LocalDate.of(
                            LocalDate.parse(value).getYear(),
                            LocalDate.parse(value).getMonth(),
                            LocalDate.parse(value).getDayOfMonth() + 1
                    );
                    query = "DELETE FROM studentgroupteacher.teacher WHERE teacher.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));
                    statement.executeUpdate();
                    break;
                case GENDER:
                    Gender newGender = Gender.valueOf(value);

                    query = "DELETE FROM studentgroupteacher.teacher WHERE teacher.Gender = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setObject(1, newGender.getValue(), java.sql.Types.CHAR);
                    statement.executeUpdate();
                    break;
            }
            statement.close();
        }
    }


    private Student selectStudentById(int id) throws SQLException {
        if(!students.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.student WHERE student.student_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                int resId = res.getInt(1);
                for (Student student : students) {
                    if (student.getId() == resId) {
                        return student;
                    }
                }
                }
            statement.close();
        }
        return null;
    }
    private Teacher selectTeacherById(int id) throws SQLException {
        if(!teachers.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.teacher WHERE student.teacher_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                int resId = res.getInt(1);
                for (Teacher teacher : teachers) {
                    if (teacher.getId() == resId) {
                        return teacher;
                    }
                }
                }
            statement.close();
        }
        return null;
    }
    private Group selectGroupById(int id) throws SQLException {
        if(!groups.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.group WHERE student.group_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                int resId = res.getInt(1);
                for (Group group : groups) {
                    if (group.getId() == resId) {
                        return group;
                    }
                }
            }
            statement.close();
        }
        return null;
    }

    private Connection doConnection() throws SQLException {
        return DriverManager.getConnection(
                url,
                user,
                password
        );
    }
    private Connection getConnection() {
        return connection;
    }

}
