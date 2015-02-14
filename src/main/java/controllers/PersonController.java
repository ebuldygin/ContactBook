package controllers;

import java.util.Collection;
import model.Group;
import model.Person;

public interface PersonController {

    void addPersonToGroup(Person person, Group group);

    void createPerson(Person person);

    Collection<Person> getAllPersons();

    Collection<Group> groupsForPerson(Person person);

    Collection<Person> personsInGroup(Group group);

    void removeAllPersons();

    void removePerson(Person person);

    void removePersonFromGroup(Person person, Group group);

    void updatePerson(Person person);
    
}
