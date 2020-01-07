package repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
public class CsvEntity extends AbstractEntity {
    private int id;
    private int region_id, country_id, city_id;
    private String name;
}
