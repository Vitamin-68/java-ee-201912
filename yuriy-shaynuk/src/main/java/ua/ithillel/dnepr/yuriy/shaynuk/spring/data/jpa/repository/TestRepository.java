package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class TestRepository <EntityType extends AbstractEntity<IdType>, IdType> implements CrudRepository<EntityType, IdType> {

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

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
