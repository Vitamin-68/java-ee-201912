package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements MutableRepository<EntityType, IdType>, ImmutableRepository<EntityType, IdType> {


    private final EntityManager entityManager;
    private final Class<? extends EntityType> clazz;
    private EntityTransaction transaction;

    public JpaCrudRepository(EntityManager entityManager, Class<? extends EntityType> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.transaction = entityManager.getTransaction();
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();
        List<EntityType> list = entityManager.createQuery("Select t from " + clazz.getSimpleName() + " t").getResultList();
        if (!list.isEmpty()) {
            result = Optional.of(list);
        }
        return result;
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        Optional<EntityType> result = Optional.empty();
        EntityType entity = entityManager.find(clazz, id);
        if (entity != null) {
            result = Optional.of(entity);
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        Optional<List<EntityType>> result = Optional.empty();
        CriteriaQuery<EntityType> criteria = (CriteriaQuery<EntityType>) entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.select(criteria.from(clazz));
        List<EntityType> list = entityManager.createQuery(criteria).getResultList();
        if (!list.isEmpty()) {
            result = Optional.of(list);
        }
        return result;
    }

    @Override
    public EntityType create(EntityType entity) {
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        transaction.begin();
        entityManager.merge(entity);
        transaction.commit();
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        Optional<EntityType> result = this.findById(id);
        if (!result.isEmpty()) {
            transaction.begin();
            entityManager.remove(result.get());
            transaction.commit();
        }
        return result.orElse(null);
    }

}
