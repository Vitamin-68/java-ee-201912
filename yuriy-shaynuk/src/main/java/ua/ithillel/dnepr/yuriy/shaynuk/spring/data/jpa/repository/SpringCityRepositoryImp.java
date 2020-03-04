package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.City;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SpringCityRepositoryImp extends BaseSpringRepositoryImp<City,Integer> {
    private CityCrudRepository cityCrudRepository;

    public SpringCityRepositoryImp(CityCrudRepository cityCrudRepository) {
        this.cityCrudRepository = cityCrudRepository;
        super.crudRepository = cityCrudRepository;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> cityList = Optional.empty();

        switch (fieldName){
            case "name": cityList = cityCrudRepository.findByName(value); break;
            case "countryId": cityList = cityCrudRepository.findByCountryId(value); break;
            case "regionId": cityList = cityCrudRepository.findByRegionId(value); break;
        }

        return cityList;
    }
}
