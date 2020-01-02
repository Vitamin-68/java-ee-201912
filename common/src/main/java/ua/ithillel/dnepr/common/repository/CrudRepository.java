package ua.ithillel.dnepr.common.repository;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface CrudRepository<EntityType extends BaseEntity<IdType>, IdType>
        extends ImmutableRepository<EntityType, IdType>, MutableRepository<EntityType, IdType> {
}
