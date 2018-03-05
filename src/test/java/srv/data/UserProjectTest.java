package srv.data;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public class UserProjectTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Before
    public void prepareManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test.holdoorsrv.jpa");
        entityManager = entityManagerFactory.createEntityManager();

        clearTables("User", "Project");
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

    @After
    public void closeEverything() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void AddUserAndProjectsTest() {

        final String userFirstName = "Sarah";
        final String userLastName = "Connor";
        final String user1Uid = userFirstName + userLastName + Math.random();
        final String user2Uid = userFirstName + userLastName + Math.random();

        final String project1Name = "Project #1";
        final String project2Name = "Another project";

        User user = new User();
        user.setName(new Name(userFirstName, userLastName));
        user.setUid(user1Uid);

        User user2 = new User();
        user2.setName(new Name(userLastName, userFirstName));
        user2.setUid(user2Uid);

        Project p1 = new Project();
        p1.setName(project1Name);

        Project p2 = new Project();
        p2.setName(project2Name);

        p1.setHolderUid(user);
        p2.setHolderUid(user);

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.getTransaction().commit();

        entityManager.detach(user);
        entityManager.detach(user2);
        entityManager.detach(p1);
        entityManager.detach(p2);

//        User foundUser = entityManager.find(User.class, user1Uid);
//        foundUser =

        Project p3 = new Project();
        p3.setName("asd");
        CriteriaBuilder crib = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cri =  crib.createQuery(User.class);
        Root<User> from = cri.from(User.class);
        cri.select(from);
        cri.where(crib.equal(from.get("uid"), user1Uid));
        TypedQuery<User> tq = entityManager.createQuery(cri);
        User userResult = tq.getSingleResult();
        Assert.assertEquals(userResult.getName().getFirst(), userFirstName);
        Assert.assertEquals(userResult.getProjects().size(), 2);
        Assert.assertEquals(userResult.getProjects().toArray()[0], p1);
        Assert.assertEquals(userResult.getProjects().toArray()[1], p2);

//        Assert.assertTrue(foundUser.equals(user));
//
//        foundUser = entityManager.find(User.class, user2Uid);
//        Assert.assertTrue(foundUser.equals(user2));

    }

}
