package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table
public class City extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;
    private Integer countryId;
    private Integer regionId;
    private String name;

    public City() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return getCountryId().equals(city.getCountryId()) &&
                getName().equals(city.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountryId(), getName());
    }
}
