package dbmodules.dao;

import dbmodules.daointerfaces.PersonDAO;
import dbmodules.daointerfaces.StudentDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import org.springframework.stereotype.Repository;

import static webdebugger.WebInputDebugger.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;

import static dbmodules.types.Criteria.*;

@Repository
public class StudentDAOimpl implements StudentDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Student entity) {
        entityManager.persist(entity);
    }
    @Override
    public Student selectById(int id) {
        TypedQuery<Student> query = entityManager.createQuery("select s " +
                "from Student s join fetch s.group " +
                "where s.id = :id", Student.class);
        query.setParameter("id", id);
        return query.getResultList().get(0);
    }
    @Override
    public List<Student> select(String name,
                                LocalDate birth,
                                Gender gender,
                                Group group) {
        TypedQuery<Student> query = entityManager.createQuery("select s " +
                "from Student s join fetch s.group " +
                "where ((:name is not null and s.name like :name) or :name is null) " +
                "and ((:birthday is not null and s.birthday = :birthday) or :birthday is null) " +
                "and ((:group is not null and s.group  = :group) or :group is null) " +
                "and ((:gender is not null and s.gender = :gender) or :gender is null)", Student.class);
        query.setParameter("name", "%" + name + "%");
        query.setParameter("birthday", birth);
        query.setParameter("group", group);
        query.setParameter("gender", gender);
        return query.getResultList();
    }
    @Override
    public List<Student> selectByGroup(Group group) {
        TypedQuery<Student> query = entityManager.createQuery("select s " +
                "from Student s join fetch s.group " +
                "where s.group = :group", Student.class);
        query.setParameter("group", group);
        return query.getResultList();
    }
    @Override
    public List<Student> selectAll() {
        TypedQuery<Student> query = entityManager
                .createQuery("select s FROM Student s join fetch s.group", Student.class);
        return query.getResultList();
    }
    @Override
    public void update(Student person, Criteria criteria, String value) {

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
                GroupDAOimpl groupDAO = new GroupDAOimpl();
                Group group = groupDAO.select(Integer.parseInt(value));
                person.setGroup(group);
                break;
            }
        }
        entityManager.persist(person);
    }
    @Override
    public void update(Student person, String name,
                       LocalDate birth, Gender gender, Group group) {
        person.setName(name);
        person.setBirthday(birth);
        person.setGender(gender);
        person.setGroup(group);
        entityManager.persist(entityManager.merge(person));
    }
    @Override
    public void delete(Student entity) {
        entityManager.remove(entityManager.merge(entity));
    }
}