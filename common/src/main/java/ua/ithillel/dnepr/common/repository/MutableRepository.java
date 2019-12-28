package ua.ithillel.dnepr.common.repository;

public interface MutableRepository<EntityType extends BaseEntity<IdType>, IdType> {
    EntityType create(EntityType entity);

    EntityType update(EntityType entity);

    EntityType delete(IdType id);
}
