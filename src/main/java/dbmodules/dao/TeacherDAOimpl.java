package dbmodules.dao;

import dbmodules.daointerfaces.TeacherDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.entity.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;

@Repository
public class TeacherDAOimpl implements TeacherDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void add(Teacher entity) {
        entityManager.persist(entity);
    }

    @Override
    public Teacher selectById(int id) {
        TypedQuery<Teacher> query = entityManager.createQuery("select t " +
                "from Teacher t left join fetch t.groups " +
                "where t.id = :id", Teacher.class);
        query.setParameter("id", id);

        try {
            return query.getResultList().get(0);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Teacher> select(String name, LocalDate birth, Gender gender) {
        TypedQuery<Teacher> query = entityManager.createQuery("select t " +
                "from Teacher t left join fetch t.groups " +
                "where ((:name is not null and t.name like :name) or :name is null) " +
                "and ((:birthday is not null and t.birthday = :birthday) or :birthday is null) " +
                "and ((:gender is not null and t.gender = :gender) or :gender is null)", Teacher.class);
        query.setParameter("name", "%" + name + "%");
        query.setParameter("birthday", birth);
        query.setParameter("gender", gender);
        return query.getResultList();
    }
    @Override
    public List<Teacher> select(int first, int last, String search) {
        System.out.print(search);
        TypedQuery<Teacher> query = entityManager.createQuery("select t " +
                "from Teacher t left join fetch t.groups " +
                "where t.name like :filter " +
                "or t.birthday like :filter " +
                "or t.gender like :filter", Teacher.class);
        query.setParameter("filter", "%" + search + "%");
        return query.setFirstResult(first).setMaxResults(last).getResultList();
    }

    @Override
    public List<Teacher> selectAll() {
        TypedQuery<Teacher> query = entityManager
                .createQuery("FROM Teacher", Teacher.class);
        return query.getResultList();
    }
    @Override
    public List<Teacher> selectAll(int first, int last) {
        TypedQuery<Teacher> query = entityManager
                .createQuery("FROM Teacher", Teacher.class)
                .setFirstResult(first).setMaxResults(last);
        return query.getResultList();
    }
    @Override
    public void update(Teacher person, Criteria criteria, String value) {

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
    }
    @Override
    public void update(Teacher person, String name, LocalDate birth, Gender gender) {
        person.setName(name);
        person.setBirthday(birth);
        person.setGender(gender);
        entityManager.persist(entityManager.merge(person));
    }
    @Override
    public void putTeacherInGroup(Teacher teacher, Group group) {
        teacher = entityManager.merge(teacher);
        group = entityManager.merge(group);
        teacher.addGroup(group);
        entityManager.merge(teacher);
    }
    @Override
    public List<Group> getTeacherGroups(int id) {
        Teacher teacher = selectById(id);
        return teacher.getGroups();
    }
    @Override
    public void removeTeacherFromGroup(Teacher teacher, Group group) {
        teacher = entityManager.merge(teacher);
        group = entityManager.merge(group);
        teacher.removeGroup(group);
    }
    @Override
    public void delete(Teacher entity) {
        entityManager.remove(entityManager.merge(entity));
    }
}
