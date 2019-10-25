package dbmodules.dao;

import dbmodules.interfaces.StudentTable;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Table;
import dbmodules.tables.Teacher;
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
import java.util.Objects;

public class StudentDAO implements StudentTable {
    public List<Student> selectStudent(Criteria criteria, String value)
            throws SQLException {
        String query = "SELECT * FROM studentgroupteacher.student";
        PreparedStatement statement = getConnection().
                prepareStatement(query);
        List<Student> list = new ArrayList<>();

        switch (criteria) {
            case ID:
                list.add(selectById(
                        TableType.STUDENT,
                        Integer.parseInt(value)
                ));
                break;
            case NAME:
                query = "SELECT * FROM studentgroupteacher.student"
                        + " WHERE student.Name = ?";
                statement = getConnection().prepareStatement(query);
                statement.setString(1, value);

                ResultSet res = statement.executeQuery();
                while(res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            case BIRTH:
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth() + 1
                );
                //System.out.println(birth);
                query = "SELECT * FROM studentgroupteacher.student " +
                        "WHERE student.Birthday = ?";
                statement = getConnection().prepareStatement(query);
                statement.setDate(1, java.sql.Date.valueOf(birth));

                res = statement.executeQuery();
                while(res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            case GENDER:
                Gender gender = Gender.valueOf(value);
                query = "SELECT * FROM studentgroupteacher.student" +
                        " WHERE student.Gender = ?";
                statement = getConnection().prepareStatement(query);
                statement.setObject(1, gender.getValue(),
                        java.sql.Types.CHAR);

                res = statement.executeQuery();
                while(res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            case GROUP:
                Group group = selectGroup(Integer.parseInt(value));
                int group_id = group.getId();
                query = "SELECT * FROM studentgroupteacher." +
                        "student WHERE student.group_id = ?";
                statement = getConnection().prepareStatement(query);
                statement.setInt(1, group_id);

                res = statement.executeQuery();
                while(res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
            case ALL :
                res = statement.executeQuery();
                while(res.next()) {
                    list.add(getStudentFromRS(res));
                }
                break;
        }
        statement.close();
        return list;
    }
    public Student selectById(int id)
            throws SQLException, IOException {
        String query = "SELECT * FROM studentgroupteacher.student WHERE student_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();

        if (res.next()) {
            int s_id = res.getInt(1);
            String s_name = res.getString(2);
            LocalDate s_birth = res.getDate(3).toLocalDate();
            Gender s_gender = Gender.valueOf(res.getString(4));
            int s_group_id;
            if(res.getInt(5) != java.sql.Types.NULL) {
                s_group_id = res.getInt(5);
            } else {
                s_group_id = java.sql.Types.NULL;
            }

            statement.close();
            return new Student(
                    s_id,
                    s_name,
                    s_birth.getYear(),
                    s_birth.getMonth().getValue(),
                    s_birth.getDayOfMonth(),
                    s_gender,
                    s_group_id
            );

        }

        return null;
    }
    public void addStudent(Student student) throws SQLException {
        student.add();
    }

    public void updateStudent(int id, Criteria criteria, String value)
            throws SQLException, IOException {
        Student student = selectById(TableType.STUDENT,id);
        if(!Objects.isNull(student)) {
            String query = "SELECT * FROM studentgroupteacher.student";
            PreparedStatement statement = getConnection()
                    .prepareStatement(query);

            switch (criteria) {
                case NAME:
                    student.setName(value);
                    break;
                case BIRTH:
                    LocalDate birth = LocalDate.of(
                            LocalDate.parse(value).getYear(),
                            LocalDate.parse(value).getMonth(),
                            LocalDate.parse(value).getDayOfMonth() + 1
                    );
                    student.setBirthday(birth);
                    break;
                case GENDER:
                    Gender newGender = Gender.valueOf(value);
                    student.setGender(newGender);
                    break;
                case GROUP:
                    Group group = selectGroup(Integer.parseInt(value));
                    int group_id = group.getId();
                    student.setGroup(group_id);
                    break;
            }
            statement.close();
        }
    }
    public void deleteStudent(Criteria criteria, String value)
            throws SQLException, IOException {
        List<Student> students = selectStudent(criteria,value);
        for(Student s: students) {
            s.delete(criteria);
        }
    }

}
