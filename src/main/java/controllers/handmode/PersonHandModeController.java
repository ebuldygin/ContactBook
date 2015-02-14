package controllers.handmode;

import controllers.PersonController;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Group;
import model.Person;

public class PersonHandModeController implements PersonController {

    private static final Logger log = Logger
            .getLogger(PersonHandModeController.class.getName());
    @Inject
    private EntityManagerFactory emf;

    @Override
    public Collection<Person> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        Collection<Person> result = em.createNamedQuery("Person.findAll")
                .getResultList();
        em.close();
        return result;
    }

    @Override
    public void createPerson(Person person) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void updatePerson(Person person) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void removePerson(Person person) {
        EntityManager em = emf.createEntityManager();
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
        } finally {
            em.close();
        }
    }

    @Override
    public void removeAllPersons() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.createNamedQuery("Person.clearAll").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void addPersonToGroup(Person person, Group group) {
        if(group == null) {
            return;
        }
        EntityManager em = emf.createEntityManager();
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
            person.getGroups().add(group);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void removePersonFromGroup(Person person, Group group) {
        EntityManager em = emf.createEntityManager();
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
            person.getGroups().remove(group);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Person> personsInGroup(Group group) {
        Collection<Person> result;
        EntityManager em = emf.createEntityManager();
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
        em.close();
        return result;
    }

    @Override
    public Collection<Group> groupsForPerson(Person person) {
        Collection<Group> result;
        EntityManager em = emf.createEntityManager();
        result = em.createQuery("SELECT g "
                + "FROM GroupNode g LEFT OUTER JOIN g.persons p "
                + "WHERE p = :person ")
                .setParameter("person", person)
                .getResultList();
        em.close();
        return result;
    }

    public String toString() {
        return "PersonController " + emf;
    }
}
