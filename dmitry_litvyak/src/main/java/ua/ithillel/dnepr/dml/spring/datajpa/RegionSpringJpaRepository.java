package ua.ithillel.dnepr.dml.spring.datajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.ithillel.dnepr.dml.domain.jpa.Region;

@Repository
public interface RegionSpringJpaRepository extends CrudRepository<Region,Integer> {
}
