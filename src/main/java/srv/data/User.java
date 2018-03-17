package srv.data;

import common.core.App;
import org.hibernate.annotations.GenericGenerator;
import srv.JPAFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user")
public class User implements Serializable {

//    @Column(name = "id")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //generator = "increment")
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User() {}

    public User(String firstName, String lastName, String uid) {
        setName(new Name(firstName, lastName));
        setUid(uid);
    }


    @Column(unique = true, name = "uid")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "holderUid")
    private Collection<Project> projects = new ArrayList<>();

    Name name;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public static User findByUid(String uid) {
        User returnValue = null;

        EntityManager entityManager = JPAFactory.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery =  criteriaBuilder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        criteriaQuery.select(from);
        criteriaQuery.where(criteriaBuilder.equal(from.get("uid"), uid));
        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
        try {
            returnValue = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            App.log().severe("Entity not found by given uid \"" + uid + "\". Not fatal, return null.");
        }

        return returnValue;
    }

    @Override
    public boolean equals(Object o) {
        User that = (User) o;
        return this.id.equals(that.getId()) && this.name.equals(that.name);
    }
}


