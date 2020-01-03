package repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CrudRepository<EntityType extends BaseEntity<IdType>, IdType>
        implements ua.ithillel.dnepr.common.repository.CrudRepository<EntityType, IdType> {
    private String filePath;
    private final ImmutableRepository<EntityType, IdType> immutableRepository;
    private final MutableRepository<EntityType, IdType> mutableRepository;

    public CrudRepository(String filePath) {
        this.filePath = filePath;
        immutableRepository = new ImmutableRepositoryImp<>(filePath);
        mutableRepository = new MutableRepositoryImp<>(filePath);
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
        return immutableRepository.findByField(fieldName,value);
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
