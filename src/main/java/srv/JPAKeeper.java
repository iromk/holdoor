package srv;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAKeeper {

    private static final EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager = null;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("test.holdoorsrv.jpa");
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null)
            entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    public static void close() {
        entityManager.close();
        entityManager = null;
        entityManagerFactory.close();
    }


}
