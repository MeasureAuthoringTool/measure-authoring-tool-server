package mat.configuration;

import mat.hibernate.HibernateStatisticsFilter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableTransactionManagement
@Transactional
public class HibernateConfiguration {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        return buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = (SessionFactory) HibernateStatisticsFilter.getContext().getBean("sessionFactory");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static Session getHibernateSession() {
        return HibernateConfiguration.getSessionFactory().getCurrentSession();
    }

    public static Session createHibernateSession() {
        SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();
        Session session = sessionFactory.openSession();

        return session;
    }
}
