import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Teacher extends DBInfo implements SQLOperations<String> {
    private static int nextId = 0;
    private static String tableName = "teacher";
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

        String query = "INSERT INTO " + getDBName() + "." + tableName + " " +
                "(teacher_id, Name, Birthday, Sex) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, teacher_id);
        statement.setString(2, this.name);
        statement.setString(3, birthday);
        statement.setObject(4, this.sex, java.sql.Types.CHAR);
        statement.executeUpdate();
        statement.close();
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
    public ArrayList<Integer> selectByCriteria(String criteria, String critValue) throws SQLException {
        String query = "SELECT * FROM " + getDBName() + "." + tableName + " " +
                "WHERE " + tableName + "." + criteria + " = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, critValue);

        ResultSet res = statement.executeQuery();
        ArrayList<Integer> list = new ArrayList<>();
        while(res.next()) {
            list.add(res.getInt(1));
        }
        statement.close();
        return list;
    }
    public void updateByCriteria(String criteria, String critValue) throws SQLException {
        if(criteria.equals("Name")) {
            setName(critValue);
        } else if(criteria.equals("Birthday")) {
            setBirthday(critValue);
        } else {
            setSex(critValue);
        }

        String query = "UPDATE " + getDBName() + "." + tableName + " " +
                "SET " + criteria + " = ? WHERE " + tableName + ".teacher_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, critValue);
        statement.setInt(2, teacher_id);
        statement.executeUpdate();
        statement.close();
    }
    public void deleteByCriteria(String criteria, String critValue) throws SQLException {
        String query = "DELETE FROM " + getDBName() + "." + tableName + " " +
                "WHERE " + tableName + "." + criteria + " = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, critValue);
        statement.executeUpdate();
        statement.close();
        nextId--;
    }

    private void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    private void setSex(String sex) {
        this.sex = sex.charAt(0);
    }
    private void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "teacher_id: " + teacher_id + "; name: " + name + "; " +
                "birthday: " + birthday + "; sex: " + sex;
    }
}
