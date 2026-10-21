package services;
import org.junit.jupiter.api.extension.*;
import util.HibernateUtil;
/** Meme isolation en lancement JUnit IntelliJ et par Maven. */
public class TestDatabaseExtension implements BeforeAllCallback {
    public void beforeAll(ExtensionContext context) {
        String url=System.getProperty("tp.hibernate.url");
        if(url==null) System.setProperty("tp.hibernate.url","jdbc:mysql://localhost:3306/db_tp_hibernate_test?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        else if(!url.contains("/db_tp_hibernate_test?")) throw new IllegalStateException("Base JUnit incorrecte.");
        System.setProperty("tp.hibernate.ddl","create-drop");
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).getOrComputeIfAbsent(
            "hibernate-test-factory",key -> (ExtensionContext.Store.CloseableResource)HibernateUtil::shutdown);
    }
}
