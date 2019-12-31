package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.BaseEntity;

//"country_id";"city_id";"name"
@Getter
@Setter
public class Country implements BaseEntity<Integer> {

    private Integer Id;
    private Integer city_id;
    private String name;

    @Override
    public Integer getId() {
        return Id;
    }
}
