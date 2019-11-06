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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO implements PersonTable<Teacher> {
    public void add(Teacher person) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(person);
        transaction.commit();
        session.close();
    }
    public Teacher selectById(int id) {
        Session session = null;
        Teacher teacher = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            teacher = session.load(Teacher.class, id);
        } catch (Exception e) {
            System.out.println("Hibernate error.");
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return teacher;
    }
    public List<Teacher> select(Criteria criteria, String value) {
        Query query = null;
        Session session = HibernateSessionFactory
                .getSessionFactory()
                .openSession();
        List<Teacher> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                session.close();
                return list;
            }
            case NAME : {
                query = session
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

                query = session
                        .createQuery("FROM Teacher WHERE Birthday = :birthday");
                query.setParameter("birthday", formattedBirth);
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                query = session
                        .createQuery("FROM Teacher WHERE Gender = :gender");
                query.setParameter("gender", gender.getValue());
                break;
            }
            case ALL : {
                query = session
                        .createQuery("FROM Teacher");
                break;
            }
        }
        list = query.list();
        session.close();
        return list;
    }
    public void update(Teacher person, Criteria criteria, String value) {
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
        session.merge(person);
        session.getTransaction().commit();
        session.close();
    }
    public void delete(Criteria criteria, String value) {
        List<Teacher> teachers = select(criteria,value);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for(Teacher teacher : teachers) {
            session.delete(teacher);
        }

        transaction.commit();
        session.close();
    }
    public void putTeacherInGroup(Teacher teacher, Group group) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        group.addTeacher(teacher);
        session.merge(group);
        transaction.commit();
        session.close();
    }
    public List<Group> getTeacherGroups(int id) {
        Teacher teacher = selectById(id);
        return teacher.getGroups();
    }
    public void removeTeacherFromGroup(Teacher teacher, Group group) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        group.removeTeacher(teacher);
        session.merge(group);
        transaction.commit();
        session.close();
    }

}
