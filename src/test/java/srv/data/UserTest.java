package srv.data;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class UserTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Before
    public void prepareManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test.holdoorsrv.jpa");
        entityManager = entityManagerFactory.createEntityManager();

        clearTables();
    }

    private void clearTables() {
        String hql="delete from User";
        Query query= entityManager.createQuery(hql);
        entityManager.getTransaction().begin();
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @After
    public void closeEverything() {
        entityManagerFactory.close();
    }

    @Test
    public void AddUserTest() {

        final String userFirstName = "Sarah";
        final String userLastName = "Connor";

        User user = new User();
        user.setName(new Name(userFirstName, userLastName));

        User user2 = new User();
        user2.setName(new Name(userLastName, userFirstName));

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.getTransaction().commit();

        entityManager.detach(user);
        entityManager.detach(user2);

        User foundUser = entityManager.find(User.class, 1);
        Assert.assertTrue(foundUser.equals(user));

        foundUser = entityManager.find(User.class, 2);
        Assert.assertTrue(foundUser.equals(user2));

    }

}