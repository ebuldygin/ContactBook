package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity(name = "GroupNode")
@NamedQueries({
    @NamedQuery(name = "GroupNode.findAll", query = "SELECT g FROM GroupNode g"),
    @NamedQuery(name = "GroupNode.clearAll", query = "DELETE FROM GroupNode")
})
public class Group implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Group parent;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Group> subgroups = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private List<Person> persons = new ArrayList<>();

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public Set<Group> getSubgroups() {
        return subgroups;
    }

    public void setSubgroups(Set<Group> subGroups) {
        this.subgroups = subgroups;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Group other = (Group) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public static class Builder {

        private Group item;

        public Builder() {
            this.item = new Group();
        }

        public Builder id(long id) {
            this.item.id = id;
            return this;
        }

        public Builder name(String name) {
            this.item.name = name;
            return this;
        }

        public Builder parent(Group parent) {
            this.item.parent = parent;
            return this;
        }

        public Builder subGroups(Set<Group> subGroups) {
            this.item.subgroups = subGroups;
            return this;
        }

        public Builder persons(List<Person> persons) {
            this.item.persons = persons;
            return this;
        }

        public Group build() {
            return this.item;
        }
    }
}
