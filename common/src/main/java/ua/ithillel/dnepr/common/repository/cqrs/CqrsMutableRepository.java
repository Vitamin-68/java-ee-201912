package ua.ithillel.dnepr.common.repository.cqrs;

import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public interface CqrsMutableRepository<EntityType extends AbstractEntity<IdType>, IdType>
        extends MutableRepository<EntityType, IdType>,
        Observable<EntityType, IdType> {
}
