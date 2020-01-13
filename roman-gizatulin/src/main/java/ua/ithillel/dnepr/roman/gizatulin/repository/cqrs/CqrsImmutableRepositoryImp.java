package ua.ithillel.dnepr.roman.gizatulin.repository.cqrs;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsImmutableRepository;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.cqrs.Observer;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CqrsImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        implements CqrsImmutableRepository<EntityType, IdType> {
    private IndexedCrudRepository<EntityType, IdType> repository;

    public CqrsImmutableRepositoryImp(IndexedCrudRepository<EntityType, IdType> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return repository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return repository.findByField(fieldName, value);
    }

    @Override
    public List<Observer<EntityType, IdType>> getObservers() {
        return Collections.singletonList(entity -> {
            if (repository.findById(entity.getId()).isPresent()) {
                repository.update(entity);
            } else {
                repository.create(entity);
            }
        });
    }
}
