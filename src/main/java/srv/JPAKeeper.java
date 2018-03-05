package srv;

import common.Environment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAKeeper {

    private static final EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager = null;

    static {
        String jpaName = null;
        switch (Environment.id) {
            case DEV: jpaName = "dev.holdoorsrv.jpa"; break;
            case PROD: jpaName = "prod.holdoorsrv.jpa"; break;
            case TEST: jpaName = "test.holdoorsrv.jpa"; break;
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(jpaName);
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null)
            entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    public static void closeEntityManager() {
        entityManager.close();
        entityManager = null;
    }

    public static void close() {
        closeEntityManager();
        entityManagerFactory.close();
    }


}
