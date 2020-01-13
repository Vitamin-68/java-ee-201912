package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
public class City extends AbstractEntity<Integer> {

    private Integer id;
    private Integer country_id;
    private Integer region_id;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

}
