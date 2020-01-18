package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yevhen.lepet.entity.City;
import ua.ithillel.dnepr.yevhen.lepet.entity.Country;

import java.util.List;
import java.util.Optional;


@Slf4j
public class CityCrudRepoTest {
    private CityCrudRepo cityCrudRepo;
    private static final String PATH_CITY_CSV = "./src/main/resources/city.csv";
    private static final char DELIMITER = ';';

    @BeforeEach
    void setUp() {
        cityCrudRepo = new CityCrudRepo(PATH_CITY_CSV, DELIMITER);
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(cityCrudRepo.findAll().get());
    }

    @Test
    void findById() {
        Optional<City> testCity = cityCrudRepo.findById(4400);
        Assertions.assertEquals(4400, testCity.get().getId());
    }

    @Test
    void findByField(){
        Optional<List<City>> cities = cityCrudRepo.findByField("name", "Бородино");
        Assertions.assertTrue(cities.isPresent());
    }

    @Test
    void create(){
        City testCity = new City();
        testCity.setId(111212121);
        testCity.setCountry_id(1231233);
        testCity.setRegion_id(12332);
        testCity.setName("NewCity");
        cityCrudRepo.update(testCity);
        Optional <City> result = cityCrudRepo.findById(111212121);
        Assertions.assertNotNull(result);
    }
/*
    @Test
    void update(){
        City testCity = cityCrudRepo.findById(4313).get();
        testCity.setName("NewCity");
        cityCrudRepo.update(testCity);
        Assertions.assertEquals(testCity.getName(), cityCrudRepo.findById(4313).get().getName());
    }

    @Test
    void delete(){
        cityCrudRepo.delete(4313);
        Assertions.assertTrue(cityCrudRepo.findById(4313).isEmpty());
    }

 */
}
