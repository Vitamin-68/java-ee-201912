package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
public class Country extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    private Integer Id;
    private Integer city_id;
    private String name;

    @Override
    public Integer getId(){return Id;}

}
