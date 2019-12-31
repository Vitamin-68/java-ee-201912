package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.BaseEntity;

//"region_id";"country_id";"city_id";"name"
@Getter
@Setter
public class Region implements BaseEntity<Integer> {

    private Integer Id;
    private Integer country_id;
    private Integer city_id;
    private String name;

    @Override
    public Integer getId() {
        return Id;
    }
}
