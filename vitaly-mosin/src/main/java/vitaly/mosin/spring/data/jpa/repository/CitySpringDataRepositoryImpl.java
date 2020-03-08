package vitaly.mosin.spring.data.jpa.repository;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import vitaly.mosin.repository.entity.City;

import java.util.List;
import java.util.Optional;

public class CitySpringDataRepositoryImpl implements CrudRepository<City, Integer> {
    @Override
    public Optional<List<City>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<City> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public City create(City entity) {
        return null;
    }

    @Override
    public City update(City entity) {
        return null;
    }

    @Override
    public City delete(Integer id) {
        return null;
    }
}
