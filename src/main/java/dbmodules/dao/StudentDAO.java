package dbmodules.dao;

import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import dbmodules.types.TableType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class StudentDAO extends TableDAO implements PersonTable<Student> {
    public StudentDAO() throws IOException, SQLException {
        super(TableType.STUDENT);
    }

    public void add(Student person)
            throws SQLException {
        String query = "INSERT INTO " + getDBName() + "."
                + getTableName() + " " +
                "(Name, Birthday, Gender, group_id) " +
                "VALUES (?, ?, ?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, person.getName());
        statement.setDate(2, java.sql.Date.valueOf(person.getBirth()));
        statement.setObject(3, person.getGender().getValue(), java.sql.Types.CHAR);
        statement.setInt(4, person.getGroup_id());
        statement.executeUpdate();
        statement.close();
    }
    public Student selectById(int id)
            throws SQLException, IOException  {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName() + " WHERE " + getTableName() + "." + getTableName() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();

        if (res.next()) {
            int s_id = res.getInt(1);
            String name = res.getString(2);
            LocalDate birth = res.getDate(3).toLocalDate();
            Gender gender = Gender.valueOf(res.getString(4));
            int group_id = res.getInt(5);
            statement.close();

            return new Student(
                        s_id,
                        name,
                        birth.getYear(),
                        birth.getMonth().getValue(),
                        birth.getDayOfMonth(),
                        gender,
                        group_id
                );
        }
        return null;
    }
    public List<Student> select(Criteria criteria, String value)
            throws SQLException, IOException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName();
        PreparedStatement statement = getConnection().
                prepareStatement(query);
        List<Student> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                break;
            }
            case NAME : {
                query = "SELECT * FROM " + getDBName() + "."
                        + getTableName() + " WHERE " + getTableName() + ".Name = ?";
                statement = getConnection().prepareStatement(query);
                statement.setString(1, value);

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            }
            case BIRTH : {
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth() + 1
                );
                query = "SELECT * FROM " + getDBName() + "."
                        + getTableName() + " WHERE " + getTableName() + ".Birthday= ?";
                statement = getConnection().prepareStatement(query);
                statement.setDate(1, java.sql.Date.valueOf(birth));

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                query = "SELECT * FROM " + getDBName() + "."
                        + getTableName() + " WHERE " + getTableName() + ".Gender = ?";
                statement = getConnection().prepareStatement(query);
                statement.setObject(1, gender.getValue(),
                        java.sql.Types.CHAR);

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            }
            case GROUP : {
                GroupDAO gd = new GroupDAO();

                Group group = gd.select(Integer.parseInt(value));

                query = "SELECT * FROM " + getDBName() + "."
                        + getTableName() + " WHERE " + getTableName() + ".group_id= ?";
                statement = getConnection().prepareStatement(query);
                statement.setInt(1, group.getId());

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            }
            case ALL : {
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            }
        }
        statement.close();
        return list;
    }
    public void update(Student person, Criteria criteria, String value)
            throws SQLException, IOException {
        switch (criteria) {
            case NAME : {
                person.setName(value);
                break;
            }
            case BIRTH : {
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth() + 1
                );
                person.setBirthday(birth);
                break;
            }
            case GENDER : {
                Gender newGender = Gender.valueOf(value);
                person.setGender(newGender);
                break;
            }
            case GROUP : {
                GroupDAO gd = new GroupDAO();
                Group group = gd.select(Integer.parseInt(value));
                int group_id = group.getId();
                person.setGroup_id(group_id);
                break;
            }
        }
        String query = "UPDATE " + getDBName() + "." + getTableName()
                + " SET Name = ?, Birthday = ?, Gender = ?, group_id = ? " +
                "WHERE " + getTableName() + "."
                + getTableName() + "_id = ?;";
        PreparedStatement statement = getConnection()
                .prepareStatement(query);
        statement.setString(1, person.getName());
        statement.setDate(2, java.sql.Date.valueOf(person.getBirth()));
        statement.setObject(3, person.getGender().getValue(), java.sql.Types.CHAR);
        statement.setInt(4, person.getGroup_id());
        statement.setInt(5, person.getId());
        statement.executeUpdate();
        statement.close();
    }
    public void delete(Criteria criteria, String value)
            throws SQLException, IOException {
        List<Student> students = select(criteria,value);
        String query = "DELETE FROM " + getDBName() + "."
                + getTableName() +
                " WHERE " + getTableName() + "."
                + getTableName() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        for(Student student : students) {
            statement.setInt(1, student.getId());
            statement.executeUpdate();
        }
    }



    private Student getStudentFromRS(ResultSet res)
            throws SQLException {
        int id = res.getInt(1);
        String name = res.getString(2);
        LocalDate birth = res.getDate(3).toLocalDate();
        Gender gender = Gender.valueOf(res.getString(4));
        int group_id = res.getInt(5);
        return new Student(
                id,
                name,
                birth.getYear(),
                birth.getMonth().getValue(),
                birth.getDayOfMonth(),
                gender,
                group_id
        );
    }
}