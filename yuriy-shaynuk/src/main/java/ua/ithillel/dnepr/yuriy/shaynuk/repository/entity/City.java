package ua.ithillel.dnepr.yuriy.shaynuk.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.Objects;

//"city_id";"country_id";"region_id";"name"

@Getter
@Setter
public class City extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    private Integer country_id;
    private Integer region_id;
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return country_id.equals(city.country_id) &&
                region_id.equals(city.region_id) &&
                getId().equals(city.getId()) &&
                name.equals(city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country_id, region_id, name);
    }
}
