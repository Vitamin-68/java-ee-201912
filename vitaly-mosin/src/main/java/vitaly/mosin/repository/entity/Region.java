package vitaly.mosin.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
public class Region extends AbstractEntity<Integer> {

    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;

    public Region() {
    }

    public Region(Integer id, Integer countryId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.cityId = cityId;
        this.name = name;
    }
}
