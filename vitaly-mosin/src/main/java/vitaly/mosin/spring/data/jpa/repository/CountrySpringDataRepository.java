package vitaly.mosin.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import vitaly.mosin.spring.data.jpa.entity.CountryJdata;

import java.util.List;
import java.util.Optional;

public interface CountrySpringDataRepository extends CrudRepository<CountryJdata, Integer> {
    Optional<List<CountryJdata>> findByNameEndingWithAndIdLessThan(String name, Integer id);
}
