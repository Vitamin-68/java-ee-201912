package ua.hillel.jpaRepo;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class JpaRepo<EntityType extends AbstractEntity<Integer>>
        implements CrudRepository<EntityType, Integer> {

    private Class<EntityType> clazz;
    private EntityManager manager;
    private EntityTransaction transaction;


    @Override
    public Optional<List<EntityType>> findAll() {
        List<EntityType> result = manager.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
        if (result.isEmpty()) {
            throw new NullPointerException();
        }
        log.info("count {}", result.size());
        return Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
        EntityType entity = manager.find(clazz, id);
        if (entity == null) {
            throw new NullPointerException("Object is empty");
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<EntityType> createria = cb.createQuery(clazz);
        Root<EntityType> root = createria.from(clazz);
        createria.where(
                cb.equal(root.get(fieldName), value)
        );
        List<EntityType> types = manager.createQuery(createria).getResultList();
        return Optional.of(types);
    }

    @Override
    public EntityType create(EntityType entity) {
        transaction.begin();
        manager.persist(entity);
        log.info("created {}", entity.getId());
        transaction.commit();
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        transaction.begin();
        manager.merge(entity);
        transaction.commit();
        log.info("updated {}", entity.getId());
        return entity;
    }

    @Override
    public EntityType delete(Integer id) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaDelete<EntityType> criteriaDelete = cb.createCriteriaDelete(clazz);
        Root<EntityType> root = criteriaDelete.from(clazz);
        criteriaDelete.where(cb.equal(root.get("id"), id));
        Optional<EntityType> entityType = findById(id);
        transaction.begin();
        manager.createQuery(criteriaDelete).executeUpdate();
        transaction.commit();
        log.info("deleted {}", id);
        return entityType.get();
    }
}