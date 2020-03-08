package vitaly.mosin.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;

public interface CitySpringDataRepository extends JpaRepository<CityJdata, Integer> {
}
