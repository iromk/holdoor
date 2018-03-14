package srv.data;

import org.junit.*;
import srv.JPAFactory;

import javax.persistence.EntityManager;

public class UserTest {


    @BeforeClass
    public static void setUpClass() {
        new FixtureSetup_Users(true);

    }

    @After
    public void close() {
        JPAFactory.closeEntityManager();
    }

    @Test
    public void AddUserTest() {
        final EntityManager entityManager = JPAFactory.getEntityManager();

        final String userFirstName = "Sarah";
        final String userLastName = "Connor";

        User user = new User();
        user.setName(new Name(userFirstName, userLastName));

        User user2 = new User();
        user2.setName(new Name(userLastName, userFirstName));

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        Long user1Id = user.getId();
        entityManager.persist(user2);
        Long user2Id = user2.getId();
        entityManager.getTransaction().commit();

        entityManager.detach(user);
        entityManager.detach(user2);

        User foundUser = entityManager.find(User.class, user1Id);
        Assert.assertTrue(foundUser.equals(user));

        foundUser = entityManager.find(User.class, user2Id);
        Assert.assertTrue(foundUser.equals(user2));

    }

    @Test//(expected = NullPointerException.class)
    public void WrongUidGiven() {
        User hey = User.findByUid("wrongestuidever");
        Assert.assertNull(hey);
    }


}