package repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Repository
public class CityRepositoryImpl implements CrudRepository<City, Integer> {
    private CrudRepository<City, Integer> crudRepository;

    public CityRepositoryImpl() {
        File dataFile = Utils.createTempFile(getClass(),"city.csv");
        if (dataFile != null) {
            crudRepository = new CrudRepositoryImp<>(dataFile.getPath(),City.class);
        }
    }

    @Cacheable(cacheNames = "cities")
    @Override
    public Optional<List<City>> findAll() {
        return crudRepository.findAll();
    }

    @Override
    public Optional<City> findById(Integer id) {
        return crudRepository.findById(id);
    }

    @Override
    public Optional<List<City>> findByField(String s, Object o) {
        return Optional.empty();
    }

    @Override
    public City create(City city) {
        return crudRepository.create(city);
    }

    @Override
    public City update(City city) {
        return crudRepository.update(city);
    }

    @Override
    public City delete(Integer id) {
        return crudRepository.delete(id);
    }
}
