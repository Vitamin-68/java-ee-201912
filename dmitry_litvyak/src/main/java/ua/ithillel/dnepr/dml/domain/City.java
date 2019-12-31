package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.BaseEntity;

//"city_id";"country_id";"region_id";"name"

@Getter
@Setter
public class City implements BaseEntity<Integer> {

    private Integer Id;
    private Integer country_id;
    private Integer region_id;
    private String name;
    @Override
    public Integer getId() {
        return Id;
    }
}
