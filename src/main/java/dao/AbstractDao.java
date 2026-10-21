package dao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/** Rollback avant fermeture de la session, puis retour false suivant le contrat CRUD. */
abstract class AbstractDao<T> implements IDao<T> {
    private final Class<T> type;
    AbstractDao(Class<T> type) { this.type=type; }
    private boolean ecrire(Consumer<Session> action) {
        try(Session session=HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx=null;
            try {
                tx=session.beginTransaction(); action.accept(session); tx.commit(); return true;
            } catch(RuntimeException e) {
                if(tx!=null && tx.isActive()) {
                    try { tx.rollback(); } catch(RuntimeException rollback) { e.addSuppressed(rollback); }
                }
                Logger.getLogger(getClass().getName()).warning("Operation annulee : "+e.getMessage());
                return false;
            }
        }
    }
    public boolean create(T o) { return ecrire(s -> s.save(o)); }
    public boolean update(T o) { return ecrire(s -> s.update(o)); }
    public boolean delete(T o) { return ecrire(s -> s.delete(o)); }
    public T getById(int id) {
        try(Session s=HibernateUtil.getSessionFactory().openSession()) { return s.get(type,id); }
    }
    public List<T> getAll() {
        try(Session s=HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from "+type.getSimpleName()+" order by id",type).list();
        }
    }
}
