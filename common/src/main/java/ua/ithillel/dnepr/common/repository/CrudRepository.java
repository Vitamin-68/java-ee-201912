package ua.ithillel.dnepr.common.repository;

public interface CrudRepository<EntityType extends BaseEntity<IdType>, IdType>
        extends ImmutableRepository<EntityType, IdType>, MutableRepository<EntityType, IdType> {
}
