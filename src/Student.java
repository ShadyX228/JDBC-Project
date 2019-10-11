import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class Student extends DBInfo implements SQLOperations<String> {
    private static int nextId = 0;
    private static String tableName = "student";
    private int student_id;
    private String name;
    private String birthday;
    private char sex;
    private int group_id;

    Student() throws SQLException {
            name = "NULL";
            birthday = "NULL";
            sex = 'N';
            group_id = NULL;
    }
    Student(String name, LocalDate birth, char sex, int group_id)
            throws
            SQLException {
        student_id = ++nextId;
        this.name = name;

        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        birthday = birth.format(df);

        this.sex = sex;
        this.group_id = group_id;

        String query = "INSERT INTO " + getDBName() + "." + tableName + " " +
                    "(student_id, Name, Birthday, Sex, group_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
        //System.out.println(student_id + " " + name + " " + birthday + " " + sex + " " + group_id);
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, student_id);
        statement.setString(2, this.name);
        statement.setString(3, birthday);
        statement.setObject(4, this.sex, java.sql.Types.CHAR);
        statement.setInt(5, this.group_id);
        statement.executeUpdate();
        statement.close();
    }

    public ArrayList<Integer> selectByCriteria(String criteria, String critValue) throws SQLException {
        String query = "SELECT * FROM " + getDBName() + "." + tableName + " " +
                "WHERE " + tableName + "." + criteria + " = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);

        if(criteria.equals("student_id") || criteria.equals("group_id")) {
            statement.setInt(1, Integer.parseInt(critValue));
        } else {
            statement.setString(1, critValue);
        }

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
        } else if(criteria.equals("Sex")) {
            setSex(critValue);
        } else {
            setGroup(critValue);
        }

        String query = "UPDATE " + getDBName() + "." + tableName + " " +
                "SET " + criteria + " = ? WHERE " + tableName + ".student_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);

        if(criteria.equals("group_id")) {
            statement.setInt(1, Integer.parseInt(critValue));
        } else {
            statement.setString(1, critValue);
        }
        statement.setInt(2, student_id);
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
    private void setGroup(String group_id) {
        this.group_id = Integer.parseInt(group_id);
    }

    public String toString() {
        return "student_id: " + student_id + "; name: " + name + "; " +
                "birthday: " + birthday + "; sex: " + sex +
                "; group_id: " + group_id;
    }

}
