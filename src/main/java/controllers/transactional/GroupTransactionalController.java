package controllers.transactional;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import controllers.GroupController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import model.Group;
import model.Person;

public class GroupTransactionalController implements GroupController {

    @Inject
    private EntityManager em;

    @Override
    public Collection<Group> getAllGroups() {
        Collection<Group> result = em.createNamedQuery("GroupNode.findAll")
                .getResultList();
        return result;
    }

    @Override
    public Collection<Group> getSubgroups(Group group) {
        Collection<Group> result;
        if (group == null) {
            result = em.createQuery("SELECT g FROM GroupNode g WHERE g.parent IS NULL")
                    .getResultList();
        } else {
            result = em.createQuery("SELECT g FROM GroupNode g WHERE g.parent=:group")
                    .setParameter("group", group)
                    .getResultList();
        }
        return result;
    }

    @Override
    public Collection<Group> getSubGroupsRecursive(Group group) {
        Collection<Group> result;
        if (group == null) {
            result = em.createQuery("SELECT g FROM GroupNode g")
                    .getResultList();
        } else {
            group = em.find(Group.class, group.getId());
            result = getSubgroupsRecursiveP(group);
        }
        return result;
    }

    private Collection<Group> getSubgroupsRecursiveP(Group parent) {
        Collection<Group> result = new ArrayList<>();
        Collection<Group> children = parent.getSubGroups();
        for (Group group : children) {
            result.add(group);
            if (!group.getSubGroups().isEmpty()) {
                result.addAll(getSubgroupsRecursiveP(group));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void removeAllGroups() {
        em.createNamedQuery("GroupNode.clearAll").executeUpdate();
    }

    @Override
    @Transactional
    public void addSubGroup(Group group, Group parent) {
        if (group.getId() == null) {
            em.persist(group);
        } else {
            group = em.find(Group.class, group.getId());
        }
        if (parent != null) {
            if (parent.getId() == null) {
                em.persist(parent);
            } else {
                parent = em.find(Group.class, parent.getId());
            }
        }
        group.setParent(parent);
    }

    @Override
    @Transactional
    public void createGroup(Group group) {
        addSubGroup(group, null);
    }

    @Override
    @Transactional
    public void updateGroup(Group group) {
        em.merge(group);
    }

    @Override
    @Transactional
    public void removeGroup(Group group) {
        if (group.getId() == null) {
            return;
        }
        group = em.find(Group.class, group.getId());
        group.setParent(null);
        em.remove(group);
    }

    @Override
    public int countPersonsInGroupRecursive(Group group) {
        Long result;
        if (group == null) {
            result = (Long) em.createQuery("SELECT COUNT(p.id) FROM Person p")
                    .getSingleResult();
        } else {
            group = em.find(Group.class, group.getId());
            Collection<Group> allGroups = getSubgroupsRecursiveP(group);
            Set<Person> persons = new HashSet<>(group.getPersons());
            for (Group g : allGroups) {
                persons.addAll(g.getPersons());
            }
            result = new Long(persons.size());
        }
        return result.intValue();
    }

    @Override
    public int countPersonsInGroup(Group group) {
        Long result;
        if (group == null) {
            result = (Long) em.createQuery("SELECT COUNT(p) "
                    + "FROM Person p LEFT OUTER JOIN p.groups g "
                    + "WHERE g IS NULL")
                    .getSingleResult();
        } else {
            result = (Long) em.createQuery("SELECT COUNT(p) "
                    + "FROM Person p LEFT OUTER JOIN p.groups g "
                    + "WHERE g = :group ")
                    .setParameter("group", group)
                    .getSingleResult();
        }
        return result.intValue();
    }
}
