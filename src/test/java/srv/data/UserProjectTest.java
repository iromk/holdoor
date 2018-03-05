package srv.data;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import srv.JPAKeeper;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;

public class UserProjectTest {

    private EntityManager entityManager;

    @Before
    public void prepareManagerFactory() {
        entityManager = JPAKeeper.getEntityManager();

        clearTables("User", "Project");

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
        JPAKeeper.closeEntityManager();
    }

    @Test
    public void AddUserAnd2ProjectsTest() {

        final String userFirstName = "Conan";
        final String userLastName = "Barber";
        final String userUid = "headcut";

        final String project1Name = "Project #1";
        final String project2Name = "Another project";

        Project p1 = new Project();
        p1.setName(project1Name);

        Project p2 = new Project();
        p2.setName(project2Name);

        User conan = new User(userFirstName, userLastName, userUid);

        p1.setHolderUid(conan);
        p2.setHolderUid(conan);

        entityManager.getTransaction().begin();
        entityManager.persist(conan);
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.getTransaction().commit();

        entityManager.detach(p1);
        entityManager.detach(p2);
        entityManager.detach(conan);

        User check = User.findByUid(userUid);

        Assert.assertEquals(check.getName().getFirst(), userFirstName);
        Assert.assertEquals(check.getProjects().size(), 2);
        Assert.assertEquals(check.getProjects().toArray()[0], p1);
        Assert.assertEquals(check.getProjects().toArray()[1], p2);
    }

    @Test
    public void FindUserAndAdd3Projects() {
        User hey = User.findByUid("logmi");
        Assert.assertNull (hey);
    }

}
