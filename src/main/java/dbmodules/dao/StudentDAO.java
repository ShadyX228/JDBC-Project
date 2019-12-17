package dbmodules.dao;

import dbmodules.interfaces.PersonTable;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import utilfactories.JPAUtil;

import static webdebugger.WebInputDebugger.*;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;

import static dbmodules.types.Criteria.*;

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
    public List<Student> select(HashMap<Criteria, String> criteriasMap) {
        List<Student> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("FROM Student");
        if(criteriasMap.containsKey(ID)) {
            Student student = selectById(Integer.parseInt(criteriasMap.get(ID)));
            if(!Objects.isNull(student)) {
                list.add(student);
            }
        } else {
            queryBuilder.append(" WHERE ");
            Group group = null;
            for (HashMap.Entry<Criteria,
                    String> element : criteriasMap.entrySet()) {
                switch (element.getKey()) {
                    case NAME: {
                        queryBuilder.append("Name LIKE :name AND ");
                        break;
                    }
                    case BIRTH: {
                        queryBuilder.append("birthday = :birthday AND ");
                        break;
                    }
                    case GENDER: {
                        queryBuilder.append("gender = :gender AND ");
                        break;
                    }
                    case GROUP: {
                        GroupDAO groupDAO = new GroupDAO();
                        int groupNumber = Integer
                                .parseInt(criteriasMap.get(GROUP));
                        group = checkGroup(groupNumber, groupDAO);

                        if(!Objects.isNull(group)) {
                            queryBuilder.append("group_id = :group_id AND ");
                            System.out.println(criteriasMap.get(GROUP));
                        }
                        groupDAO.closeEntityManager();
                        break;
                    }
                }
            }
            if(!criteriasMap.containsKey(ALL)) {
                if(!criteriasMap.isEmpty()) {
                    String query = queryBuilder.toString().substring(0, queryBuilder.toString().length() - 4);
                    Query execute = entityManager
                            .createQuery(query);
                    if (criteriasMap.containsKey(NAME)) {
                        execute.setParameter("name",
                                "%" + criteriasMap.get(NAME) + "%");
                    }
                    if (criteriasMap.containsKey(BIRTH)) {
                        LocalDate birth = LocalDate.of(
                                LocalDate.parse(criteriasMap.get(BIRTH))
                                        .getYear(),
                                LocalDate.parse(criteriasMap.get(BIRTH))
                                        .getMonth(),
                                LocalDate.parse(criteriasMap.get(BIRTH))
                                        .getDayOfMonth()
                        );
                        execute.setParameter("birthday", birth);
                    }
                    if (criteriasMap.containsKey(GENDER)) {
                        Gender gender = Gender
                                .valueOf(criteriasMap.get(GENDER));
                        execute.setParameter("gender", gender);
                    }

                    if (criteriasMap.containsKey(GROUP)) {
                        if(!Objects.isNull(group)) {
                            execute.setParameter("group_id",
                                    group.getId());
                        }
                    }

                    System.out.println(queryBuilder);

                    list = execute.getResultList();
                }

            } else {
                String allSelect = "FROM Student";
                list = entityManager
                        .createQuery(allSelect)
                        .getResultList();
            }
        }
        return list;
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
    public void update(Student person, String name,
                       LocalDate birth, Gender gender, Group group) {
        entityManager.getTransaction().begin();
        person.setName(name);
        person.setBirthday(birth);
        person.setGender(gender);
        person.setGroup(group);
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }
}