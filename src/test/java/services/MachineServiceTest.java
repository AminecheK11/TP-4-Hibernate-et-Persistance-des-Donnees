package services;
import entities.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MachineServiceTest extends PersistenceTestSupport {
    MachineService service=new MachineService();
    Salle salle() { Salle s=new Salle("S1","Informatique"); assertTrue(new SalleService().create(s)); return s; }
    @Test void testCreateMachine() {
        Salle s=salle(); Machine m=new Machine("M-101","Dell",s);
        assertTrue(service.create(m)); assertTrue(m.getId()>0);
        assertEquals(s.getId(),service.getById(m.getId()).getSalle().getId());
    }
    @Test void testUpdateMachine() {
        Machine m=new Machine("M-101","Dell",salle()); service.create(m); m.setMarque("HP");
        assertTrue(service.update(m)); assertEquals("HP",service.getById(m.getId()).getMarque());
    }
    @Test void testDeleteMachineSansSupprimerSalle() {
        Salle s=salle(); Machine m=new Machine("M-101","Dell",s); service.create(m);
        assertTrue(service.delete(m)); assertNull(service.getById(m.getId())); assertNotNull(new SalleService().getById(s.getId()));
    }
    @Test void testGetAll() {
        Salle s=salle(); service.create(new Machine("M1","Dell",s)); service.create(new Machine("M2","HP",s));
        assertEquals(2,service.getAll().size());
    }
    @Test void testRechercheMarque() {
        Salle s=salle(); service.create(new Machine("M1","Dell",s)); service.create(new Machine("M2","HP",s)); service.create(new Machine("M3","Dell",s));
        assertEquals(2,service.findByMarque("Dell").size());
        assertTrue(service.findByMarque("Lenovo").isEmpty());
    }
    @Test void testParametreHql() {
        service.create(new Machine("M1","Dell",salle()));
        assertTrue(service.findByMarque("' or 1=1 --").isEmpty());
    }
    @Test void testChangementSalle() {
        Salle a=salle(),b=new Salle("S2","Reseau"); new SalleService().create(b);
        Machine m=new Machine("M1","Dell",a); service.create(m); m.setSalle(b);
        assertTrue(service.update(m)); assertEquals(b.getId(),service.getById(m.getId()).getSalle().getId());
        assertTrue(new SalleService().getAvecMachines(a.getId()).getMachines().isEmpty());
    }
    @Test void testRollbackUpdateInvalide() {
        Machine m=new Machine("M1","Dell",salle()); service.create(m); m.setRef("X".repeat(300));
        assertFalse(service.update(m)); assertEquals("M1",service.getById(m.getId()).getRef());
    }
    @Test void testIdAbsent() { assertNull(service.getById(-1)); }
}
