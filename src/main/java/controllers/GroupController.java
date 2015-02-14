package controllers;

import java.util.Collection;
import model.Group;

public interface GroupController {

    void addSubGroup(Group group, Group parent);

    int countPersonsInGroup(Group group);

    int countPersonsInGroupRecursive(Group group);

    void createGroup(Group group);

    Collection<Group> getAllGroups();

    Collection<Group> getSubgroups(Group group);

    Collection<Group> getSubGroupsRecursive(Group group);

    void removeAllGroups();

    void removeGroup(Group group);

    void updateGroup(Group group);
    
}
