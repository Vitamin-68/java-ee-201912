package ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.City;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.Country;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.Region;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository.CityRepository;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository.CountryRepository;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository.RegionRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class AppEngine {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    public AppEngine(
            CountryRepository countryRepository,
            CityRepository cityRepository,
            RegionRepository regionRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }

    public void start(String... args) {
        log.info("Application is starting with arguments: {}", Arrays.toString(args));

        createEntities();
        printEntities();
    }

    @Transactional
    void createEntities() {
        countryRepository.saveAll(createCountries());
        cityRepository.saveAll(createCities());
        regionRepository.saveAll(createRegions());
    }

    @Transactional
    void printEntities() {
        log.info("================COUNTRIES================");
        countryRepository.findAll().forEach(country -> log.info(country.toString()));
        log.info("================CITIES================");
        cityRepository.findAll().forEach(city -> log.info(city.toString()));
        log.info("================REGIONS================");
        regionRepository.findAll().forEach(region -> log.info(region.toString()));
        regionRepository
                .findAll(PageRequest.of(1, 3, Sort.by("name")))
                .forEach(region -> log.info(region.toString()));
    }

    private List<Country> createCountries() {
        final List<Country> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Country country = new Country();
            country.setName("country_" + i);
            country.setCityId(new Random().nextInt());
            result.add(country);
        }
        return result;
    }

    private List<City> createCities() {
        final List<City> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            City city = new City();
            city.setName("city_" + i);
            city.setCountryId(new Random().nextInt());
            city.setRegionId(new Random().nextInt());
            result.add(city);
        }
        return result;
    }

    private List<Region> createRegions() {
        final List<Region> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Region region = new Region();
            region.setName("region_" + i);
            region.setCountryId(new Random().nextInt());
            region.setCityId(new Random().nextInt());
            result.add(region);
        }
        return result;
    }
}
