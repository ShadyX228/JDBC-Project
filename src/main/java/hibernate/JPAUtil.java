package hibernate;

import dbmodules.tables.Group;
import dbmodules.tables.Table;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil <T extends Table> {
    private static final String unitName = "JDBC";
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(unitName);

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }
    public static void close(){
        entityManagerFactory.close();
    }

    public void add(T entity) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    public void delete(T entity) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}