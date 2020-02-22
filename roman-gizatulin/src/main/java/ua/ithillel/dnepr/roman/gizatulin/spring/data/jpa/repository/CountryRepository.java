package ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    Optional<List<Country>> findByNameEndingWithAndIdLessThan(String name, Integer id);
}
