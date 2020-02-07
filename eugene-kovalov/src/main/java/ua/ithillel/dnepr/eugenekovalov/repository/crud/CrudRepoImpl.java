package ua.ithillel.dnepr.eugenekovalov.repository.crud;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CrudRepoImpl<EntityType extends AbstractEntity<IdType>, IdType>
        implements CrudRepository<EntityType, IdType> {

    private final ImmutableRepository<EntityType, IdType> immutableRepository;
    private final MutableRepository<EntityType, IdType> mutableRepository;

    public CrudRepoImpl(Path path, Class<EntityType> entityTypeClass) {
        this.immutableRepository = new ImmutableRepoImpl<>(path, entityTypeClass);
        this.mutableRepository = new MutableRepositoryImpl<>(path, entityTypeClass);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return immutableRepository.findAll();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return immutableRepository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return immutableRepository.findByField(fieldName, value);
    }

    @Override
    public EntityType create(EntityType entity) {
        return mutableRepository.create(entity);
    }

    @Override
    public EntityType update(EntityType entity) {
        return mutableRepository.update(entity);
    }

    @Override
    public EntityType delete(IdType id) {
        return mutableRepository.delete(id);
    }
}
