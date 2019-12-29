package ua.ithillel.dnepr.common.repository;

public interface IndexedCrudRepository<EntityType extends BaseEntity<IdType>, IdType>
        extends CrudRepository<EntityType, IdType>, IndexedRepository {
}
