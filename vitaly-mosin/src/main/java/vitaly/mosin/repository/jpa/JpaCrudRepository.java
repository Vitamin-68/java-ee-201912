package vitaly.mosin.repository.jpa;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.repository.exceptions.ExceptionResponseCode;
import vitaly.mosin.repository.exceptions.MyRepoException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements MutableRepository<EntityType, IdType>, ImmutableRepository<EntityType, IdType> {

    private final Class<? extends EntityType> clazz;
    private final EntityManager entityManager;
    private static final String QUERY_SELECT_ALL = "SELECT e FROM %s e";
    private static final String QUERY_SELECT_BY_ID = "SELECT e FROM %s e WHERE e.id = %s";
    private static final String QUERY_SELECT_BY_FIELD = "SELECT e FROM %s e WHERE e.%s = %s";

    public JpaCrudRepository(Class<? extends EntityType> clazz, EntityManagerFactory entityManagerFactory) {
        this.clazz = clazz;
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_ALL, clazz.getSimpleName());
        final List<EntityType> result = entityManager.createQuery(queryString).getResultList();
        transaction.commit();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        EntityType result = null;
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_BY_ID, clazz.getSimpleName(), id);
        try {
            result = (EntityType) entityManager.createQuery(queryString).getSingleResult();
        } catch (NoResultException e) {
            log.error("Entity not found", e);
        } finally {
            transaction.commit();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_BY_FIELD, clazz.getSimpleName(), fieldName, value);
        final List<EntityType> result = entityManager.createQuery(queryString).getResultList();
        transaction.commit();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @SneakyThrows
    @Override
    public EntityType create(EntityType entity) {
        if (isEntityExists(entity)) {
            log.error("Entity already exists");
            throw new MyRepoException(ExceptionResponseCode.ENTITY_ALREADY_EXIST, "Entity already exist");
        }
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity);
        transaction.commit();
        return entity;
    }

    private boolean isEntityExists(EntityType entity) {
        return (entityManager.find(entity.getClass(), entity.getId()) != null);
    }

    @Override
    public EntityType update(EntityType entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity);
        transaction.commit();
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        EntityTransaction transaction = entityManager.getTransaction();
        EntityType entity = findById(id).get();
        transaction.begin();
        entityManager.remove(entity);
        transaction.commit();
        return entity;
    }
}
