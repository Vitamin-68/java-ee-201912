package ua.ithillel.dnepr.dml.spring.datajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.ithillel.dnepr.dml.domain.jpa.City;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitySpringJpaRepository extends CrudRepository<City, Integer> {

    public Optional<List<City>> findCityByNameContains(String paretName);
}
