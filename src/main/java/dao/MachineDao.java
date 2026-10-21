package dao;
import entities.Machine;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;
import java.util.List;
public class MachineDao extends AbstractDao<Machine> {
    public MachineDao() { super(Machine.class); }
    public List<Machine> findByMarque(String marque) {
        try(Session session=HibernateUtil.getSessionFactory().openSession()) {
            Query<Machine> query=session.createQuery("from Machine m where m.marque = :marque",Machine.class);
            query.setParameter("marque",marque);
            return query.list();
        }
    }
}
