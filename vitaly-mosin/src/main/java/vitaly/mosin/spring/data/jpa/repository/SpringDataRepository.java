package vitaly.mosin.spring.data.jpa.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.jpa.entity.CityJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Component
public class SpringDataRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements ua.ithillel.dnepr.common.repository.CrudRepository<EntityType, IdType> {

    private Class<? extends EntityType> clazz;
    private CrudRepository crudRepository;
//    CrudRepository<EntityType, IdType> crudRepository2;
    EntityManagerFactory entityManagerFactory;

    @Autowired
    public SpringDataRepository(CitySpringDataRepository citySpringDataRepository,
                                CountrySpringDataRepository countrySpringDataRepository,
                                RegionSpringDataRepository regionSpringDataRepository) {
        this.crudRepository = citySpringDataRepository;
    }
//    @Autowired
//    public SpringDataRepository(CrudRepository<EntityType, IdType> repo) {
//        this.crudRepository2 = repo;
//    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.of((List<EntityType>) crudRepository.findAll());
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return (Optional<EntityType>) crudRepository.findById(id);
    }

    @Transactional
    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<? extends EntityType> query = cb.createQuery(clazz);
        Root<? extends EntityType> entity = query.from(clazz);
        Path<String> path = entity.get(fieldName);
        query.select(entity).where(cb.equal(path, value));
        return Optional.of(entityManager.createQuery(query).getResultList());

        // тоже не работает
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<?> query = cb.createQuery(clazz);
//        Root<?> entity = query.from(clazz);
//        Path<String> path = entity.get(fieldName);
//        query.select(entity).where(cb.equal(path, value));
//        return Optional.of(entityManager.createQuery(query).getResultList());

        // неудачная попытка создать запрос с помощью имени метода
//        return Optional.of((List<EntityType>) crudRepository.findByFieldWhereNameEqualsAndMatches(fieldName, value);
    }

    @Override
    public EntityType create(EntityType entity) {
        crudRepository.save(entity);
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        crudRepository.save(entity);
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        Object result = null;
        if (crudRepository.findById(id).isPresent()) {
            result = crudRepository.findById(id).get();
            crudRepository.delete(result);
        }
        return (EntityType) result;
    }
}
