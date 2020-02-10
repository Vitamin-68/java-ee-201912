package vitaly.mosin.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
@ToString
public class City extends AbstractEntity<Integer> {

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
