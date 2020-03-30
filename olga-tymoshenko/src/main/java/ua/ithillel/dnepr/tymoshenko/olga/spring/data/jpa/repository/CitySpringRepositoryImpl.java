package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.City;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidatorDecoratorBlank;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidator;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidatorDecorator;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgDecoratorEmpty;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidatorDecoratorCityField;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class CitySpringRepositoryImpl implements ua.ithillel.dnepr.common.repository.CrudRepository<City, Integer> {
    private final CityCrudRepository cityCrudRepository;
    CitySpringRepositoryImpl(CityCrudRepository cityCrudRepository) {
        this.cityCrudRepository = cityCrudRepository;
    }

    @Override
    public Optional<List<City>> findAll() {
        return Optional.of(StreamSupport.stream(cityCrudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<City> findById(Integer id) {
        return cityCrudRepository.findById(id);
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        validateField(fieldName);
        switch (fieldName) {
            case "name":
                return cityCrudRepository.findByName((String) value);
            case "countryId":
                return cityCrudRepository.findByCountryId((Integer) value);
            case "regionId":
                return cityCrudRepository.findByRegionId((Integer) value);
        }
        return Optional.empty();
    }

    @Override
    public City create(City entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        Optional<City> city = Optional.empty();
        Optional<List<City>> cityList = cityCrudRepository.findByName(entity.getName());
        if (cityList.isPresent()) {
            city = getDetectedEntity(cityList.get(), entity);
        }
        if (city.isEmpty()) {
            cityCrudRepository.save(entity);
        } else {
            log.error("Entity  exist");
        }
        return entity;
    }

    @Override
    public City delete(Integer id) {
        Optional<City> city = cityCrudRepository.findById(id);
        if (city.isPresent()) {
            cityCrudRepository.deleteById(id);
            return city.get();
        } else {
            log.error("Id is failure Entity is not exist");
        }
        return null;
    }

    @Override
    public City update(City entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        Optional<City> city = Optional.empty();
        Optional<List<City>> cityList = cityCrudRepository.findByName(entity.getName());
        if (cityList.isPresent()) {
            city = getDetectedEntity(cityList.get(), entity);
        }
        if (city.isPresent()) {
            entity.setId(city.get().getId());
            cityCrudRepository.save(entity);
        } else {
            cityCrudRepository.save(entity);
        }
        return entity;
    }

    private void validateField(String fieldName) {
        ArgValidatorDecorator argValidator =
                new ArgDecoratorEmpty(
                        new ArgValidatorDecoratorBlank(
                                new ArgValidatorDecoratorCityField(new ArgValidator())
                        )
                );
        try {
            argValidator.validate(fieldName);
        } catch (Exception e) {
            log.error("Field is not validate" + e);
            throw new IllegalStateException("Field is not validate" + e);
        }
    }

    private Optional<City> getDetectedEntity(List<City> cityList, City entity) {

        for (City city : cityList) {
            if (city.equals(entity)) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }
}