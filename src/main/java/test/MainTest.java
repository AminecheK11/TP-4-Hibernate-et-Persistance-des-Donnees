package test;
import entities.*;
import services.*;
import util.HibernateUtil;
public class MainTest {
    public static void main(String[] args) {
        try {
            SalleService salles=new SalleService(); MachineService machines=new MachineService();
            Salle salle=salles.getAll().stream().filter(s -> "S1".equals(s.getCode())).findFirst().orElse(null);
            if(salle==null) {
                salle=new Salle("S1","Salle Informatique");
                if(!salles.create(salle)) throw new IllegalStateException("Creation salle echouee.");
            }
            final int id=salle.getId();
            if(machines.getAll().stream().noneMatch(m -> "M-101".equals(m.getRef()) && m.getSalle()!=null && m.getSalle().getId()==id)) {
                if(!machines.create(new Machine("M-101","Dell",salle))) throw new IllegalStateException("Creation machine echouee.");
            }
            System.out.println("Donnees inserees avec succes (ou deja presentes).");
            for(Salle s:salles.getAll()) System.out.printf("Salle %d : %s - %s%n",s.getId(),s.getCode(),s.getLibelle());
            for(Machine m:machines.findByMarque("Dell")) System.out.printf("Machine Dell %d : %s, salle=%s%n",m.getId(),m.getRef(),m.getSalle().getCode());
            System.out.println("Machines de S1 : "+salles.getAvecMachines(id).getMachines().size());
        } finally { HibernateUtil.shutdown(); }
    }
}
