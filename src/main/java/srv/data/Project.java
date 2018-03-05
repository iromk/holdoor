package srv.data;

import javax.persistence.*;

@Entity
@Table(name = "project")
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pid;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumns({
//    @JoinColumn(name="uid", referencedColumnName="uid"), //, nullable = false)
    @JoinColumn(name="uid", referencedColumnName="uid") //, nullable = false)
//    })
    private User holderUid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getHolderUid() {
        return holderUid;
    }

    public void setHolderUid(User holderUid) {
        this.holderUid = holderUid;
    }

    @Override
    public boolean equals(Object obj) {
        Project that = (Project) obj;
        return this.name.equals(that.name);
    }
}
