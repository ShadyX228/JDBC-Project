package dbmodules.dao;

import dbmodules.interfaces.GroupTable;
import dbmodules.tables.Group;
import utilfactories.JPAUtil;
import javax.persistence.EntityManager;
import java.util.List;

public class GroupDAO extends JPAUtil<Group> implements GroupTable {
    private EntityManager entityManager = getEntityManager();

    public Group selectById(int id) {
        return entityManager.find(Group.class, id);
    }
    public Group select(int number)  {
        Group group = (Group) entityManager
                .createQuery("FROM Group WHERE number = :number")
                .setParameter("number",number)
                .getResultList().get(0);
        return group;
    }
    public List<Group> selectAll() {
        List<Group> groups = entityManager.createQuery("FROM Group").getResultList();
        return groups;
    }
}