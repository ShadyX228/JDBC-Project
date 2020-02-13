package utilfactories;

import dbmodules.tables.Table;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class JPAUtil{
    private static final String unitName = "JDBC";
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory(unitName);
    protected EntityManager entityManager = getEntityManager();

    protected EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    public static void close(){
        entityManagerFactory.close();
    }
    public void closeEntityManager() {
        entityManager.close();
    }
}