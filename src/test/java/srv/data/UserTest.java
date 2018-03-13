package srv.data;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import srv.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserTest {

    private EntityManager entityManager;

    @Before
    public void prepareManagerFactory() {
        entityManager = JPAFactory.getEntityManager();

        clearTables("User");

        loadKnownData();

    }

    private void clearTables(String... tableNames) {
        if(tableNames.length == 0) tableNames[0] = "User";
        entityManager.getTransaction().begin();
        for (String table: tableNames) {
            String hql="delete from " + table;
            Query query= entityManager.createQuery(hql);
            query.executeUpdate();
        }
        entityManager.getTransaction().commit();
    }

    private void loadKnownData() {

        entityManager.getTransaction().begin();

        User nextUser;
        nextUser = new User("Jane", "Doe", "jd1989");
        entityManager.persist(nextUser);
        nextUser = new User("Mac", "Os", "applejuice");
        entityManager.persist(nextUser);
        nextUser = new User("Keep", "Simple", "stupid");
        entityManager.persist(nextUser);
        nextUser.getName().setFirst("Keep It");
        entityManager.persist(nextUser);
        nextUser = new User("Sarah", "Connor", "weallbedead");
        entityManager.persist(nextUser);
        nextUser = new User("Yankee", "Go", "dunno");
        entityManager.persist(nextUser);
        nextUser = new User("World", "Peace", "nya");
        entityManager.persist(nextUser);

        entityManager.getTransaction().commit();
    }


    @After
    public void closeEverything() {
        JPAFactory.closeEntityManager();
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