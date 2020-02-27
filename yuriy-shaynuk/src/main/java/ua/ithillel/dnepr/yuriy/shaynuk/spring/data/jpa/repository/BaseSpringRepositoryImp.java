package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class BaseSpringRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType> implements ua.ithillel.dnepr.common.repository.CrudRepository<EntityType, IdType> {
    CrudRepository<EntityType, IdType> crudRepository;

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.of(StreamSupport.stream(crudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return crudRepository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public EntityType create(EntityType entity) {
        return crudRepository.save(entity);
    }

    @Override
    public EntityType update(EntityType entity) {
        return crudRepository.save(entity);
    }

    @Override
    public EntityType delete(IdType id) {
        Optional<EntityType> deleteEntity = crudRepository.findById(id);
        crudRepository.deleteById(id);
        return deleteEntity.orElseThrow();
    }
}
