package vitaly.mosin.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "region")
public class RegionJpa extends AbstractEntity<Integer> {
    @Id
    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;

    public RegionJpa() {
    }

    public RegionJpa(Integer id, Integer countryId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
    }
}
