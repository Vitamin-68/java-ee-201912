package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

//"region_id";"country_id";"city_id";"name"
@Getter
@Setter
public class Region extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    private Integer Id;
    private Integer country_id;
    private Integer city_id;
    private String name;

}
