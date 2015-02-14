package controllers.guice;

import controllers.PersonController;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import model.Group;
import model.Person;

public class PersonGuiceController implements PersonController {

    private static final Logger log = Logger
            .getLogger(PersonGuiceController.class.getName());
    @Inject
    private EntityManager em;

    @Override
    public Collection<Person> getAllPersons() {        
        Collection<Person> result = em.createNamedQuery("Person.findAll")
                .getResultList();        
        return result;
    }

    @Override
    public void createPerson(Person person) {        
        em.getTransaction().begin();
        try {
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        }
    }

    @Override
    public void updatePerson(Person person) {        
        em.getTransaction().begin();
        try {
            em.merge(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        }
    }

    @Override
    public void removePerson(Person person) {        
        em.getTransaction().begin();
        try {
            person = em.find(Person.class, person.getId());
            for(Group group : person.getGroups()) {
                group.getPersons().remove(person);
            }
            em.remove(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        }
    }

    @Override
    public void removeAllPersons() {        
        em.getTransaction().begin();
        try {
            em.createNamedQuery("Person.clearAll").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        }
    }

    @Override
    public void addPersonToGroup(Person person, Group group) {
        if(group == null) {
            return;
        }        
        em.getTransaction().begin();
        try {
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
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        }
    }

    @Override
    public void removePersonFromGroup(Person person, Group group) {        
        em.getTransaction().begin();
        try {
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
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        }
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
                    + "WHERE g = :group ").setParameter("group", group)
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
