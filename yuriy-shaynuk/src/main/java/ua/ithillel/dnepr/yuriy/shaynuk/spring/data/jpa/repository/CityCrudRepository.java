package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityCrudRepository extends CrudRepository<City, Integer>{
    Optional<List<City>> findByName(Object name);
    Optional<List<City>> findByCountryId(Object id);
    Optional<List<City>> findByRegionId(Object id);
}
