package utilfactories;

import dbmodules.tables.Table;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil <T extends Table> {
    private static final String unitName = "JDBC";
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory(unitName);
    protected EntityManager entityManager = getEntityManager();

    private EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }
    public static void close(){
        entityManagerFactory.close();
    }

    public void add(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }
    public void delete(T entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }
}