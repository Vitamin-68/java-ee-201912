package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.SpringCity;

import java.util.List;
import java.util.Optional;

public interface CityCrudRepository extends CrudRepository<SpringCity, Integer>{
    Optional<List<SpringCity>> findByName(Object name);
    Optional<List<SpringCity>> findByCountryId(Object id);
    Optional<List<SpringCity>> findByRegionId(Object id);
}
