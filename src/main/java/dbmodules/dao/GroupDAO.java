package dbmodules.dao;

import dbmodules.interfaces.GroupTable;
import dbmodules.tables.Group;
import utilfactories.JPAUtil;
import java.util.List;

public class GroupDAO extends JPAUtil<Group> implements GroupTable {

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
}