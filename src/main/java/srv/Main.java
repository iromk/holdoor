package srv;

import srv.db.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("vasd");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("holdoorsrv.jpa");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        entityManagerFactory.close();

        new Server().start();
    }
}
