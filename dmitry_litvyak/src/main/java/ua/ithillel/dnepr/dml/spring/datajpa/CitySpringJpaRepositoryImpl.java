package ua.ithillel.dnepr.dml.spring.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.dml.domain.jpa.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class CitySpringJpaRepositoryImpl implements ua.ithillel.dnepr.common.repository.CrudRepository<City, Integer> {

    CrudRepository<City, Integer> crudRepository;
    EntityManagerFactory entityManagerFactory;

    @Autowired
    public CitySpringJpaRepositoryImpl(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<List<City>> findAll() {
        return Optional.of((List<City>) crudRepository.findAll());
    }

    @Override
    public Optional<City> findById(Integer id) {
        return crudRepository.findById(id);
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<City> query = cb.createQuery(City.class);
        Root<City> city = query.from(City.class);
        Path<String> fPath = city.get(fieldName);
        query.select(city).where(cb.equal(fPath,value));
        return Optional.of(entityManager.createQuery(query).getResultList());
    }

    @Override
    public City create(City entity) {
        return crudRepository.save(entity);
    }

    @Override
    public City update(City entity) {
        return crudRepository.save(entity);
    }

    @Override
    public City delete(Integer id) {
        Optional<City> resultCity = crudRepository.findById(id);
        City result = null;
        if (resultCity.isPresent()) {
            crudRepository.deleteById(id);
            result = resultCity.get();
        }
        return result;
    }
}
