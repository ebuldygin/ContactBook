package test.contactbook;

import test.contactbook.controllers.TestModule;
import test.contactbook.controllers.AbstractAllControllers;
import controllers.GroupController;
import java.util.Collection;
import model.Group;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CRUDGroupTest {

    private GroupController groupCtrl;
    private AbstractAllControllers allCtrls;

    @Before
    public void setUp() {
        allCtrls = TestModule.testControllers();
        groupCtrl = allCtrls.getGroupCtrl();
        groupCtrl.removeAllGroups();
    }

    @After
    public void tearDown() {
        allCtrls.close();
    }

    @Test
    public void CRUDGroup() {
        // create part
        Group group = new Group();
        assertNull(group.getId());
        group.setName("Test Created");
        groupCtrl.addSubGroup(group, null);
        Collection<Group> groups = groupCtrl.getAllGroups();
        assertEquals(1, groups.size());
        group = groups.iterator().next();
        assertEquals("Test Created", group.getName());
        assertNotNull(group.getId());
        // update part
        group.setName("Test Updated");
        groupCtrl.updateGroup(group);
        groups = groupCtrl.getAllGroups();
        assertEquals(1, groups.size());
        group = groups.iterator().next();
        assertEquals("Test Updated", group.getName());
        assertNotNull(group.getId());
        // delete part        
        groupCtrl.removeGroup(group);
        groups = groupCtrl.getAllGroups();
        assertTrue(groups.isEmpty());
        // create many entities
        for (int i = 0; i < 30; i++) {
            group = new Group();
            group.setName("Test " + i);
            groupCtrl.addSubGroup(group, null);
        }
        // delete all entities
        groups = groupCtrl.getAllGroups();
        assertEquals(30, groups.size());
        groupCtrl.removeAllGroups();
        groups = groupCtrl.getAllGroups();
        assertTrue(groups.isEmpty());
    }

    @Test
    public void simpleHierarchyTest() {
        Group root0 = new Group.Builder().name("Root 0").build();
        Group root1 = new Group.Builder().name("Root 1").build();

        groupCtrl.createGroup(root0);
        groupCtrl.createGroup(root1);

        groupCtrl.addSubGroup(root1, root0);
        assertEquals(1, groupCtrl.getSubGroupsRecursive(root0).size());
    }

    @Test
    public void hierarchyTest() {
        Group root0 = new Group.Builder().name("Root 0").build();
        Group root1 = new Group.Builder().name("Root 1").build();
        Group root2 = new Group.Builder().name("Root 2").build();
        Group root00 = new Group.Builder().name("Root 00").build();
        Group root01 = new Group.Builder().name("Root 01").build();
        Group root010 = new Group.Builder().name("Root 010").build();

        groupCtrl.createGroup(root0);
        groupCtrl.createGroup(root1);
        groupCtrl.createGroup(root2);
        groupCtrl.createGroup(root00);
        groupCtrl.createGroup(root01);

        assertEquals(5, groupCtrl.getAllGroups().size());
        for (Group group : groupCtrl.getAllGroups()) {
            assertTrue(groupCtrl.getSubgroups(group).isEmpty());
        }
        assertTrue(groupCtrl.getSubgroups(root0).isEmpty());      
        assertEquals(5, groupCtrl.getSubgroups(null).size());
        // add subgroups
        groupCtrl.addSubGroup(root00, root0);
        groupCtrl.addSubGroup(root01, root0);
        groupCtrl.addSubGroup(root010, root01);
        assertEquals(3, groupCtrl.getSubGroupsRecursive(root0).size());
        assertEquals(2, groupCtrl.getSubgroups(root0).size());        
        assertEquals(3, groupCtrl.getSubgroups(null).size());
        // replace one subgroup to the root
        groupCtrl.addSubGroup(root00, null);     
        assertEquals(4, groupCtrl.getSubgroups(null).size());
        assertEquals(1, groupCtrl.getSubgroups(root0).size());
        assertEquals(2, groupCtrl.getSubGroupsRecursive(root0).size());
        // remove one subgroup
        groupCtrl.addSubGroup(root00, root0);
        assertEquals(3, groupCtrl.getSubgroups(null).size());
        assertEquals(2, groupCtrl.getSubgroups(root0).size());
        groupCtrl.removeGroup(root01);
        assertEquals(3, groupCtrl.getSubgroups(null).size());
        assertEquals(1, groupCtrl.getSubgroups(root0).size());
        assertEquals(1, groupCtrl.getSubGroupsRecursive(root0).size());
        // remove one root group and one child     
        groupCtrl.removeGroup(root0);
        assertEquals(2, groupCtrl.getSubgroups(null).size());
        //check db size
        assertEquals(2, groupCtrl.getAllGroups().size());
    }
}
