package dbmodules.dao;

import dbmodules.interfaces.GroupTable;
import dbmodules.tables.Group;
import hibernate.HibernateSessionFactory;
import hibernate.JPAUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class GroupDAO extends JPAUtil implements GroupTable {
    public void add(Group group) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(group);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    public Group selectById(int id) {
        EntityManager entityManager = getEntityManager();
        Group group = entityManager.find(Group.class, id);
        entityManager.close();
        return group;
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
    public void delete(Group group) {
        EntityManager entityManager = getEntityManager();
        group = entityManager.find(Group.class, group.getId());
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        //System.out.println(group + " " + entityManager.contains(group));
        entityManager.remove(group);
        entityManager.merge(group);
        transaction.commit();
        entityManager.close();
    }
}