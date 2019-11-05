package hibernate;

import dbmodules.tables.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;

public class HibernateSessionFactory {
    private static SessionFactory sessionFactory;
    private HibernateSessionFactory() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration()
                        .configure();

                configuration.addAnnotatedClass(Group.class);
                configuration.addAnnotatedClass(Student.class);
                configuration.addAnnotatedClass(Teacher.class);

                StandardServiceRegistryBuilder builder = new
                        StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.
                        buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Some error occured.");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}