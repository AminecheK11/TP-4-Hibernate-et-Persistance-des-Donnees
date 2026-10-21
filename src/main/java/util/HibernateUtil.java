package util;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.nio.file.*;
import java.io.*;
import java.util.Properties;
public final class HibernateUtil {
    private static final SessionFactory sessionFactory=buildSessionFactory();
    private HibernateUtil() {}
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration config=new Configuration().configure();
            Path local=Path.of("hibernate-local.properties");
            if(Files.exists(local)) {
                Properties p=new Properties();
                try(Reader r=Files.newBufferedReader(local)) { p.load(r); }
                config.addProperties(p);
            }
            String password=System.getenv("TP_MYSQL_PASSWORD");
            if(password!=null) config.setProperty("hibernate.connection.password",password);
            String url=System.getProperty("tp.hibernate.url");
            String ddl=System.getProperty("tp.hibernate.ddl");
            if(url!=null) config.setProperty("hibernate.connection.url",url);
            if(ddl!=null) {
                if(!config.getProperty("hibernate.connection.url").contains("/db_tp_hibernate_test?"))
                    throw new IllegalArgumentException("Override DDL reserve a la base de test.");
                config.setProperty("hibernate.hbm2ddl.auto",ddl);
            }
            return config.buildSessionFactory();
        } catch(Exception ex) { throw new ExceptionInInitializerError(ex); }
    }
    public static SessionFactory getSessionFactory() { return sessionFactory; }
    public static void shutdown() { if(!sessionFactory.isClosed()) sessionFactory.close(); }
}
