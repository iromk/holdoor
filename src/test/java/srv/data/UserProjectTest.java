package srv.data;

import org.junit.*;
import srv.JPAFactory;

import javax.persistence.*;

public class UserProjectTest {

    @BeforeClass
    public static void setUpClass() {

        new FixtureSetup_Users(true);
        new FixtureSetup_Projects(true);
    }

    @After
    public void close() {
        JPAFactory.closeEntityManager();
    }

    @Test
    public void AddUserAnd2ProjectsTest() {

        final EntityManager entityManager = JPAFactory.getEntityManager();

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
        User hey = User.findByUid("dunno");
        Assert.assertNotNull (hey);
    }

}
