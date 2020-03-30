package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.Region;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgDecoratorEmpty;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidator;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidatorDecorator;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidatorDecoratorBlank;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator.ArgValidatorDecoratorRegionField;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class RegionSpringRepositoryImpl implements ua.ithillel.dnepr.common.repository.CrudRepository<Region, Integer> {
    private final RegionCrudRepository regionCrudRepository;
    RegionSpringRepositoryImpl(RegionCrudRepository regionCrudRepository) {this.regionCrudRepository = regionCrudRepository;}

    @Override
    public Optional<List<Region>> findAll() {
        return Optional.of(StreamSupport.stream(regionCrudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Region> findById(Integer id) {
        return regionCrudRepository.findById(id);
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        validateField(fieldName);
        switch (fieldName) {
            case "name":
                return regionCrudRepository.findByName((String) value);
            case "countryId":
                return regionCrudRepository.findByCountryId((Integer) value);
        }
        return Optional.empty();
    }

    @Override
    public Region create(Region entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        Optional<Region> region = Optional.empty();
        Optional<List<Region>> regionList = regionCrudRepository.findByName(entity.getName());
        if (regionList.isPresent()) {
            region = getDetectedEntity(regionList.get(), entity);
        }
        if (region.isEmpty()) {
            regionCrudRepository.save(entity);
        } else {
            log.error("Entity  exist");
        }
        return entity;
    }

    @Override
    public Region update(Region entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        Optional<Region> region = Optional.empty();
        Optional<List<Region>> regionList = regionCrudRepository.findByName(entity.getName());
        if (regionList.isPresent()) {
            region = getDetectedEntity(regionList.get(), entity);
        }
        if (region.isPresent()) {
            entity.setId(region.get().getId());
            regionCrudRepository.save(entity);
        } else {
            regionCrudRepository.save(entity);
        }
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        Optional<Region> region = regionCrudRepository.findById(id);
        if (region.isPresent()) {
            regionCrudRepository.deleteById(id);
            return region.get();
        } else {
            log.error("Id is failure Entity is not exist");
        }
        return null;
    }

    private void validateField(String fieldName) {
        ArgValidatorDecorator argValidator =
                new ArgDecoratorEmpty(
                        new ArgValidatorDecoratorBlank(
                                new ArgValidatorDecoratorRegionField(new ArgValidator())
                        )
                );
        try {
            argValidator.validate(fieldName);
        } catch (Exception e) {
            log.error("Field is not validate" + e);
            throw new IllegalStateException("Field is not validate" + e);
        }
    }

    private Optional<Region> getDetectedEntity(List<Region> regionList, Region entity) {
        for (Region region : regionList) {
            if (region.equals(entity)) {
                return Optional.of(region);
            }
        }
        return Optional.empty();
    }
}
