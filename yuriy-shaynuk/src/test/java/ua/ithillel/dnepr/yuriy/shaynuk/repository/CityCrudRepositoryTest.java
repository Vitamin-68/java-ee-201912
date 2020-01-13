package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class CityCrudRepositoryTest {
    static File dataFile =null;
    static private CrudRepository<City, Integer> cityRepository;

    @BeforeAll
    static void setUp() {
        dataFile = Utils.createTempFile("cityyy.csv");
        if (dataFile != null) {
            cityRepository = new CrudRepositoryImp<City, Integer>(dataFile.getPath(),City.class);
        }
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = cityRepository.findAll();
        assertFalse(cities.get().isEmpty());
    }

    @Test
    void findById() {
        Optional<City> test = cityRepository.findById(999);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<City>> test = cityRepository.findByField("name", "Москва");
        Assertions.assertFalse(test.get().isEmpty());
        Optional<List<City>> test2 = cityRepository.findByField("name", "some string name");
        Assertions.assertTrue(test2.get().isEmpty());
        Optional<List<City>> test3 = cityRepository.findByField("1name1", 99999);
        Assertions.assertTrue(test3.get().isEmpty());
    }

    @Test
    void create() {
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountry_id(111);
        testCity.setRegion_id(222);
        testCity.setId(999);
        cityRepository.create(testCity);
        Optional<City> test3 = cityRepository.findById(999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    void update() {
        City testCity = new City();
        testCity.setName("Москва");
        testCity.setCountry_id(31599);
        testCity.setRegion_id(4312);
        testCity.setId(999);

        City test = cityRepository.update(testCity);
        Assertions.assertNotNull(test);
    }

    @Test
    void delete() {
        City testCity = new City();
        testCity.setName("deleteCity");
        testCity.setCountry_id(11);
        testCity.setRegion_id(22);
        testCity.setId(333);
        cityRepository.create(testCity);

        City test = cityRepository.delete(333);
        Assertions.assertNotNull(test);
    }
}