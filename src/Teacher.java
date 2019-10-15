import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Teacher extends Table {
    private int id;
    private String name;
    private LocalDate birthday;
    private Gender gender;

    Teacher(String name, int year, int month, int day, Gender gender, int group_id)
            throws
            SQLException,
            IOException {
        this.name = name;
        birthday = LocalDate.of(year,month,day+1);
        this.gender = gender;
        setTableName(TableType.TEACHER);

        // inserting in db
        String query = "INSERT INTO " + getDBName() + "." + getTableName() + " " +
                "(Name, Birthday, Gender) " +
                "VALUES (?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, this.name);
        statement.setDate(2, java.sql.Date.valueOf(birthday));
        statement.setObject(3, this.gender.getValue(), java.sql.Types.CHAR);
        statement.executeUpdate();

        // get id from db
        id = setId();
        statement.close();
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

    public void setName(String name) {
        this.name = name;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String toString() {
        return "teacher_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; sex: " + gender;
    }
}
