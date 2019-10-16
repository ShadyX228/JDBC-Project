import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class Student extends Table {
    private int id;
    private String name;
    private LocalDate birthday;
    private Gender gender;
    private int group_id;

    Student(int id, String name, int year, int month, int day, Gender gender, int group_id)
            throws
            SQLException,
            IOException {
        this.id = id;
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        this.group_id = group_id;
        setTableName(TableType.STUDENT);
    }

    Student(String name, int year, int month, int day, Gender gender, int group_id)
            throws
            SQLException,
            IOException {
        this.name = name;
        birthday = LocalDate.of(year,month,day+1);
        this.gender = gender;
        this.group_id = group_id;
        setTableName(TableType.STUDENT);

        // insert in db
        add();

        // get id from db
        id = setId();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDate getBirth() {
        return birthday;
    }
    public int getGroup() {
        return group_id;
    }
    public Gender getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setGroup(int group_id) {
        this.group_id = group_id;
    }
    public void add() throws SQLException {
        String query = "INSERT INTO " + getDBName() + "." + getTableName() + " " +
                "(Name, Birthday, Gender) " +
                "VALUES (?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setDate(2, java.sql.Date.valueOf(birthday));
        statement.setObject(3, gender.getValue(), java.sql.Types.CHAR);
        statement.executeUpdate();
        statement.close();
    }

    public String toString() {
        return  "student_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender +
                "; group_id: " + group_id;
    }

}
