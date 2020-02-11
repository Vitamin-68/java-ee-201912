package vitaly.mosin.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.repository.entity.City;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "country")
public class CountryJpa extends AbstractEntity<Integer> {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CityJpa> citiesJpa;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RegionJpa> regionsJpa;

    private String name;

    public CountryJpa() {
    }

//    public CountryJpa(Integer id, String name) {
//        this.id = id;
//        this.name = name;
//    }
}
