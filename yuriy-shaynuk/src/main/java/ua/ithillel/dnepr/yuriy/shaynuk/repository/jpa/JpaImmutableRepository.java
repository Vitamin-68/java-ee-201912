package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public class JpaImmutableRepository<EntityType extends AbstractEntity<IdType>, IdType> implements ImmutableRepository<EntityType, IdType> {

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }
}
