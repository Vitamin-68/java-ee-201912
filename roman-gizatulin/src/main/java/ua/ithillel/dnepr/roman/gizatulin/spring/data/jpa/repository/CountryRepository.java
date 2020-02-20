package ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {
}
