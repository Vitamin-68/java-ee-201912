package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository;
import org.springframework.data.repository.CrudRepository;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.City;
import java.util.List;
import java.util.Optional;
public interface CityCrudRepository extends CrudRepository<City, Integer> {
    Optional<List<City>> findByName(String name);
    Optional<City> findById(Integer id);
    Optional<List<City>> findByCountryId(Integer countryId);
    Optional<List<City>> findByRegionId(Integer regionId);
}
