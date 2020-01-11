package ua.ithillel.dnepr.common.repository.cqrs;

import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.List;

public interface CqrsImmutableRepository<EntityType extends AbstractEntity<IdType>, IdType>
        extends ImmutableRepository<EntityType, IdType> {
    List<Observer<EntityType, IdType>> getObservers();
}
