package ua.ithillel.dnepr.roman.gizatulin.repository.cqrs;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsImmutableRepository;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsMutableRepository;
import ua.ithillel.dnepr.common.repository.cqrs.Observer;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CqrsCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType>
        implements CqrsCrudRepository<EntityType, IdType> {

    private final CqrsImmutableRepository<EntityType, IdType> immutableRepository;
    private final CqrsMutableRepository<EntityType, IdType> mutableRepository;


    public CqrsCrudRepositoryImpl() {
        immutableRepository = new CqrsImmutableRepositoryImp<>(null);
        mutableRepository = new CqrsMutableRepositoryImpl<>();
        for (Observer<EntityType, IdType> observer : immutableRepository.getObservers()) {
            mutableRepository.addListener(observer);
        }
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
        Optional<List<EntityType>> result = Optional.empty();
        return result;
    }

    @Override
    public EntityType create(EntityType entity) {
        EntityType result = mutableRepository.create(entity);
        return result;
    }

    @Override
    public EntityType update(EntityType entity) {
        EntityType result = mutableRepository.update(entity);
        return result;
    }

    @Override
    public EntityType delete(IdType id) {
        return mutableRepository.delete(id);
        //TODO: Remove indexes
    }
}