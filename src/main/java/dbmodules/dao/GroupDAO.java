package dbmodules.dao;

import dbmodules.entity.BaseEntity;
import dbmodules.service.GroupService;
import dbmodules.entity.Group;
import utilfactories.JPAUtil;

import java.util.List;

public class GroupDAO extends JPAUtil implements GroupService {
    public void add(Group entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }
    public Group selectById(int id) {
        return entityManager.find(Group.class, id);
    }
    public Group select(int number)  {
        return (Group) entityManager
                .createQuery("FROM Group WHERE number = :number")
                .setParameter("number",number)
                .getResultList().get(0);
    }
    public List<Group> selectAll() {
        return  entityManager.createQuery("FROM Group").getResultList();
    }
    public void update(Group group, int number) {
        entityManager.getTransaction().begin();
        group.setNumber(number);
        entityManager.persist(group);
        entityManager.getTransaction().commit();
    }
    public void delete(Group entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }
}