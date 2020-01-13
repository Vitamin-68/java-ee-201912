package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
public class Country extends AbstractEntity<Integer> {

    private Integer id;
    private Integer city_id;
    private String name;

    @Override
    public Integer getId(){return id;}

}
