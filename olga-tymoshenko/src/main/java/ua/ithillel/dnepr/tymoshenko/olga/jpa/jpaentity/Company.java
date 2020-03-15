package ua.ithillel.dnepr.tymoshenko.olga.jpa.jpaentity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class Company extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCompany", nullable = false, unique = true)
    private Integer id;
    private String company;
    private String description;
    private String city;

    public Company() {
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "CompanyWorker",
            joinColumns = @JoinColumn(name = "companyID"),
            inverseJoinColumns = @JoinColumn(name = "workerID")
    )
    private List<Person> persons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company1 = (Company) o;
        return getCompany().equals(company1.getCompany()) &&
                Objects.equals(getDescription(), company1.getDescription()) &&
                getCity().equals(company1.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompany(), getDescription(), getCity());
    }
}
