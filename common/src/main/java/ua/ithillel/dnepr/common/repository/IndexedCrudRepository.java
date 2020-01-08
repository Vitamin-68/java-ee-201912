package ua.ithillel.dnepr.common.repository;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface IndexedCrudRepository<EntityType extends BaseEntity<IdType>, IdType>
        extends CrudRepository<EntityType, IdType>, IndexedRepository {
}
