package dbmodules.dao;

import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import utilfactories.JPAUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO extends JPAUtil<Teacher> implements PersonTable<Teacher> {
    public Teacher selectById(int id) {
        return entityManager.find(Teacher.class, id);
    }
    public List<Teacher> select(Criteria criteria, String value) {
        List<Teacher> list = new ArrayList<>();

        switch (criteria) {
            case ID : {
                list.add(selectById(Integer.parseInt(value)));
                break;
            }
            case NAME : {
                list = entityManager
                        .createQuery("FROM Teacher WHERE Name LIKE :name")
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
                        .createQuery("FROM Teacher WHERE birthday = :birthday")
                        .setParameter("birthday", birth)
                        .getResultList()
                ;
                break;
            }
            case GENDER : {
                Gender gender = Gender.valueOf(value);
                list = entityManager
                        .createQuery("FROM Teacher WHERE gender = :gender")
                        .setParameter("gender", gender)
                        .getResultList();
                break;
            }
            case ALL : {
                list = entityManager
                        .createQuery("FROM Teacher")
                        .getResultList();
                break;
            }
        }
        return list;
    }
    public void update(Teacher person, Criteria criteria, String value) {
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
        }
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }
    public void putTeacherInGroup(Teacher teacher, Group group) {
        entityManager.getTransaction().begin();
        teacher.addGroup(group);
        entityManager.merge(teacher);
        entityManager.getTransaction().commit();
    }
    public List<Group> getTeacherGroups(int id) {
        Teacher teacher = selectById(id);
        return teacher.getGroups();
    }
    public void removeTeacherFromGroup(Teacher teacher, Group group) {
        entityManager.getTransaction().begin();
        teacher = entityManager.merge(teacher);
        group = entityManager.merge(group);
        teacher.removeGroup(group);
        entityManager.getTransaction().commit();
    }
}
