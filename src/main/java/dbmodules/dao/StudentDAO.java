package dbmodules.dao;

import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
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

public class StudentDAO implements PersonTable<Student> {
    public void add(Student person) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(person);
        transaction.commit();
        session.close();
    }
    public Student selectById(int id) {
        Session session = null;
        Student student = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            student = session.load(Student.class, id);
        } catch (Exception e) {
            System.out.println("Hibernate error.");
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return student;
    }
    public List<Student> select(Criteria criteria, String value) {
        Query query = null;
        Session session = HibernateSessionFactory
                .getSessionFactory()
                .openSession();
        List<Student> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                session.close();
                return list;
            }
            case NAME : {
                query = session
                        .createQuery("FROM Student WHERE Name LIKE :name");
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
                        .createQuery("FROM Student WHERE Birthday = :birthday");
                query.setParameter("birthday", formattedBirth);
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                query = session
                        .createQuery("FROM Student WHERE Gender = :gender");
                query.setParameter("gender", gender.getValue());
                break;
            }
            case GROUP : {
                GroupDAO groupDAO = new GroupDAO();
                Group group = groupDAO.select(Integer.parseInt(value));

                query = session
                        .createQuery("FROM Student WHERE group_id = :group_id");
                query.setParameter("group_id", group.getId());
                break;
            }
            case ALL : {
                query = session
                        .createQuery("FROM Student");
                break;
            }
        }
        list = query.list();
        session.close();
        return list;
    }
    public void update(Student person, Criteria criteria, String value) {
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
            case GROUP : {
                GroupDAO groupDAO = new GroupDAO();
                Group group = groupDAO.select(Integer.parseInt(value));
                person.setGroup(group);
                break;
            }
        }
        session.update(person);
        session.getTransaction().commit();
        session.close();
    }
    public void delete(Criteria criteria, String value) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List<Student> students = select(criteria,value);

        for(Student student : students) {
            session.delete(student);
        }

        transaction.commit();
        session.close();
    }

}