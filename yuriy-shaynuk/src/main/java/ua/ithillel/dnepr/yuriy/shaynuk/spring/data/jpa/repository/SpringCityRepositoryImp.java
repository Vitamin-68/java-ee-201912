package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.City;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Configuration
public class SpringCityRepositoryImp implements CrudRepository<City, Integer> {
    private CityCrudRepository crudRepository;

    public SpringCityRepositoryImp(CityCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<List<City>> findAll() {
        return Optional.of(StreamSupport.stream(crudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<City> findById(Integer id) {
        return crudRepository.findById(id);
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public City create(City entity) {
        return crudRepository.save(entity);
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
