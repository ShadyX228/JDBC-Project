package dbmodules.dao;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import hibernate.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static dbmodules.types.TableType.STUDENT;


public class StudentDAO implements PersonTable<Student> {
    public void add(Student person)
            throws SQLException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(person);
        tx1.commit();
        session.close();
    }
    public Student selectById(int id)
            throws SQLException, IOException {
        return HibernateSessionFactory.getSessionFactory().openSession().get(Student.class, id);
    }
    public List<Student> select(Criteria criteria, String value)
            throws SQLException, IOException {
        Query query = null;
        List<Student> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                break;
            }
            case NAME : {
                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Student WHERE Name = :name");
                query.setParameter("name", value);
                break;
            }
            case BIRTH : {
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth()
                );
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedBirth = dateTimeFormatter.format(birth);

                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Student WHERE Birthday = :birthday");
                query.setParameter("birthday", formattedBirth);
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Student WHERE Gender = :gender");
                query.setParameter("gender", gender.getValue());
                break;
            }
            case GROUP : {
                GroupDAO groupDAO = new GroupDAO();
                Group group = groupDAO.select(Integer.parseInt(value));

                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Student WHERE group_id = :group_id");
                query.setParameter("gender", group.getId());
                break;
            }
            case ALL : {
                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Student");
                break;
            }
        }
        list = query.list();
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
                        LocalDate.parse(value).getMonth().getValue(),
                        LocalDate.parse(value).getDayOfMonth()
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
                GroupDAO groupDAO = new GroupDAO();
                Group group = groupDAO.select(Integer.parseInt(value));
                person.setGroup(group);
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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBirth = dateTimeFormatter.format(person.getBirth());
        statement.setString(2, formattedBirth);

        statement.setObject(3, person.getGender().getValue(), java.sql.Types.CHAR);
        statement.setInt(4, person.getGroup().getId());
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


}