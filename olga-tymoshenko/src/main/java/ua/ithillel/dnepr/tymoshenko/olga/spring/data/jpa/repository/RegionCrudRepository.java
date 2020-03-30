package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository;
import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.Region;
import java.util.List;
import java.util.Optional;

public interface RegionCrudRepository extends CrudRepository<Region, Integer> {
    Optional<List<Region>> findByName(String name);
    Optional<Region> findById(Integer id);
    Optional<List<Region>> findByCountryId(Integer countryId);
}
