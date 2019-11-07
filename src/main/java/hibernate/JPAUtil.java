package hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final String unitName = "JDBC";
    private static final EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(unitName);

    public static EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }
    public static void close(){
        emFactory.close();
    }
}