package srv.data;

import srv.JPAFactory;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collection;

public class FixtureSetup_Users extends FixtureSetup {

    public static Collection<Object[]> usersFixtureData() {
        return Arrays.asList(new Object[][] {
                {"Jane", "Doe", "jd1989"},
                {"Mac", "Os", "applejuice"},
                {"Keep", "Simple", "stupid"},
                {"Sarah", "Connor", "weallbedead"},
                {"Yankee", "Go", "dunno"},
                {"World", "Peace", "nya"}
        });
    }


    public FixtureSetup_Users(Boolean clearTable) {
        if(clearTable) clearTables("User");
        loadFixtures();
    }

    public  static void  loadFixtures() {
        EntityManager entityManager = JPAFactory.getEntityManager();

        entityManager.getTransaction().begin();

        User user;
        for (Object[] userObject: usersFixtureData()) {
            user = new User((String)userObject[0], (String)userObject[1], (String)userObject[2]);
            entityManager.persist(user);
        }

        entityManager.getTransaction().commit();
    }

}
