package dbmodules.dao;

import dbmodules.interfaces.GroupTable;
import dbmodules.tables.Group;
import hibernate.JPAUtil;
import javax.persistence.EntityManager;
import java.util.List;

public class GroupDAO extends JPAUtil<Group> implements GroupTable {
    public Group selectById(int id) {
        EntityManager entityManager = getEntityManager();
        Group entity = entityManager.find(Group.class, id);
        entityManager.close();
        return entity;
    }
    public Group select(int number)  {
        EntityManager entityManager = getEntityManager();
        Group group = (Group) entityManager
                .createQuery("FROM Group WHERE number = :number")
                .setParameter("number",number)
                .getResultList().get(0);
        entityManager.close();
        return group;
    }
    public List<Group> selectAll() {
        EntityManager entityManager = getEntityManager();
        List<Group> groups = entityManager.createQuery("FROM Group").getResultList();
        entityManager.close();
        return groups;
    }
}