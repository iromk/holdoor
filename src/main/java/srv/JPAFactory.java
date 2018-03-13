package srv;

import common.App;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAFactory {

    private static final EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager = null;

    static {
        String jpaName = null;
        switch (App.get().environment()) {
            case DEV: jpaName = "dev.holdoorsrv.jpa"; break;
            case PROD: jpaName = "prod.holdoorsrv.jpa"; break;
            case TEST: jpaName = "test.holdoorsrv.jpa"; break;
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(jpaName);
        App.log().info("JPA set with '" + jpaName + "'");
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null)
            entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    public static void closeEntityManager() {
        entityManager.close();
        entityManager = null;
        App.log().info("Entity manager closed.");
    }

    public static void close() {
        closeEntityManager();
        entityManagerFactory.close();
    }


}
