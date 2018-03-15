package srv;

import common.App;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
//import srv.data.auto.User;
//import srv.data.User;
import srv.data.Name;
import srv.data.Project;
import srv.data.auto.Auto;



public class Main {

    private static Session session = null;

    public static void main(String[] args) {
        App.set().environment(App.Environment.DEV).init();
        if(App.log() == null) System.out.println("log is nul");
    }

    public static void main2(String[] args) {


        session = createHibernateSession();
//        if (session != null) {
//            System.out.println("session is created");
//            // Добавление записей в таблицу
//            // Закрытие сессии
//            if (session.isOpen())
//                session.close();


//        if (session == null)
//            return;

        srv.data.auto.User user1 = new srv.data.auto.User();
        user1.setName ("Иван");
        user1.setLogin("ivan");

        srv.data.auto.User  user2 = new srv.data.auto.User ();
        user2.setName ("Сергей");
        user2.setLogin("serg"  );

        Transaction trans = session.beginTransaction();
        session.save(user1);
        session.save(user2);
/*        entityManager.getTransaction().begin();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.getTransaction().commit();*/

        Auto auto = new Auto();
        auto.setName("Volvo");
        auto.setUser(user1);
/*        entityManager.getTransaction().begin();
        entityManager.persist(auto);
        entityManager.getTransaction().commit();*/
        session.saveOrUpdate(auto);

        auto = new Auto();
        auto.setName("Skoda");
        auto.setUser(user1);
        session.saveOrUpdate(auto);
//
        session.flush();
        trans.commit();

//        } else {
//            System.out.println("session is not created");
//        }

//        new Server().start();
    }

    private static Session createHibernateSession()
    {
        SessionFactory sf  = null;
        ServiceRegistry srvc = null;
        try {
            Configuration conf = new Configuration()
                    .addAnnotatedClass(srv.data.auto.Auto.class)
                    .addAnnotatedClass(srv.data.auto.User.class);

//            conf.configure();
            conf.configure("META-INF/hibernate.cfg.xml");

//            srvc = new StandardServiceRegistryBuilder().applySettings(
//                    conf.getProperties()).build();
            sf = conf.buildSessionFactory();//srvc);
            session = sf.openSession();
            System.out.println("Создание сессии");
        } catch (Throwable e) {
            System.err.println("Failed to create sessionFactory object : " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
        return session;
    }
}
