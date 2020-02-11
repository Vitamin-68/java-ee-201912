package vitaly.mosin.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "region")
public class RegionJpa extends AbstractEntity<Integer> {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private CountryJpa country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CityJpa> citiesJpa;

    private String name;

    public RegionJpa() {
    }

//    public RegionJpa(Integer id, Integer countryId, String name) {
//        this.id = id;
//        this.countryId = countryId;
//        this.name = name;
//    }
}
