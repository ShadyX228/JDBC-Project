package utilfactories;

import dbmodules.entity.BaseEntity;
import dbmodules.service.BaseService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil implements BaseService {
    private static final String unitName = "JDBC";
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory(unitName);
    protected EntityManager entityManager = getEntityManager();

    private EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    public void closeEntityManager() {
        entityManager.close();
    }
}