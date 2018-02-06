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

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        entityManager.detach(user);

//        user = null;


        User foundUser = entityManager.find(User.class, 1);

        Assert.assertTrue(foundUser.equals(user));

//        user = new User();
//        user.setId(1);
//        user.setName(new Name(userFirstName, "wrongLastName"));
//        Assert.assertFalse(entityManager.contains(user));
//
//        user.setName(new Name(userFirstName, userLastName));
//        Assert.assertTrue(entityManager.contains(user));
    }

}