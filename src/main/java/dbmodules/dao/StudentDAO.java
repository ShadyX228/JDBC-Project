package dbmodules.dao;

import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import utilfactories.JPAUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;

public class StudentDAO extends JPAUtil<Student> implements PersonTable<Student> {
    public Student selectById(int id) {
        return entityManager.find(Student.class, id);
    }
    public List<Student> select(Criteria criteria, String value) {
        List<Student> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                break;
            }
            case NAME : {
                list = entityManager
                        .createQuery("FROM Student WHERE Name LIKE :name")
                        .setParameter("name", "%" + value + "%")
                        .getResultList();
                break;
            }
            case BIRTH : {
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth()
                );

                list = entityManager
                        .createQuery("FROM Student WHERE birthday = :birthday")
                        .setParameter("birthday", birth)
                        .getResultList()
                ;
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                list = entityManager
                        .createQuery("FROM Student WHERE gender = :gender")
                        .setParameter("gender", gender)
                        .getResultList();
                break;
            }
            case GROUP  : {
                int groupNumber = Integer.parseInt(value);
                Group group = new GroupDAO().select(groupNumber);
                list = entityManager
                        .createQuery("FROM Student WHERE group_id = :group_id")
                        .setParameter("group_id", group.getId())
                        .getResultList();
                break;
            }
            case ALL : {
                list = entityManager
                        .createQuery("FROM Student")
                        .getResultList();
                break;
            }
        }
        return list;
    }
    public String select(List<Criteria> criterias, List<String> values) {
        List<Student> list = new ArrayList<>();
        String query = "FROM Student WHERE";
        for(Criteria criteria : criterias) {
            switch (criteria) {
                case ID : {
                    list.add(selectById(Integer.parseInt(values.get(0))));
                    break;
                }
                case ALL : {
                    query = "FROM Student";
                    break;
                }
                case NAME : {
                    query += "Name LIKE :name AND";
                }
                case BIRTH:  {
                    query += "birthday = :birthday AND";
                }
                case GENDER: {
                    query += "gender = :gender AND";
                }
                case GROUP: {
                    query += "group_id = :group_id AND";
                }
            }
            query = query.substring(0, query.length()-2);
            System.out.println(query);
        }
        /*switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                break;
            }
            case NAME : {
                list = entityManager
                        .createQuery("FROM Student WHERE Name LIKE :name")
                        .setParameter("name", "%" + value + "%")
                        .getResultList();
                break;
            }
            case BIRTH : {
                LocalDate birth = LocalDate.of(
                        LocalDate.parse(value).getYear(),
                        LocalDate.parse(value).getMonth(),
                        LocalDate.parse(value).getDayOfMonth()
                );

                list = entityManager
                        .createQuery("FROM Student WHERE birthday = :birthday")
                        .setParameter("birthday", birth)
                        .getResultList()
                ;
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                list = entityManager
                        .createQuery("FROM Student WHERE gender = :gender")
                        .setParameter("gender", gender)
                        .getResultList();
                break;
            }
            case GROUP  : {
                int groupNumber = Integer.parseInt(value);
                Group group = new GroupDAO().select(groupNumber);
                list = entityManager
                        .createQuery("FROM Student WHERE group_id = :group_id")
                        .setParameter("group_id", group.getId())
                        .getResultList();
                break;
            }
            case ALL : {
                list = entityManager
                        .createQuery("FROM Student")
                        .getResultList();
                break;
            }
        }*/
        return query;
    }
    public void update(Student person, Criteria criteria, String value) {
        entityManager.getTransaction().begin();

        person = entityManager.merge(person);
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
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }
    public void update(Student person, String name, LocalDate birth, Gender gender, Group group) {
        entityManager.getTransaction().begin();
        person.setName(name);
        person.setBirthday(birth);
        person.setGender(gender);
        person.setGroup(group);
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }
}