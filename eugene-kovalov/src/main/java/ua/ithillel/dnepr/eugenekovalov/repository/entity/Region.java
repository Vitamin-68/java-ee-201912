package ua.ithillel.dnepr.eugenekovalov.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Getter
@Setter
public class Region implements BaseEntity<Integer> {
    private int id;
    private int countryId;
    private int cityId;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public Integer getCityId() {
        return cityId;
    }
}
