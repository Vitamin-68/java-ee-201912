package ua.ithillel.dnepr.tymoshenko.olga.jpa.jpaentity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class Adress extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAdress", nullable = false, unique = true)
    private Integer id;
    private String city;
    private String street;
    private String apart;

    public Adress() {
    }

    @OneToMany(mappedBy = "adress", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Person> tenants;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adress adress = (Adress) o;
        return getCity().equals(adress.getCity()) &&
                getStreet().equals(adress.getStreet()) &&
                Objects.equals(getApart(), adress.getApart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getApart());
    }
}
