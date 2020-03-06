package ua.ithillel.dnepr.tymoshenko.olga.jpa.jpaentity;
import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Worker")
public class Person extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDWorker", nullable = false, unique = true)
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;

    public Person() {
    }

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "adressID")
    private Adress adress;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "CompanyWorker",
            joinColumns = @JoinColumn(name = "workerID"),
            inverseJoinColumns = @JoinColumn(name = "companyID")
    )
    private List<Company> workingPlaces;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return getFirstName().equals(person.getFirstName()) &&
                getLastName().equals(person.getLastName()) &&
                Objects.equals(getAge(), person.getAge()) &&
                getAdress().equals(person.getAdress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getAge(), getAdress());
    }
}
