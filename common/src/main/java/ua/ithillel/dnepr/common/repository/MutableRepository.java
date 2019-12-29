package ua.ithillel.dnepr.common.repository;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface MutableRepository<EntityType extends BaseEntity<IdType>, IdType> {
    EntityType create(EntityType entity);

    EntityType update(EntityType entity);

    EntityType delete(EntityType entity);
}
