package test.contactbook;

import test.contactbook.controllers.TestModule;
import test.contactbook.controllers.AbstractAllControllers;
import controllers.PersonController;
import java.util.Collection;
import model.Person;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CRUDPersonTest {

    private PersonController personCtrl;    
    private AbstractAllControllers allCtrls;

    @Before
    public void setUp() {
        allCtrls = TestModule.testControllers();
        personCtrl = allCtrls.getPersonCtrl();       
        personCtrl.removeAllPersons();        
    }

    @After
    public void tearDown() {
        allCtrls.close();
    }
    
    @Test
    public void CRUDPerson() {
        // create part
        Person p = new Person();
        p.setName("Test Created");
        assertNull(p.getId());
        personCtrl.createPerson(p);
        Collection<Person> persons = personCtrl.getAllPersons();
        assertEquals(1, persons.size());
        p = persons.iterator().next();
        assertEquals("Test Created", p.getName());
        assertNotNull(p.getId());
        // update part
        p.setName("Test Updated");
        personCtrl.updatePerson(p);
        persons = personCtrl.getAllPersons();
        assertEquals(1, persons.size());
        p = persons.iterator().next();
        assertEquals("Test Updated", p.getName());
        assertNotNull(p.getId());        
        // delete part        
        personCtrl.removePerson(p);
        persons = personCtrl.getAllPersons();
        assertTrue(persons.isEmpty());
        // create many entities
        for(int i=0; i<30; i++) {
            p = new Person();
            p.setName("Test " + i);
            personCtrl.createPerson(p);
        }
        persons = personCtrl.getAllPersons();
        assertEquals(30, persons.size());
        // delete all entities
        personCtrl.removeAllPersons();
        persons = personCtrl.getAllPersons();
        assertTrue(persons.isEmpty());
    }        
}
