package dbmodules.dao;

import dbmodules.daointerfaces.GroupDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Teacher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.InputMismatchException;
import java.util.List;

@Repository
public class GroupDAOimpl implements GroupDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Group entity) {
        entityManager.persist(entity);
    }
    @Override
    public Group selectById(int id) {
        return entityManager.find(Group.class, id);
    }
    @Override
    public Group select(int number)  {
        try {
            return (Group) entityManager
                    .createQuery("FROM Group WHERE number = :number")
                    .setParameter("number", number)
                    .getResultList().get(0);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<Group> selectAll() {
        return  entityManager.createQuery("FROM Group").getResultList();
    }
    @Override
    public List<Teacher> selectGroupTeachers(Group group) {
        group = entityManager.merge(group);
        return group.getTeachers();
    }
    @Override
    public void update(Group group, int number) {
        group = entityManager.merge(group);
        group.setNumber(number);
        entityManager.persist(group);
    }
    @Override
    public void delete(Group entity) {
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
    }
}