package services;
import org.junit.jupiter.api.BeforeEach;
import org.hibernate.Session;
import util.HibernateUtil;
@org.junit.jupiter.api.extension.ExtendWith(TestDatabaseExtension.class)
abstract class PersistenceTestSupport {
    @BeforeEach void nettoyer() {
        try(Session s=HibernateUtil.getSessionFactory().openSession()) {
            String url=(String)HibernateUtil.getSessionFactory().getProperties().get("hibernate.connection.url");
            if(!url.contains("/db_tp_hibernate_test?")) throw new IllegalStateException("Base de test obligatoire.");
            var tx=s.beginTransaction();
            s.createQuery("delete from Machine").executeUpdate(); s.createQuery("delete from Salle").executeUpdate(); tx.commit();
        }
    }
}
