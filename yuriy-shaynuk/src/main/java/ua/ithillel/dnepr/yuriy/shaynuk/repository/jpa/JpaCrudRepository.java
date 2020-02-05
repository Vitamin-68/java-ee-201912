package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaCrudRepository<EntityType extends AbstractEntity<IdType>, IdType> implements CrudRepository<EntityType, IdType> {
    private final JpaImmutableRepository<EntityType, IdType> immutableRepository;
    private final JpaMutableRepository<EntityType, IdType> mutableRepository;

//    private static <T> List<T> loadAllData(Class<T> type, Session session) {
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<T> criteria = builder.createQuery(type);
//        criteria.from(type);
//        return session.createQuery(criteria).getResultList();
//    }

    public JpaCrudRepository(EntityManager entityManager, Class<EntityType> clazz) {
//        CriteriaQuery<EntityType> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
//        criteria.select(criteria.from(clazz));
//        List<EntityType> ListOfEmailDomains = entityManager.createQuery(criteria).getResultList();

        immutableRepository = new JpaImmutableRepository<>(entityManager,clazz);
        mutableRepository = new JpaMutableRepository<>(entityManager);
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
        EntityType newEntity = null;
        if(findById(entity.getId()).isEmpty()){
            newEntity = mutableRepository.create(entity);
        }
        return newEntity;
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
