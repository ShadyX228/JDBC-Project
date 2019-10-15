import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Teacher extends DBInfo {
    private int teacher_id;
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

        // inserting in db
        String query = "INSERT INTO " + getDBName() + ".teacher " +
                "(Name, Birthday, Gender) " +
                "VALUES (?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, this.name);
        statement.setDate(2, java.sql.Date.valueOf(birthday));
        statement.setObject(3, this.gender.getValue(), java.sql.Types.CHAR);
        statement.executeUpdate();

        // get id from db
        query = "SELECT * FROM studentgroupteacher.teacher ORDER BY teacher.teacher_id DESC LIMIT 1";
        statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();
        if(res.next()) {
            teacher_id = res.getInt(1);
        }
        statement.close();
    }

    public int getId() {
        return teacher_id;
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
        return "teacher_id: " + teacher_id + "; name: " + name + "; " +
                "birthday: " + birthday + "; sex: " + gender;
    }
}
