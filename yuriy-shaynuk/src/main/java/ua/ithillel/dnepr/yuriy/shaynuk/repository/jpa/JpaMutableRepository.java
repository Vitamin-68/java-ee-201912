package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class JpaMutableRepository<EntityType extends AbstractEntity<IdType>, IdType>
        extends  BaseJpaRepository<EntityType, IdType>
        implements MutableRepository<EntityType, IdType> {
    private EntityTransaction transaction;

    public JpaMutableRepository(EntityManager entityManager) {
        super.entityManager = entityManager;
        transaction = entityManager.getTransaction();
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
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
