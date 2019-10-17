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

    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;

    Database(boolean showTables) throws SQLException, IOException {
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

        fillTables(showTables);
    }

    // student's methods
    public void addStudent(Student student) {
        students.add(student);
    }
    public ArrayList<Student> selectStudent(Criteria criteria, String value)
            throws
            SQLException {
        if(!students.isEmpty()) {
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
                        int id = res.getInt(1);
                        for(Student student : students) {
                            if(student.getId() == id) {
                                list.add(student);
                                break;
                            }
                        }
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
                        int id = res.getInt(1);
                        for(Student student : students) {
                            if(student.getId() == id) {
                                list.add(student);
                                break;
                            }
                        }
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
                        int id = res.getInt(1);
                        for(Student student : students) {
                            if(student.getId() == id) {
                                list.add(student);
                                break;
                            }
                        }
                    }
                    break;
                case GROUP:
                    int group_id = Integer.parseInt(value);
                    query = "SELECT * FROM studentgroupteacher." +
                            "student WHERE student.group_id = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setInt(1, group_id);

                    res = statement.executeQuery();
                    while(res.next()) {
                        int id = res.getInt(1);
                        for(Student student : students) {
                            if(student.getId() == id) {
                                list.add(student);
                                break;
                            }
                        }
                    }
                    break;
            }
            statement.close();
            return list;
        }
        return null;
    }
    public void updateStudent(int id, Criteria criteria, String value)
            throws
            SQLException {
            if(!students.isEmpty()) {
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
                            int group_id = Integer.parseInt(value);
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
    }
    public void deleteStudent(Criteria criteria, String value)
            throws
            SQLException {
        if(!students.isEmpty()) {
                String query = "SELECT * FROM studentgroupteacher.student";
                PreparedStatement statement = getConnection()
                        .prepareStatement(query);

                switch (criteria) {
                    case ID:
                        int id = Integer.parseInt(value);
                        Student student = selectById(TableType.STUDENT,id);

                        query = "DELETE FROM studentgroupteacher.student " +
                                "WHERE student.student_id = ?";
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
                        it = students.iterator();
                        while(it.hasNext()) {
                            Student element = it.next();
                            if(element.getGender().equals(gender)) {
                                it.remove();
                            }
                        }

                        query = "DELETE FROM studentgroupteacher." +
                                "student WHERE student.Gender = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setObject(1, gender.getValue()
                                , java.sql.Types.CHAR);
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
                        query = "DELETE FROM studentgroupteacher." +
                                "student WHERE student.group_id = ?";
                        statement = getConnection().prepareStatement(query);
                        statement.setInt(1, group_id);
                        statement.executeUpdate();
                        break;
                }
                statement.close();
        }
    }

    // teacher's methods
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    public ArrayList<Teacher> selectTeacher(Criteria criteria, String value)
            throws
            SQLException {
        if(!teachers.isEmpty()) {
            String query = "SELECT * FROM studentgroupteacher.teacher";
            PreparedStatement statement = getConnection()
                    .prepareStatement(query);
            ArrayList<Teacher> list = new ArrayList<>();

            switch (criteria) {
                case ID:
                    list.add(selectById(TableType.TEACHER,Integer.parseInt(value)));
                    break;
                case NAME:
                    query = "SELECT * FROM studentgroupteacher." +
                            "teacher WHERE teacher.Name = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setString(1, value);

                    ResultSet res = statement.executeQuery();
                    while(res.next()) {
                        int id = res.getInt(1);
                        for(Teacher teacher : teachers) {
                            if(teacher.getId() == id) {
                                list.add(teacher);
                                break;
                            }
                        }
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
                        int id = res.getInt(1);
                        for(Teacher teacher : teachers) {
                            if(teacher.getId() == id) {
                                list.add(teacher);
                                break;
                            }
                        }
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
                        int id = res.getInt(1);
                        for(Teacher teacher : teachers) {
                            if(teacher.getId() == id) {
                                list.add(teacher);
                                break;
                            }
                        }
                    }
                    break;
            }
            statement.close();
            return list;
        }
        return null;
    }
    public void updateTeacher(int id, Criteria criteria, String value)
            throws
            SQLException {
        if(!teachers.isEmpty()) {
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
    }
    public void deleteTeacher(Criteria criteria, String value)
            throws
            SQLException {
        if(!teachers.isEmpty()) {
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
                            LocalDate.parse(value).getDayOfMonth()
                    );
                    it = teachers.iterator();
                    while(it.hasNext()) {
                        Teacher element = it.next();
                        if(element.getBirth().equals(birth)) {
                            it.remove();
                        }
                    }

                    query = "DELETE FROM studentgroupteacher.teacher " +
                            "WHERE teacher.Birthday = ?";
                    statement = getConnection().prepareStatement(query);
                    statement.setDate(1, java.sql.Date.valueOf(birth));
                    statement.executeUpdate();
                    break;
                case GENDER:
                    Gender gender = Gender.valueOf(value);
                    it = teachers.iterator();
                    while(it.hasNext()) {
                        Teacher element = it.next();
                        if(element.getGender().equals(gender)) {
                            it.remove();
                        }
                    }

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
    }
    public void putTeacherInGroup(int teacher_id, int group_number)
            throws
            SQLException {
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
    public ArrayList<Group> selectTeachersGroup(int teacher_id)
            throws
            SQLException {
        String query = "SELECT * FROM studentgroupteacher.groupteacher " +
                "WHERE groupteacher.teacher_id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, teacher_id);
        ResultSet res = statement.executeQuery();

        ArrayList<Group> list = new ArrayList<>();
        while(res.next()) {
            for(Group group : groups) {
                //System.out.print(group);
                if(group.getId() == res.getInt(2)) {
                    list.add(group);
                }
            }
            /*for(Group group : groups) {
                int g_id = group.getId();
                for(Group tGroup : teacher.getGroups()) {
                    if(g_id == tGroup.getId()) {
                        list.add(tGroup);
                    }
                }
            }*/
            //Group group = selectById(TableType.GROUP,res.getInt(2));
            //list.add(group);
        }
        return list;
    }

    // group's methods
    public void addGroup(Group group) {
        groups.add(group);
    }
    public Group selectGroup(int group_number) throws SQLException {
        String query = "SELECT * FROM studentgroupteacher.group " +
                "WHERE group.Number = ? LIMIT 0,1";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group_number);

        ResultSet res = statement.executeQuery();
        if(res.next()) {
            int resId = res.getInt(1);
            for (Group group : groups) {
                if (group.getId() == resId) {
                    return group;
                }
            }
        }
        return null;
    }

    // public database methods
    public void printTables() {
        System.out.println("Table: student");
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println();

        System.out.println("Table: teacher");
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
        System.out.println();

        System.out.println("Table: group");
        for (Group group : groups) {
            System.out.println(group);
        }
        System.out.println();
    }

    // internal database methods
    private <T extends Table> T selectById(TableType table, int id)
            throws
            SQLException {
        String query = "SELECT * FROM studentgroupteacher."
                + table.getValue() + " WHERE " + table.getValue() + "."
                + table.getValue() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();

        if(table.equals(TableType.STUDENT) && !students.isEmpty()) {
            if (res.next()) {
                int resId = res.getInt(1);
                for (Student student : students) {
                    if (student.getId() == resId) {
                        return (T)student;
                    }
                }
            }
        } else if (table.equals(TableType.TEACHER) && !teachers.isEmpty()) {
            if (res.next()) {
                int resId = res.getInt(1);
                for (Teacher teacher : teachers) {
                    if (teacher.getId() == resId) {
                        return (T)teacher;
                    }
                }
            }
            statement.close();
        }  else if (table.equals(TableType.GROUP) && !groups.isEmpty()) {
            if (res.next()) {
                int resId = res.getInt(1);
                for (Group group : groups) {
                    if (group.getId() == resId) {
                        return (T)group;
                    }
                }
            }
            statement.close();
        }

            return null;
    }
    private void fillTables(boolean showTables)
            throws
            SQLException,
            IOException {
        String query = "SELECT * FROM studentgroupteacher.student";
        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            int id = res.getInt(1);
            String name = res.getString(2);

            LocalDate birthday;
            birthday = res.getDate(3).toLocalDate();

            Gender gender;
            if(!Objects.isNull(res.getString(4))) {
                gender = Gender.valueOf(res.getString(4));
            } else {
                gender = Gender.NULL;
            }

            int group_id;
            if(res.getInt(5) != java.sql.Types.NULL) {
                group_id = res.getInt(5);
            } else {
                group_id = java.sql.Types.NULL;
            }

            students.add(new Student(
                    id,
                    name,
                    birthday.getYear(),
                    birthday.getMonthValue(),
                    birthday.getDayOfMonth(),
                    gender,
                    group_id
                )
            );
        }

        query = "SELECT * FROM studentgroupteacher.teacher";
        statement = getConnection().prepareStatement(query);
        res = statement.executeQuery();
        while (res.next()) {
            int id = res.getInt(1);
            String name = res.getString(2);

            LocalDate birthday;
            birthday = res.getDate(3).toLocalDate();

            Gender gender;
            if(!Objects.isNull(res.getString(4))) {
                gender = Gender.valueOf(res.getString(4));
            } else {
                gender = Gender.NULL;
            }

            teachers.add(new Teacher(
                    id,
                    name,
                    birthday.getYear(),
                    birthday.getMonthValue(),
                    birthday.getDayOfMonth(),
                    gender
                )
            );
        }

        query = "SELECT * FROM studentgroupteacher.group";
        statement = getConnection().prepareStatement(query);
        res = statement.executeQuery();
        while (res.next()) {
            int id = res.getInt(1);
            int number = res.getInt(2);

            groups.add(new Group(
                            id,
                            number
                    )
            );
        }

        if(showTables) {
            printTables();
        }
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