package ua.ithillel.alex.tsiba.repository.repositories.jpa;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ImmutableRepository<EntityType extends AbstractEntity<Integer>> extends AbstractRepository<EntityType>
        implements ua.ithillel.dnepr.common.repository.ImmutableRepository<EntityType, Integer> {

    public ImmutableRepository(Class objectClass) {
        super(objectClass);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntityType> criteriaQuery = criteriaBuilder.createQuery(objectClass);

        Root<EntityType> entityTypeRoot = criteriaQuery.from(objectClass);
        criteriaQuery.select(entityTypeRoot);

        List<EntityType> list = entityManager.createQuery(criteriaQuery).getResultList();
        if (!list.isEmpty()) {
            result = Optional.of(list);
        }

        return result;
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
        Optional<EntityType> result = Optional.empty();

        EntityType item = entityManager.find(objectClass, id);
        if (item != null) {
            result = Optional.of(item);
        }

        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        Optional<List<EntityType>> result = Optional.empty();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntityType> criteriaQuery = criteriaBuilder.createQuery(objectClass);

        Root<EntityType> entityTypeRoot = criteriaQuery.from(objectClass);
        criteriaQuery.select(entityTypeRoot);
        criteriaQuery.where(criteriaBuilder.equal(entityTypeRoot.get(fieldName), value));

        List<EntityType> list = entityManager.createQuery(criteriaQuery).getResultList();
        if (!list.isEmpty()) {
            result = Optional.of(list);
        }

        return result;
    }
}
