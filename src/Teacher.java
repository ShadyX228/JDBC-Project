import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Teacher extends Table {
    private static int nextId = 0;
    private int teacher_id;
    private String name;
    private String birthday;
    private char sex;

    Teacher(String name, LocalDate birth, char sex)
            throws
            SQLException {
        teacher_id = ++nextId;
        this.name = name;

        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        birthday = birth.format(df);

        this.sex = sex;

        String query = "INSERT INTO " + getDBName() + ".teacher " +
                "(teacher_id, Name, Birthday, Sex) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, teacher_id);
        statement.setString(2, this.name);
        statement.setString(3, birthday);
        statement.setObject(4, this.sex, java.sql.Types.CHAR);
        doUpdateQuery(statement);
    }
    public String toString() {
        return "teacher_id: " + teacher_id + "; name: " + name + "; " +
                "birthday: " + birthday + "; sex: " + sex;
    }
    public void putTeacherInGroup(int group_id)
            throws
            SQLException {
        String query = "INSERT INTO " + getDBName() + ".groupteacher " +
                "(id, group_id, teacher_id) " +
                "VALUES (NULL, ?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group_id);
        statement.setInt(2, teacher_id);
        statement.executeUpdate();
        statement.close();
    }
}
