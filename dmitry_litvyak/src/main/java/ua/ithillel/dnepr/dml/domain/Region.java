package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
public class Region extends AbstractEntity<Integer> {
    private Integer country_id;
    private Integer city_id;
    private String name;
}
