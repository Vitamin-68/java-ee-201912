package vitaly.mosin.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import vitaly.mosin.spring.data.jpa.entity.RegionJdata;

public interface RegionSpringDataRepository extends CrudRepository<RegionJdata, Integer> {
}
