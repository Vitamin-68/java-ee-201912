package repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Getter
@Setter
public class CsvEntity implements BaseEntity<Integer> {
    private int id;
    private int region_id, country_id, city_id;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
