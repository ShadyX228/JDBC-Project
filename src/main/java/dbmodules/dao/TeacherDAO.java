package dbmodules.dao;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static dbmodules.types.TableType.TEACHER;

public class TeacherDAO extends TableDAO implements PersonTable<Teacher> {
    public TeacherDAO() throws IOException, SQLException {
        super(TEACHER);
    }

    public void add(Teacher person)
            throws SQLException {
        String query = "INSERT INTO " + getDBName() + "."
                + getTableName() + " " +
                "(Name, Birthday, Gender) " +
                "VALUES (?, ?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, person.getName());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBirth = dateTimeFormatter.format(person.getBirth());
        statement.setString(2, formattedBirth);

        statement.setObject(3, person.getGender().getValue(), java.sql.Types.CHAR);
        statement.executeUpdate();
        statement.close();
    }
    public Teacher selectById(int id)
            throws SQLException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName() + " WHERE " + getTableName() + "." + getTableName() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();

        Teacher teacher = null;

        boolean check = false;
        if (res.next()) {
            int s_id = res.getInt(1);
            String name = res.getString(2);
            LocalDate birth = res.getDate(3).toLocalDate();
            Gender gender = Gender.valueOf(res.getString(4));

            teacher = new Teacher(
                    s_id,
                    name,
                    birth.getYear(),
                    birth.getMonth().getValue(),
                    birth.getDayOfMonth(),
                    gender
            );
            check = true;
        }
        if(check) {
            query = "SELECT * FROM " + getDBName() + ".groupteacher " +
                    "WHERE groupteacher.teacher_id = ?;";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1, teacher.getId());
            res = statement.executeQuery();

            while(res.next()) {
                Group group;
                try {
                    group = new GroupDAO().selectById(res.getInt(2));
                } catch (IOException e) {
                    statement.close();
                    System.out.println("Some error occured while selecting teacher by id.");
                    return null;
                }
                teacher.addGroup(group);
            }
        }
        statement.close();
        return teacher;
    }
    public List<Teacher> select(Criteria criteria, String value)
            throws SQLException, IOException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName();
        PreparedStatement statement = getConnection().
                prepareStatement(query);
        List<Teacher> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                break;
            }
            case NAME : {
                query = "SELECT * FROM " + getDBName() + "."
                        + getTableName() + " WHERE " + getTableName() + "ые.Name LIKE ?";
                statement = getConnection().prepareStatement(query);
                statement.setString(1, "%" + value + "%");

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getTeacherFromRS(res));
                }
                break;
            }
            case BIRTH : {
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth() + 1
                );
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedBirth = dateTimeFormatter.format(birth);

                query = "SELECT * FROM " + getDBName() + "."
                        + getTableName() + " WHERE " + getTableName() + ".Birthday= ?";
                statement = getConnection().prepareStatement(query);
                statement.setString(1, formattedBirth);

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getTeacherFromRS(res));
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
                    list.add(getTeacherFromRS(res));
                }
                break;
            }
            case ALL : {
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    list.add(getTeacherFromRS(res));
                }
                break;
            }
        }
        statement.close();
        return list;
    }
    public void update(Teacher person, Criteria criteria, String value)
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
        }
        String query = "UPDATE " + getDBName() + "." + getTableName()
                + " SET Name = ?, Birthday = ?, Gender = ?" +
                "WHERE " + getTableName() + "."
                + getTableName() + "_id = ?;";
        PreparedStatement statement = getConnection()
                .prepareStatement(query);
        statement.setString(1, person.getName());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBirth = dateTimeFormatter.format(person.getBirth());
        statement.setString(2, formattedBirth);

        statement.setObject(3, person.getGender().getValue(), java.sql.Types.CHAR);
        statement.setInt(4, person.getId());
        statement.executeUpdate();
        statement.close();
    }
    public void delete(Criteria criteria, String value)
            throws SQLException, IOException {
        List<Teacher> students = select(criteria,value);
        String query = "DELETE FROM " + getDBName() + "."
                + getTableName() +
                " WHERE " + getTableName() + "."
                + getTableName() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        for(Teacher teacher : students) {
            statement.setInt(1, teacher.getId());
            statement.executeUpdate();
        }
    }
    public void putTeacherInGroup(Teacher teacher, Group group)
            throws SQLException {
        String query = "INSERT INTO " + getDBName() + ".groupteacher"
                + " " +
                "(group_id, teacher_id) " +
                "VALUES (?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group.getId());
        statement.setInt(2, teacher.getId());

        statement.executeUpdate();
        statement.close();
    }
    public List<Group> getTeacherGroups(int id) throws SQLException {
        Teacher teacher = selectById(id);
        return teacher.getGroups();
    }
    public void removeTeacherFromGroup(Teacher teacher, Group group)
            throws SQLException {
        String query = "DELETE FROM " + getDBName() + ".groupteacher"
                + " WHERE group_id = ? AND teacher_id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group.getId());
        statement.setInt(2, teacher.getId());

        statement.executeUpdate();
        statement.close();
    }


    private Teacher getTeacherFromRS(ResultSet res)
            throws SQLException {
        int t_id = res.getInt(1);
        String name = res.getString(2);
        LocalDate birth = res.getDate(3).toLocalDate();
        Gender gender = Gender.valueOf(res.getString(4));

        return new Teacher(
                t_id,
                name,
                birth.getYear(),
                birth.getMonth().getValue(),
                birth.getDayOfMonth(),
                gender
        );
    }
}
