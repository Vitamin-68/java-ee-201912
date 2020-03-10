package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class JpaImmutableRepository<EntityType extends AbstractEntity<IdType>, IdType>
        extends  BaseJpaRepository<EntityType, IdType>
        implements ImmutableRepository<EntityType, IdType> {

    public JpaImmutableRepository(EntityManager entityManager,  Class<EntityType> clazz) {
        super.entityManager = entityManager;
        super.clazz = clazz;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        CriteriaQuery<EntityType> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.select(criteria.from(clazz));
        List<EntityType> listEntity = entityManager.createQuery(criteria).getResultList();
        return Optional.of(listEntity);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
         return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntityType> criteria = cb.createQuery(clazz);
        Root<EntityType> entityTypeRoot = criteria.from(clazz);
        criteria.where(cb.equal(entityTypeRoot.get(fieldName), value));
        List<EntityType> listEntity = entityManager.createQuery(criteria).getResultList();
        return Optional.of(listEntity);
    }
}
