package ua.ithillel.dnepr.yuriy.shaynuk.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.Objects;

//"region_id";"country_id";"city_id";"name"
@Getter
@Setter
public class Region extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    private Integer country_id;
    private Integer city_id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return country_id.equals(region.country_id) &&
                city_id.equals(region.city_id) &&
                getId().equals(region.getId()) &&
                name.equals(region.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country_id, city_id, name);
    }
}
