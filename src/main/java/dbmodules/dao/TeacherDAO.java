package dbmodules.dao;

import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
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

public class TeacherDAO implements PersonTable<Teacher> {
    public void add(Teacher person)
            throws SQLException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(person);
        transaction.commit();
        session.close();
    }
    public Teacher selectById(int id)
            throws SQLException {
        return HibernateSessionFactory.getSessionFactory().openSession().get(Teacher.class, id);

    }
    public List<Teacher> select(Criteria criteria, String value)
            throws SQLException, IOException {
        Query query = null;
        List<Teacher> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                return list;
            }
            case NAME : {
                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Teacher WHERE Name LIKE :name");
                query.setParameter("name", "%" + value + "%");
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
                        .createQuery("FROM Teacher WHERE Birthday = :birthday");
                query.setParameter("birthday", formattedBirth);
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Teacher WHERE Gender = :gender");
                query.setParameter("gender", gender.getValue());
                break;
            }
            case ALL : {
                query = HibernateSessionFactory
                        .getSessionFactory()
                        .openSession()
                        .createQuery("FROM Teacher");
                break;
            }
        }
        list = query.list();
        return list;
    }
    public void update(Teacher person, Criteria criteria, String value)
            throws SQLException, IOException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
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
        }
        session.update(person);
        session.getTransaction().commit();
        session.close();
    }
    public void delete(Criteria criteria, String value)
            throws SQLException, IOException {
        List<Teacher> teachers = select(criteria,value);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for(Teacher teacher : teachers) {
            session.delete(teacher);
        }

        transaction.commit();
        session.close();
    }
    public void putTeacherInGroup(Teacher teacher, Group group)
            throws SQLException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        group.addTeacher(teacher);
        session.merge(group);
        transaction.commit();
    }
    public List<Group> getTeacherGroups(int id) throws SQLException {
        Teacher teacher = selectById(id);
        return teacher.getGroups();
    }
    public void removeTeacherFromGroup(Teacher teacher, Group group)
            throws SQLException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        group.removeTeacher(teacher);
        session.merge(group);
        transaction.commit();
    }

}
