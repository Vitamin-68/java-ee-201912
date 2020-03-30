package vitaly.mosin.spring.data.jpa.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.repository.exceptions.ExceptionResponseCode;
import vitaly.mosin.repository.exceptions.MyRepoException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Slf4j
@Setter
@Getter
@Component
public class SpringDataRepository<EntityType extends AbstractEntity<IdType>, IdType>
        implements ua.ithillel.dnepr.common.repository.CrudRepository<EntityType, IdType> {

    private Class<EntityType> clazz;
    private CrudRepository crudRepository;
    EntityManagerFactory entityManagerFactory;

    @Autowired
    public SpringDataRepository(@Qualifier("citySpringDataRepository") CrudRepository<EntityType, IdType> springDataRepository) {
        this.crudRepository = springDataRepository;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.of((List<EntityType>) crudRepository.findAll());
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return (Optional<EntityType>) crudRepository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntityType> query = cb.createQuery(clazz);
        Root<? extends EntityType> entity = query.from(clazz);
        Path<String> path = entity.get(fieldName);
        query.select(entity).where(cb.equal(path, value));
        return Optional.of(entityManager.createQuery(query).getResultList());
    }

    @Override
    public EntityType create(EntityType entity) {
        crudRepository.save(entity);
        return entity;
    }

    @SneakyThrows
    @Override
    public EntityType update(EntityType entity) {
        if (findById(entity.getId()).isPresent()) {
            crudRepository.save(entity);
        } else {
            log.error("Update error! City with ID = {} not found.", entity.getId());
            throw new MyRepoException(ExceptionResponseCode.FAILED_UPDATE_CONTACT, "Update error, city not found.");
        }
        return entity;
    }

    @SneakyThrows
    @Override
    public EntityType delete(IdType id) {
        EntityType result;
        if (crudRepository.findById(id).isPresent()) {
            result = (EntityType) crudRepository.findById(id).get();
            crudRepository.delete(result);
        } else {
            log.error("Update error! City with ID = {} not found.", id);
            throw new MyRepoException(ExceptionResponseCode.FAILED_DELETE_CONTACT_FROM_DB, "Delete error, city not found.");
        }
        return result;
    }
}
