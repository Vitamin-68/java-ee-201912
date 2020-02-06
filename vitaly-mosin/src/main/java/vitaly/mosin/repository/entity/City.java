package vitaly.mosin.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
public class City extends AbstractEntity<Integer> {
    @Id
    private Integer id;
    private Integer countryId;
    private Integer regionId;
    private String name;

    public City() {
    }

    public City(Integer id, Integer countryId, Integer regionId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.name = name;
    }
}
