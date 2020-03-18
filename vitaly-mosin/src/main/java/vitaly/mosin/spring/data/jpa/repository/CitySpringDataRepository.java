package vitaly.mosin.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;

import java.util.List;
import java.util.Optional;

public interface CitySpringDataRepository extends CrudRepository<CityJdata, Integer> {
    Optional<List<CityJdata>> findByNameAndNameEquals(String fieldName, String value);
}
