package vitaly.mosin.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "city")
public class CityJpa extends AbstractEntity<Integer> {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private CountryJpa country;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id")
    private RegionJpa region;

    private String name;

    public CityJpa() {
    }
}
