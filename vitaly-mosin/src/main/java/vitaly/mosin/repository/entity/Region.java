package vitaly.mosin.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Region extends AbstractEntity<Integer> {
    @Id
    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;

    public Region() {
    }

    public Region(Integer id, Integer countryId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
    }
}
