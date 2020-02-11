package vitaly.mosin.repository.jpa;

import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class JpaCrudRepository <EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements MutableRepository<EntityType, IdType>, ImmutableRepository<EntityType, IdType> {

    private final Class<? extends EntityType> clazz;
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public JpaCrudRepository(Class<? extends EntityType> clazz, EntityManagerFactory entityManagerFactory) {
        this.clazz = clazz;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.transaction = entityManager.getTransaction();

    }


    @Override
    public Optional<List<EntityType>> findAll() {
        final List<EntityType> result = entityManager.createQuery("SELECT e FROM " + clazz.getName() + " e").getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
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
