package ua.ithillel.dnepr.olga.tymoshenko.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Setter
@Getter
public class Region implements BaseEntity<Integer> {

    private Integer regionId;
    private Integer countryId;
    private Integer cityId;
    private String name;

    @Override
    public Integer getId() {
        return null;
    }


}
