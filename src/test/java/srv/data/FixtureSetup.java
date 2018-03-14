package srv.data;

import srv.JPAFactory;
import srv.TestsSetup;

import javax.persistence.EntityManager;
import javax.persistence.Query;

abstract class FixtureSetup  extends TestsSetup {

    protected void clearTables(String... tableNames) {
        EntityManager entityManager = JPAFactory.getEntityManager();
        if(tableNames.length == 0) tableNames[0] = "User";
        entityManager.getTransaction().begin();
        for (String table: tableNames) {
            String hql="delete from " + table;
            Query query= entityManager.createQuery(hql);
            query.executeUpdate();
        }
        entityManager.getTransaction().commit();
    }
}
