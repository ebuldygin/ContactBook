package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.clearAll", query = "DELETE FROM Person")})
public class Person implements Serializable {

    public static enum Sex {

        MALE("Мужской"),
        FEMALE("Женский"),
        OTHER("Неизвестно");

        private Sex(String name) {
            this.name = name;
        }
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String pseudo;
    private Sex sex = Sex.OTHER;
    private String email;
    private boolean favorite;
    private String comment;
    @ManyToMany
    protected Set<Group> groups = new HashSet<>();

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
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
        final Person other = (Person) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public static class Builder {

        private Person item;

        public Builder() {
            this.item = new Person();
        }

        public Builder id(long id) {
            this.item.id = id;
            return this;
        }

        public Builder name(String name) {
            this.item.name = name;
            return this;
        }

        public Builder pseudo(String pseudo) {
            this.item.pseudo = pseudo;
            return this;
        }

        public Builder sex(Sex sex) {
            this.item.sex = sex;
            return this;
        }

        public Builder email(String email) {
            this.item.email = email;
            return this;
        }

        public Builder favorite(boolean favorite) {
            this.item.favorite = favorite;
            return this;
        }

        public Builder comment(String comment) {
            this.item.comment = comment;
            return this;
        }

        public Builder groups(Set<Group> groups) {
            this.item.groups = groups;
            return this;
        }

        public Person build() {
            return this.item;
        }
    }
}
