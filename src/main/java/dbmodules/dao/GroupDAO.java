package dbmodules.dao;

import dbmodules.interfaces.GroupTable;
import dbmodules.tables.Group;
import hibernate.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class GroupDAO implements GroupTable {
    public void add(Group group) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(group);
        transaction.commit();
        session.close();
    }
    public Group selectById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(Group.class, id);
    }
    public Group select(int number)  {
        Query query = HibernateSessionFactory
                .getSessionFactory()
                .openSession()
                .createQuery("FROM Group WHERE Number = :number");
        query.setParameter("number", number);

        List<Group> list = query.list();
        return list.get(0);
    }
    public List<Group> selectAll() {
        Query query = HibernateSessionFactory
                .getSessionFactory()
                .openSession()
                .createQuery("FROM Group");

        return query.list();
    }
    public void delete(Group group) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(group);
        transaction.commit();
        session.close();
    }
}