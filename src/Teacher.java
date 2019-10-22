import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Teacher extends Table {
    private int id;
    private String name;
    private LocalDate birthday;
    private Gender gender;
    private ArrayList<Group> groups = new ArrayList<>();


    Teacher(int id, String name, int year, int month, int day, Gender gender)
            throws
            SQLException,
            IOException {
        this.id = id;
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        setTableName(TableType.TEACHER);
    }
    Teacher(String name, int year, int month, int day, Gender gender)
            throws
            SQLException,
            IOException {
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        setTableName(TableType.TEACHER);

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
    public Gender getGender() {
        return gender;
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setName(String name) throws SQLException {
        this.name = name;
        String query = "UPDATE studentgroupteacher.teacher SET " +
                "teacher.Name = ? " +
                "WHERE teacher.teacher_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setInt(2, id);
        statement.executeUpdate();
        statement.close();
    }
    public void setBirthday(LocalDate birthday) throws SQLException {
        this.birthday = birthday;
        String query = "UPDATE studentgroupteacher.teacher SET " +
                "teacher.Birthday = ? WHERE teacher.teacher_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setDate(1, java.sql.Date.valueOf(birthday));
        statement.setInt(2, id);
        statement.executeUpdate();
        statement.close();
    }
    public void setGender(Gender gender) throws SQLException {
        this.gender = gender;
        String query = "UPDATE studentgroupteacher.teacher SET " +
                "teacher.Gender = ? " +
                "WHERE teacher.teacher_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setObject(1, gender.getValue(),
                java.sql.Types.CHAR);
        statement.setInt(2, id);
        statement.executeUpdate();
        statement.close();
    }
    public void addGroup(Group group) throws SQLException {
        String query = "INSERT INTO studentgroupteacher.groupteacher " +
                "(group_id, teacher_id) VALUES (?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group.getId());
        statement.setInt(2, id);
        statement.executeUpdate();
    }
    public void restoreGroupFromDB(Group group) {
        groups.add(group);
    }
    public void add() throws SQLException {
        String query = "INSERT INTO " + getDBName() + "."
                + getTableName() + " " +
                "(Name, Birthday, Gender) " +
                "VALUES (?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setDate(2, java.sql.Date.valueOf(birthday));
        statement.setObject(3, gender.getValue(), java.sql.Types.CHAR);
        statement.executeUpdate();
        statement.close();
    }
    public void delete(Criteria criteria) throws SQLException {
        String query;
        PreparedStatement statement;
        switch (criteria) {
            case ID:
                query = "DELETE FROM studentgroupteacher.teacher " +
                        "WHERE teacher.teacher_id = ?";
                statement = getConnection().prepareStatement(query);
                statement.setInt(1,id);
                break;
            case NAME:
                query = "DELETE FROM studentgroupteacher.teacher " +
                        "WHERE teacher.Name = ?";
                statement = getConnection().prepareStatement(query);
                statement.setString(1, name);
                break;
            case BIRTH:
                query = "DELETE FROM studentgroupteacher.teacher " +
                        "WHERE teacher.Birthday = ?";
                statement = getConnection().prepareStatement(query);
                statement.setDate(1, java.sql.Date.valueOf(
                        LocalDate.of(
                                birthday.getYear(),
                                birthday.getMonth(),
                        birthday.getDayOfMonth() + 1
                        )
                ));
                break;
            case GENDER:
                query = "DELETE FROM studentgroupteacher.teacher " +
                        "WHERE teacher.Gender = ?";
                statement = getConnection().prepareStatement(query);
                statement.setObject(1, gender.getValue(),
                        java.sql.Types.CHAR);
                break;
            default:
                System.out.println("No entered critera. Using ID.");
                query = "DELETE FROM studentgroupteacher.teacher " +
                        "WHERE teacher.teacher_id = ?";
                statement = getConnection().prepareStatement(query);
                statement.setInt(1,id);
        }
        statement.executeUpdate();
        statement.close();
    }

    public String toString() {
        return "teacher_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender;
    }
}
