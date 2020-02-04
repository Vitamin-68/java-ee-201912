package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements MutableRepository<EntityType, IdType>, ImmutableRepository<EntityType, IdType>, Closeable {

    private static final String PERSISTENT_UNIT = "persistence-unit-dml";
    private final EntityManagerFactory entityFactory;
    private final EntityManager entityManager;
    private final Class<? extends EntityType> clazz;

    public JpaCrudRepository(Class<? extends EntityType> clazz) {
        this.clazz = clazz;
        this.entityFactory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT);
        this.entityManager = this.entityFactory.createEntityManager();
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
        CriteriaQuery<EntityType> criteria = (CriteriaQuery<EntityType>) entityManager.getCriteriaBuilder();
        TypedQuery<EntityType> query = entityManager.createQuery(criteria);
        List<EntityType> list = query.getResultList();
        if (!list.isEmpty()) {
            result = Optional.of(list);
        }
        return result;
    }

    @Override
    public EntityType create(EntityType entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        entityManager.persist(entity);
        entityManager.flush();
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


    @Override
    public void close() throws IOException {
        entityManager.close();
        entityFactory.close();
    }
}
