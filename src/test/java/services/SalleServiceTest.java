package services;
import entities.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SalleServiceTest extends PersistenceTestSupport {
    SalleService service=new SalleService();
    @Test void testCreateSalle() {
        Salle s=new Salle("S2","Salle Reseau"); assertTrue(service.create(s)); assertTrue(s.getId()>0);
        assertEquals("Salle Reseau",service.getById(s.getId()).getLibelle());
    }
    @Test void testUpdateSalle() {
        Salle s=new Salle("S2","Avant"); service.create(s); s.setLibelle("Apres");
        assertTrue(service.update(s)); assertEquals("Apres",service.getById(s.getId()).getLibelle());
    }
    @Test void testDeleteSalle() {
        Salle s=new Salle("S2","Salle"); service.create(s); assertTrue(service.delete(s)); assertNull(service.getById(s.getId()));
    }
    @Test void testGetAll() {
        service.create(new Salle("S1","A")); service.create(new Salle("S2","B")); assertEquals(2,service.getAll().size());
    }
    @Test void testIdAbsent() { assertNull(service.getById(-1)); }
    @Test void testCascadeCreationEtLectureCollection() {
        Salle s=new Salle("S3","Cascade"); s.ajouterMachine(new Machine("M1","Dell",null));
        assertTrue(service.create(s));
        Salle lue=service.getAvecMachines(s.getId());
        assertEquals(1,lue.getMachines().size()); assertEquals(s.getId(),lue.getMachines().get(0).getSalle().getId());
    }
    @Test void testCascadeSuppression() {
        Salle s=new Salle("S3","Cascade"); s.ajouterMachine(new Machine("M1","Dell",null));
        assertTrue(service.create(s)); int machine=s.getMachines().get(0).getId();
        // Relire detache avec collection LAZY : delete doit charger les enfants dans sa session.
        assertTrue(service.delete(service.getById(s.getId())));
        assertNull(new MachineService().getById(machine)); assertNull(service.getById(s.getId()));
    }
    @Test void testRollbackCascadeApresInsertionPartielle() {
        Salle s=new Salle("S4","Rollback");
        s.ajouterMachine(new Machine("OK","Dell",null));
        s.ajouterMachine(new Machine("X".repeat(300),"Dell",null));
        assertFalse(service.create(s));
        assertTrue(service.getAll().isEmpty()); assertTrue(new MachineService().getAll().isEmpty());
        assertTrue(service.create(new Salle("S5","Apres erreur")));
    }
}
