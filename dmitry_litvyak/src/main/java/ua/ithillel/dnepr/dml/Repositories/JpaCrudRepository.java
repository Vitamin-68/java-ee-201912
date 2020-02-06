package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.persistence.Root;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements MutableRepository<EntityType, IdType>, ImmutableRepository<EntityType, IdType> {


    private final EntityManager entityManager;
    private final Class<? extends EntityType> clazz;

    public JpaCrudRepository(EntityManager entityManager, Class<? extends EntityType> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;

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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<? extends EntityType> criteria = criteriaBuilder.createQuery(clazz);
        TypedQuery<? extends EntityType> query = entityManager.createQuery(criteria);
        List<EntityType> list = (List<EntityType>) query.getResultList();
        if (!list.isEmpty()) {
            result = Optional.of(list);
        }
        return result;
    }

    @Override
    public EntityType create(EntityType entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();
        entityManager.flush();
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        create(entity);
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        Optional<EntityType> result = this.findById(id);
        if (!result.isEmpty()) {
            entityManager.detach(result.get());
        }
        return result.orElse(null);
    }

}
