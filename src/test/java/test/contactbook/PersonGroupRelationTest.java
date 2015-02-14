package test.contactbook;

import test.contactbook.controllers.TestModule;
import test.contactbook.controllers.AbstractAllControllers;
import static org.junit.Assert.*;
import controllers.GroupController;
import controllers.PersonController;
import model.Group;
import model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PersonGroupRelationTest {

    private GroupController groupCtrl;
    private PersonController personCtrl;
    private AbstractAllControllers allCtrls;

    public PersonGroupRelationTest() {
    }

    @Before
    public void setUp() {
        allCtrls = TestModule.testControllers();
        groupCtrl = allCtrls.getGroupCtrl();
        personCtrl = allCtrls.getPersonCtrl();
        groupCtrl.removeAllGroups();
        personCtrl.removeAllPersons();
    }

    @After
    public void tearDown() {
        allCtrls.close();
    }
    
    @Test
    public void removeGroupTest() {
        Person p1 = new Person.Builder().name("Test 0").build();        
        Group g1 = new Group.Builder().name("Group 0").build();   
        
        personCtrl.addPersonToGroup(p1, g1);        
        /*
        assertEquals(1, personCtrl.personsInGroup(g1).size());
        assertEquals(1, personCtrl.groupsForPerson(p1).size());*/
        groupCtrl.removeGroup(g1);
        assertEquals(0, groupCtrl.getAllGroups().size());
    }
       
    @Test
    public void addRemovePersonToGroup() {
        Person p0 = new Person.Builder().name("Test 0").build();
        Person p1 = new Person.Builder().name("Test 1").build();
        Group g0 = new Group.Builder().name("Group 0").build();
        Group g1 = new Group.Builder().name("Group 1").build();
        personCtrl.addPersonToGroup(p0, g0);
        personCtrl.createPerson(p1);
        assertEquals(1, personCtrl.personsInGroup(g0).size());
        assertEquals(1, personCtrl.groupsForPerson(p0).size());
        assertEquals(1, personCtrl.personsInGroup(null).size());
        assertTrue(personCtrl.groupsForPerson(p1).isEmpty());
        // remove person from group
        personCtrl.removePersonFromGroup(p0, g0);
        assertTrue(personCtrl.personsInGroup(g0).isEmpty());
        assertTrue(personCtrl.groupsForPerson(p0).isEmpty());
        // add person to groups
        personCtrl.addPersonToGroup(p1, g1);
        personCtrl.addPersonToGroup(p1, g0);        
        assertEquals(1, personCtrl.personsInGroup(g1).size());
        assertEquals(1, personCtrl.personsInGroup(g0).size());
        assertEquals(2, personCtrl.groupsForPerson(p1).size());
        groupCtrl.removeGroup(g1);
        assertEquals(1, groupCtrl.getAllGroups().size());        
        assertEquals(0, personCtrl.personsInGroup(g1).size());
        assertEquals(1, personCtrl.groupsForPerson(p1).size());

        //check db size
        assertEquals(2, personCtrl.getAllPersons().size());
        assertEquals(1, groupCtrl.getAllGroups().size());
    }

    @Test
    public void hierarchyWithPersonTest() {
        Group root0 = new Group.Builder().name("Root 0").build();
        Group root1 = new Group.Builder().name("Root 1").build();
        Group root2 = new Group.Builder().name("Root 2").build();
        Group root00 = new Group.Builder().name("Root 00").build();
        Group root01 = new Group.Builder().name("Root 01").build();
        Group root010 = new Group.Builder().name("Root 010").build();

        groupCtrl.addSubGroup(root00, root0);
        groupCtrl.addSubGroup(root01, root0);
        groupCtrl.addSubGroup(root010, root01);
        groupCtrl.createGroup(root1);
        groupCtrl.createGroup(root2);

        Person p0 = new Person.Builder().name("Test 0").build();
        Person p1 = new Person.Builder().name("Test 1").build();
        Person p2 = new Person.Builder().name("Test 2").build();
        Person p3 = new Person.Builder().name("Test 3").build();

        personCtrl.addPersonToGroup(p0, root0);
        personCtrl.addPersonToGroup(p1, root00);
        personCtrl.addPersonToGroup(p2, root01);
        personCtrl.addPersonToGroup(p3, root010);
        personCtrl.addPersonToGroup(p0, root010);

        // check creation        
        assertEquals(2, personCtrl.groupsForPerson(p0).size());

        // check counter        
        assertEquals(0, groupCtrl.countPersonsInGroup(null));
        assertEquals(1, personCtrl.personsInGroup(root0).size());
        assertEquals(2, personCtrl.personsInGroup(root010).size());
        assertEquals(4, groupCtrl.countPersonsInGroupRecursive(null));        
        assertEquals(4, groupCtrl.countPersonsInGroupRecursive(root0));

        // remove
        personCtrl.removePerson(p0);
        assertEquals(0, personCtrl.personsInGroup(root0).size());
        assertEquals(1, personCtrl.personsInGroup(root010).size());
    }
}