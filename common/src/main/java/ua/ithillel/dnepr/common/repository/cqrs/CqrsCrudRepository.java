package ua.ithillel.dnepr.common.repository.cqrs;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public interface CqrsCrudRepository<EntityType extends AbstractEntity<IdType>, IdType>
        extends CrudRepository<EntityType, IdType> {
}
