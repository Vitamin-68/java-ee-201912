package repository;

import repository.entity.CsvEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public class EntityFactory<IdType> {
    public BaseEntity<IdType> getEntity(Integer idType) {
        return new CsvEntity();
    }
}
