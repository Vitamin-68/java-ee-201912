package ua.ithillel.alex.tsiba.repository.repositories.jpa;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public class CrudRepository<EntityType extends AbstractEntity<Integer>> extends AbstractRepository<EntityType>
        implements ua.ithillel.dnepr.common.repository.CrudRepository<EntityType, Integer> {

    private final ImmutableRepository<EntityType> immutableRepository;
    private final MutableRepository<EntityType> mutableRepository;

    public CrudRepository(Class<EntityType> objectClass) {
        super(objectClass);
        immutableRepository = new ImmutableRepository<EntityType>(objectClass);
        mutableRepository = new MutableRepository<EntityType>(objectClass);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return immutableRepository.findAll();
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
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
    public EntityType delete(Integer id) {
        return mutableRepository.delete(id);
    }
}
