package srv.data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Name implements Serializable {

    private String first;

    private String last;

    public Name() {}

    public Name(String firstName, String lastName) {
        setFirst(firstName);
        setLast(lastName);
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    @Override
    public boolean equals(Object o) {
        Name that = (Name) o;
        return this.first.equals(that.getFirst()) && this.last.equals(that.getLast());
    }

}