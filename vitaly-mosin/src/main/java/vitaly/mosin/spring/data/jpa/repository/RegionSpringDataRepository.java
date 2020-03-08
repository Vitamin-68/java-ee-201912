package vitaly.mosin.spring.data.jpa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import vitaly.mosin.spring.data.jpa.entity.RegionJdata;

public interface RegionSpringDataRepository extends PagingAndSortingRepository<RegionJdata, Integer> {
}
