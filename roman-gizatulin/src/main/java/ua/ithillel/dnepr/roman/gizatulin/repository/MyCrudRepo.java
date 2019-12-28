package ua.ithillel.dnepr.roman.gizatulin.repository;

import ua.ithillel.dnepr.common.repository.BaseEntity;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public class MyCrudRepo<EntityType extends BaseEntity<IdType>, IdType> implements CrudRepository<EntityType, IdType> {
    private String filePath;

    public MyCrudRepo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findByField(String fieldName, Object value) {
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
