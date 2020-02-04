package ua.ithillel.dnepr.eugenekovalov.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
public class Region extends AbstractEntity<Integer> {
    private Integer country_id;
    private Integer city_id;
    private String name;
}
