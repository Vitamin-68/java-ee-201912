package ua.ithillel.dnepr.common.repository.cqrs;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public interface Observer<EntityType extends AbstractEntity<IdType>, IdType> {
    void update(EntityType entity);
}
