package dbmodules.tables;

import dbmodules.types.*;

import java.io.IOException;
import java.sql.*;
import java.time.*;

public class Student extends Table {
    private int id;
    private String name;
    private LocalDate birthday;
    private Gender gender;
    private Group group;

    public Student(int id, String name, int year, int month, int day,
            Gender gender, int group_id)
            throws
            SQLException,
            IOException {
        this.id = id;
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        setGroup(group_id);
        setTableName(TableType.STUDENT);
    }

    public Student(String name, int year, int month, int day,
            Gender gender, int group_id)
            throws
            SQLException,
            IOException {
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        setGroup(group_id);
        setTableName(TableType.STUDENT);

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
    public Group getGroup() {
        return group;
    }
    public Gender getGender() {
        return gender;
    }

    public void setName(String name) throws SQLException {
        this.name = name;
        String query = "UPDATE studentgroupteacher.student SET " +
                "student.Name = ? " +
                "WHERE student.student_id = ?";
        PreparedStatement statement = getConnection().
                prepareStatement(query);
        statement.setString(1, name);
        statement.setInt(2, id);
        statement.executeUpdate();
        statement.close();
    }
    public void setBirthday(LocalDate birthday) throws SQLException {
        this.birthday = birthday;
        String query = "UPDATE studentgroupteacher.student SET " +
                "student.Birthday = ? " +
                "WHERE student.student_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setDate(1, java.sql.Date.valueOf(birthday));
        statement.setInt(2, id);
        statement.executeUpdate();
        statement.close();
    }
    public void setGender(Gender gender) throws SQLException {
        this.gender = gender;
        String query = "UPDATE studentgroupteacher.student SET " +
                "student.database.types.Gender = ? WHERE student.student_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setObject(1, gender.getValue(),
                java.sql.Types.CHAR);
        statement.setInt(2, id);
        statement.executeUpdate();
        statement.close();
    }
    public void setGroup(int group_id) throws SQLException, IOException {
        String query = "UPDATE studentgroupteacher.student SET " +
                "student.group_id = ? " +
                "WHERE student.student_id = ?";
        PreparedStatement statement = getConnection()
                .prepareStatement(query);
        statement.setObject(1, group_id,
                java.sql.Types.CHAR);
        statement.setInt(2, id);
        statement.executeUpdate();

        query = "SELECT * from studentgroupteacher.group " +
                "WHERE group.group_id = ?";
        statement = getConnection().prepareStatement(query);
        statement.setInt(1,group_id);

        ResultSet res = statement.executeQuery();
        if (res.next()) {
            group = new Group(res.getInt(1), res.getInt(2));
        }

        statement.close();
    }
    public void add() throws SQLException {
        String query = "INSERT INTO " + getDBName() + "."
                + getTableName() + " " +
                "(Name, Birthday, database.types.Gender, group_id) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setDate(2, java.sql.Date.valueOf(birthday));
        statement.setObject(3, gender.getValue(), java.sql.Types.CHAR);
        statement.setInt(4, group.getId());
        statement.executeUpdate();
        statement.close();
    }
    public void delete(Criteria criteria) throws SQLException {
        String query;
        PreparedStatement statement;
        switch (criteria) {
            case ID:
                query = "DELETE FROM studentgroupteacher.student " +
                        "WHERE student.student_id = ?";
                statement = getConnection().prepareStatement(query);
                statement.setInt(1,id);
                break;
            case NAME:
                query = "DELETE FROM studentgroupteacher.student " +
                        "WHERE student.Name = ?";
                statement = getConnection().prepareStatement(query);
                statement.setString(1, name);
                break;
            case BIRTH:
                query = "DELETE FROM studentgroupteacher.student " +
                        "WHERE student.Birthday = ?";
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
                query = "DELETE FROM studentgroupteacher.student " +
                        "WHERE student.database.types.Gender = ?";
                statement = getConnection().prepareStatement(query);
                statement.setObject(1, gender.getValue(),
                        java.sql.Types.CHAR);
                break;
            case GROUP:
                query = "DELETE FROM studentgroupteacher.student " +
                        "WHERE student.group_id = ?";
                statement = getConnection().prepareStatement(query);
                statement.setInt(1, group.getId());
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
        return  "student_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender +
                "; group_id: " + group.getId();
    }

}