import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

// запилить функционал для интерактивной работы с бд
public class Database {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbname;
    private static Connection connection;
    private static Properties properties;


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


    }

    // student's methods
    public void addStudent(Student student) throws SQLException {
        student.add();
    }
    public List<Student> selectStudent(Criteria criteria, String value)
            throws
            SQLException,
            IOException {
            String query = "SELECT * FROM studentgroupteacher.student";
            PreparedStatement statement = getConnection().
                    prepareStatement(query);
            ArrayList<Student> list = new ArrayList<>();

            switch (criteria) {
                case ID:
                    list.add(selectById(
                            TableType.STUDENT,
                            Integer.parseInt(value)
                    ));
                    break;
                case NAME:
                    query = "SELECT * FROM studentgroupteacher.student"
                            + " WHERE student.Name = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setString(1, value);

                    ResultSet res = statement.executeQuery();

                    while(res.next()) {
                        int s_id = res.getInt(1);
                        String s_name = res.getString(2);
                        LocalDate s_birth = res.getDate(3).toLocalDate();
                        Gender s_gender = Gender.valueOf(res.getString(4));
                        int s_group_id;
                        if(res.getInt(5) != java.sql.Types.NULL) {
                            s_group_id = res.getInt(5);
                        } else {
                            s_group_id = java.sql.Types.NULL;
                        }
                        list.add(new Student(
                                s_id,
                                s_name,
                                s_birth.getYear(),
                                s_birth.getMonth().getValue(),
                                s_birth.getDayOfMonth(),
                                s_gender,
                                s_group_id
                        ));
                    }
                    break;
                case BIRTH:
                    LocalDate birth = LocalDate.of(
                            LocalDate.parse(value).getYear(),
                            LocalDate.parse(value).getMonth(),
                            LocalDate.parse(value).getDayOfMonth() + 1
                    );

                    query = "SELECT * FROM studentgroupteacher.student " +
                            "WHERE student.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));

                    res = statement.executeQuery();
                    while(res.next()) {
                        int s_id = res.getInt(1);
                        String s_name = res.getString(2);
                        LocalDate s_birth = res.getDate(3).toLocalDate();
                        Gender s_gender = Gender.valueOf(res.getString(4));
                        int s_group_id;
                        if(res.getInt(5) != java.sql.Types.NULL) {
                            s_group_id = res.getInt(5);
                        } else {
                            s_group_id = java.sql.Types.NULL;
                        }
                        list.add(new Student(
                                s_id,
                                s_name,
                                s_birth.getYear(),
                                s_birth.getMonth().getValue(),
                                s_birth.getDayOfMonth(),
                                s_gender,
                                s_group_id
                        ));
                    }
                    break;
                case GENDER:
                    Gender gender = Gender.valueOf(value);

                    query = "SELECT * FROM studentgroupteacher.student" +
                            " WHERE student.Gender = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setObject(1, gender.getValue(),
                            java.sql.Types.CHAR);

                    res = statement.executeQuery();
                    while(res.next()) {
                        int s_id = res.getInt(1);
                        String s_name = res.getString(2);
                        LocalDate s_birth = res.getDate(3).toLocalDate();
                        Gender s_gender = Gender.valueOf(res.getString(4));
                        int s_group_id;
                        if(res.getInt(5) != java.sql.Types.NULL) {
                            s_group_id = res.getInt(5);
                        } else {
                            s_group_id = java.sql.Types.NULL;
                        }
                        list.add(new Student(
                                s_id,
                                s_name,
                                s_birth.getYear(),
                                s_birth.getMonth().getValue(),
                                s_birth.getDayOfMonth(),
                                s_gender,
                                s_group_id
                        ));
                    }
                    break;
                case GROUP:
                    Group group = selectGroup(Integer.parseInt(value));
                    int group_id = group.getId();
                    query = "SELECT * FROM studentgroupteacher." +
                            "student WHERE student.group_id = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setInt(1, group_id);

                    res = statement.executeQuery();
                    while(res.next()) {
                        int s_id = res.getInt(1);
                        String s_name = res.getString(2);
                        LocalDate s_birth = res.getDate(3).toLocalDate();
                        Gender s_gender = Gender.valueOf(res.getString(4));
                        int s_group_id;
                        if(res.getInt(5) != java.sql.Types.NULL) {
                            s_group_id = res.getInt(5);
                        } else {
                            s_group_id = java.sql.Types.NULL;
                        }
                        list.add(new Student(
                                s_id,
                                s_name,
                                s_birth.getYear(),
                                s_birth.getMonth().getValue(),
                                s_birth.getDayOfMonth(),
                                s_gender,
                                s_group_id
                        ));
                    }
                    break;
            }
            statement.close();
            return list;
    }
    public void updateStudent(int id, Criteria criteria, String value)
            throws
            SQLException,
            IOException {
        Student student = selectById(TableType.STUDENT,id);
        if(!Objects.isNull(student)) {
                    String query = "SELECT * FROM studentgroupteacher.student";
                    PreparedStatement statement = getConnection()
                            .prepareStatement(query);

                    switch (criteria) {
                        case NAME:
                            student.setName(value);

                            query = "UPDATE studentgroupteacher.student SET " +
                                    "student.Name = ? " +
                                    "WHERE student.student_id = ?";
                            statement = getConnection().
                                    prepareStatement(query);
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
                                    "student.Birthday = ? " +
                                    "WHERE student.student_id = ?";
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
                            statement.setObject(1, newGender.getValue(),
                                    java.sql.Types.CHAR);
                            statement.setInt(2, id);
                            statement.executeUpdate();
                            break;
                        case GROUP:
                            Group group = selectGroup(Integer.parseInt(value));
                            int group_id = group.getId();
                            student.setGroup(group_id);

                            query = "UPDATE studentgroupteacher.student SET " +
                                    "student.group_id = ? " +
                                    "WHERE student.student_id = ?";
                            statement = getConnection()
                                    .prepareStatement(query);
                            statement.setObject(1, group_id,
                                    java.sql.Types.CHAR);
                            statement.setInt(2, id);
                            statement.executeUpdate();
                            break;
                    }
                    statement.close();
                }
    }
    public void deleteStudent(Criteria criteria, String value)
            throws
            SQLException,
            IOException {
                String query = "SELECT * FROM studentgroupteacher.student";
                PreparedStatement statement = getConnection()
                        .prepareStatement(query);

                switch (criteria) {
                    case ID:
                        int id = Integer.parseInt(value);

                        query = "DELETE FROM studentgroupteacher.student " +
                                "WHERE student.student_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setInt(1,id);
                        statement.executeUpdate();

                        break;
                    case NAME:
                        query = "DELETE FROM studentgroupteacher." +
                                "student WHERE student.Name = ?";
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
                        query = "DELETE FROM studentgroupteacher." +
                                "student WHERE student.Birthday = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setDate(1, java.sql.Date.valueOf(birth));
                        statement.executeUpdate();
                        break;
                    case GENDER:
                        Gender gender = Gender.valueOf(value);
                        query = "DELETE FROM studentgroupteacher." +
                                "student WHERE student.Gender = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, gender.getValue()
                                , java.sql.Types.CHAR);
                        statement.executeUpdate();
                        break;
                    case GROUP:
                        Group group = selectGroup(Integer.parseInt(value));
                        int group_id = group.getId();
                        query = "DELETE FROM studentgroupteacher." +
                                "student WHERE student.group_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setInt(1, group_id);
                        statement.executeUpdate();
                        break;
                }
                statement.close();
    }

    // teacher's methods
    public void addTeacher(Teacher teacher) throws SQLException {
        teacher.add();
    }
    public List<Teacher> selectTeacher(Criteria criteria, String value)
            throws
            SQLException,
            IOException {
            String query = "SELECT * FROM studentgroupteacher.teacher";
            PreparedStatement statement = getConnection()
                    .prepareStatement(query);
            List<Teacher> list = new ArrayList<>();

            switch (criteria) {
                case ID:
                    list.add(selectById(
                            TableType.TEACHER,
                            Integer.parseInt(value)
                    ));
                    break;
                case NAME:
                    query = "SELECT * FROM studentgroupteacher." +
                            "teacher WHERE teacher.Name = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setString(1, value);

                    ResultSet res = statement.executeQuery();
                    while(res.next()) {
                        int t_id = res.getInt(1);
                        String t_name = res.getString(2);
                        LocalDate t_birth = res.getDate(3).toLocalDate();
                        Gender t_gender = Gender.valueOf(res.getString(4));

                        list.add(new Teacher(
                                t_id,
                                t_name,
                                t_birth.getYear(),
                                t_birth.getMonth().getValue(),
                                t_birth.getDayOfMonth(),
                                t_gender
                        ));
                    }
                    break;
                case BIRTH:
                    LocalDate birth = LocalDate.of(
                            LocalDate.parse(value).getYear(),
                            LocalDate.parse(value).getMonth(),
                            LocalDate.parse(value).getDayOfMonth() + 1
                    );

                    query = "SELECT * FROM studentgroupteacher.teacher" +
                            " WHERE teacher.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));

                    res = statement.executeQuery();
                    while(res.next()) {
                        int t_id = res.getInt(1);
                        String t_name = res.getString(2);
                        LocalDate t_birth = res.getDate(3).toLocalDate();
                        Gender t_gender = Gender.valueOf(res.getString(4));

                        list.add(new Teacher(
                                t_id,
                                t_name,
                                t_birth.getYear(),
                                t_birth.getMonth().getValue(),
                                t_birth.getDayOfMonth(),
                                t_gender
                        ));
                    }
                    break;
                case GENDER:
                    Gender gender = Gender.valueOf(value);

                    query = "SELECT * FROM studentgroupteacher.teacher " +
                            "WHERE teacher.Gender = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setObject(1, gender.getValue(),
                            java.sql.Types.CHAR);

                    res = statement.executeQuery();
                    while(res.next()) {
                        int t_id = res.getInt(1);
                        String t_name = res.getString(2);
                        LocalDate t_birth = res.getDate(3).toLocalDate();
                        Gender t_gender = Gender.valueOf(res.getString(4));

                        list.add(new Teacher(
                                t_id,
                                t_name,
                                t_birth.getYear(),
                                t_birth.getMonth().getValue(),
                                t_birth.getDayOfMonth(),
                                t_gender
                        ));
                    }
                    break;
            }
            statement.close();
            return list;
    }
    public void updateTeacher(int id, Criteria criteria, String value)
            throws
            SQLException,
            IOException {
            Teacher teacher = selectById(TableType.TEACHER,id);
            if(!Objects.isNull(teacher)) {
                String query = "SELECT * FROM studentgroupteacher.teacher";
                PreparedStatement statement = getConnection().
                        prepareStatement(query);

                switch (criteria) {
                    case NAME:
                        teacher.setName(value);

                        query = "UPDATE studentgroupteacher.teacher SET " +
                                "teacher.Name = ? " +
                                "WHERE teacher.teacher_id = ?";
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
                                "teacher.Gender = ? " +
                                "WHERE teacher.teacher_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, newGender.getValue(),
                                java.sql.Types.CHAR);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        break;
                }
                statement.close();
            }
    }


    public void deleteTeacher(Criteria criteria, String value)
            throws
            SQLException,
            IOException {
            String query = "SELECT * FROM studentgroupteacher.teacher";
            PreparedStatement statement = getConnection().
                    prepareStatement(query);

            switch (criteria) {
                case ID:
                    int id = Integer.parseInt(value);
                    Student student = selectById(TableType.STUDENT,id);

                    query = "DELETE FROM studentgroupteacher.teacher " +
                            "WHERE teacher.teacher_id = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setInt(1,id);
                    statement.executeUpdate();

                    break;
                case NAME:
                    query = "DELETE FROM studentgroupteacher.teacher " +
                            "WHERE teacher.Name = ?";
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

                    query = "DELETE FROM studentgroupteacher.teacher " +
                            "WHERE teacher.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));
                    statement.executeUpdate();
                    break;
                case GENDER:
                    Gender gender = Gender.valueOf(value);

                    query = "DELETE FROM studentgroupteacher.teacher " +
                            "WHERE teacher.Gender = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setObject(1, gender.getValue(),
                            java.sql.Types.CHAR);
                    statement.executeUpdate();
                    break;
            }
            statement.close();
    }
    public void putTeacherInGroup(int teacher_id, int group_number)
            throws
            SQLException,
            IOException {
        Group group = selectGroup(group_number);
        int group_id = group.getId();
        Teacher teacher = selectById(TableType.TEACHER,teacher_id);
        teacher.addGroup(group);

        String query = "INSERT INTO studentgroupteacher.groupteacher " +
                "(group_id, teacher_id) VALUES (?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group_id);
        statement.setInt(2, teacher_id);
        statement.executeUpdate();
    }
    public List<Group> selectTeachersGroup(int teacher_id)
            throws
            SQLException,
            IOException {
        String query = "SELECT * FROM studentgroupteacher.groupteacher " +
                "WHERE groupteacher.teacher_id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, teacher_id);
        ResultSet res = statement.executeQuery();

        List<Group> list = new ArrayList<>();
        while(res.next()) {
            Group group = selectById(TableType.GROUP,res.getInt(2));
            list.add(new Group(group.getNumber()));
        }

        return list;
    }

    // group's methods
    public void addGroup(Group group) throws SQLException {
        group.add();
    }
    public Group selectGroup(int group_number)
            throws
            SQLException,
            IOException {
        String query = "SELECT * FROM studentgroupteacher.group " +
                "WHERE group.Number = ? LIMIT 0,1";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group_number);

        ResultSet res = statement.executeQuery();
        if(res.next()) {
            return new Group(group_number);
        }
        return null;
    }
    // group methods end


    // internal database methods
    private <T extends Table> T selectById(TableType table, int id)
            throws
            SQLException,
            IOException {
        String query = "SELECT * FROM studentgroupteacher."
                + table.getValue() + " WHERE " + table.getValue() + "."
                + table.getValue() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();

        if(table.equals(TableType.STUDENT)) {
            if (res.next()) {
                int s_id = res.getInt(1);
                String s_name = res.getString(2);
                LocalDate s_birth = res.getDate(3).toLocalDate();
                Gender s_gender = Gender.valueOf(res.getString(4));
                int s_group_id;
                if(res.getInt(5) != java.sql.Types.NULL) {
                    s_group_id = res.getInt(5);
                } else {
                    s_group_id = java.sql.Types.NULL;
                }

                statement.close();
                return (T)new Student(
                        s_id,
                        s_name,
                        s_birth.getYear(),
                        s_birth.getMonth().getValue(),
                        s_birth.getDayOfMonth(),
                        s_gender,
                        s_group_id
                );

            }
        } else if (table.equals(TableType.TEACHER)) {
            if (res.next()) {
                int s_id = res.getInt(1);
                String s_name = res.getString(2);
                LocalDate s_birth = res.getDate(3).toLocalDate();
                Gender s_gender = Gender.valueOf(res.getString(4));

                statement.close();
                return (T)new Teacher(
                        s_id,
                        s_name,
                        s_birth.getYear(),
                        s_birth.getMonth().getValue(),
                        s_birth.getDayOfMonth(),
                        s_gender
                );
            }
        }  else if (table.equals(TableType.GROUP)) {
            if (res.next()) {
                int s_id = res.getInt(1);
                int s_number = res.getInt(2);
                statement.close();
                return (T)new Group(
                        s_id,
                        s_number
                        );
            }
        }
        return null;
    }

    private Connection doConnection()
            throws
            SQLException {
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