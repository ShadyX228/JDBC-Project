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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class StudentDAO implements PersonTable<Student> {
    public void add(Student person)
            throws SQLException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(person);
        transaction.commit();
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
        Query query = HibernateSessionFactory
                .getSessionFactory()
                .openSession()
                .createQuery("UPDATE student SET Name = :name, Birthday = :birth, Gender = :gender, Group = :group_id WHERE student_id = :student_id");

        query.setParameter("name",person.getName());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBirth = dateTimeFormatter.format(person.getBirth());
        query.setParameter("birth",formattedBirth);

        query.setParameter("gender",person.getGender().getValue());
        query.setParameter("group_id",person.getGroup().getId());
        query.setParameter("student_id",person.getId());

    }
    public void delete(Criteria criteria, String value)
            throws SQLException, IOException {
        List<Student> students = select(criteria,value);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for(Student student : students) {
            session.delete(student);
        }

        transaction.commit();
        session.close();

    }

}