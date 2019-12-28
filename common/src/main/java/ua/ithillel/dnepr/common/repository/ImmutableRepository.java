package ua.ithillel.dnepr.common.repository;

import java.util.List;
import java.util.Optional;

public interface ImmutableRepository<EntityType extends BaseEntity<IdType>, IdType> {
    Optional<List<EntityType>> findAll();

    Optional<EntityType> findById(IdType id);

    Optional<EntityType> findByField(String fieldName, Object value);
}
