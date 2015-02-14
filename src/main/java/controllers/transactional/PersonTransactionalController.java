package controllers.transactional;

import controllers.PersonController;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.Collection;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import model.Group;
import model.Person;

public class PersonTransactionalController implements PersonController {

    private static final Logger log = Logger.getLogger(PersonTransactionalController.class.getName());
    @Inject
    private EntityManager em;

    @Override
    public Collection<Person> getAllPersons() {
        Collection<Person> result = em.createNamedQuery("Person.findAll")
                .getResultList();
        return result;
    }

    @Override
    @Transactional
    public void createPerson(Person person) {
        em.persist(person);
    }

    @Override
    @Transactional
    public void updatePerson(Person person) {
        em.merge(person);
    }

    @Override
    @Transactional
    public void removePerson(Person person) {
        person = em.find(Person.class, person.getId());
        for (Group group : person.getGroups()) {
            group.getPersons().remove(person);
        }
        em.remove(person);
    }

    @Override
    @Transactional
    public void removeAllPersons() {
        em.createNamedQuery("Person.clearAll").executeUpdate();
    }

    @Override
    @Transactional
    public void addPersonToGroup(Person person, Group group) {
        if (group == null) {
            return;
        }
        if (person.getId() != null) {
            person = em.find(Person.class, person.getId());
        } else {
            em.persist(person);
        }
        if (group != null && group.getId() != null) {
            group = em.find(Group.class, group.getId());
        } else {
            em.persist(group);
        }
        group.getPersons().add(person);
    }

    @Override
    @Transactional
    public void removePersonFromGroup(Person person, Group group) {
        if (person.getId() != null) {
            person = em.find(Person.class, person.getId());
        } else {
            em.persist(person);
        }
        if (group != null && group.getId() != null) {
            group = em.find(Group.class, group.getId());
        } else {
            em.persist(group);
        }
        group.getPersons().remove(person);
    }

    @Override
    public Collection<Person> personsInGroup(Group group) {
        Collection<Person> result;
        if (group == null) {
            result = em.createQuery("SELECT p "
                    + "FROM Person p LEFT OUTER JOIN p.groups g "
                    + "WHERE g IS NULL")
                    .getResultList();
        } else {
            result = em.createQuery("SELECT p "
                    + "FROM Person p LEFT OUTER JOIN p.groups g "
                    + "WHERE g = :group ")
                    .setParameter("group", group)
                    .getResultList();
        }
        return result;
    }

    @Override
    public Collection<Group> groupsForPerson(Person person) {
        Collection<Group> result;
        result = em.createQuery("SELECT g "
                + "FROM GroupNode g LEFT OUTER JOIN g.persons p "
                + "WHERE p = :person ")
                .setParameter("person", person)
                .getResultList();
        return result;
    }
}
