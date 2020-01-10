package ua.ithillel.dnepr.yuriy.shaynuk.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.Objects;

//"country_id";"city_id";"name"
@Getter
@Setter
public class Country extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return city_id.equals(country.city_id) &&
                getId().equals(country.getId()) &&
                name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city_id, name);
    }

    private Integer city_id;
    private String name;
}
