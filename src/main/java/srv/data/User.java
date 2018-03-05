package srv.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @Override
    public boolean equals(Object o) {
        User that = (User) o;
        return this.id.equals(that.getId()) && this.name.equals(that.name);
    }
}


