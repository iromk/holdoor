package srv.data;

import srv.JPAFactory;

import javax.persistence.EntityManager;

public class FixtureSetup_Projects extends FixtureSetup {


    public FixtureSetup_Projects(Boolean clearTable) {
        if(clearTable) clearTables("Project");
        loadFixtures();
    }

    public  static void  loadFixtures() {
        EntityManager entityManager = JPAFactory.getEntityManager();

        entityManager.getTransaction().begin();

        entityManager.getTransaction().commit();
    }

}
