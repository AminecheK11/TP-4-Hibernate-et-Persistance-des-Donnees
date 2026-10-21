package dao;
import entities.Salle;
import org.hibernate.Session;
import util.HibernateUtil;
public class SalleDao extends AbstractDao<Salle> {
    public SalleDao() { super(Salle.class); }
    /** Chargement explicite pour consulter la collection apres fermeture de session. */
    public Salle getAvecMachines(int id) {
        try(Session s=HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select distinct s from Salle s left join fetch s.machines where s.id=:id",Salle.class)
                .setParameter("id",id).uniqueResult();
        }
    }
}
