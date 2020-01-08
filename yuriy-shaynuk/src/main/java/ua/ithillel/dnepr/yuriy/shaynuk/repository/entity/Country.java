package ua.ithillel.dnepr.yuriy.shaynuk.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

//"country_id";"city_id";"name"
@Getter
@Setter
public class Country extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    private Integer Id;
    private Integer city_id;
    private String name;

}
