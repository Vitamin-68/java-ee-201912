package ua.ithillel.alex.tsiba.repository.repositories.jpa;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public class MutableRepository<EntityType extends AbstractEntity<Integer>> extends AbstractRepository<EntityType>
        implements ua.ithillel.dnepr.common.repository.MutableRepository<EntityType, Integer> {

    public MutableRepository(Class<EntityType> objectClass) {
        super(objectClass);
    }

    @Override
    public EntityType create(EntityType entity) {
        if(entity.getId() != null){
            EntityType item = entityManager.find(objectClass, entity.getId());
            if(item != null){
                entity = item;
            }
        }
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public EntityType delete(Integer id) {
        EntityType entity = entityManager.find(objectClass, id);

        if(entity != null){
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }

        return entity;
    }
}
