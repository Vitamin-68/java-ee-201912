package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsCrudRepository;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsImmutableRepository;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsMutableRepository;
import ua.ithillel.dnepr.common.repository.cqrs.Observer;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CqrsRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        implements CqrsCrudRepository<EntityType, IdType> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;
    private final CqrsImmutableRepository<EntityType, IdType> immutableRepository;
    private final CqrsMutableRepository<EntityType, IdType> mutableRepository;
    private final Set<String> indexes = Collections.synchronizedSet(new HashSet<>());

    public CqrsRepositoryImp(Connection connection, Class<? extends EntityType> clazz, CrudRepository<EntityType, IdType> crudRepository) {
        this.connection = connection;
        this.clazz = clazz;

        immutableRepository = new CqrsImmutableRepositoryImp<>(crudRepository);
        mutableRepository = new CqrsMutableRepositoryImp<>(this.connection, this.clazz);

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
