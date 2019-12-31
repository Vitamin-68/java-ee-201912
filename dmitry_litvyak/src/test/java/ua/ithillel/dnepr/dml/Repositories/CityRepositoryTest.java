package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class CityRepositoryTest {

    public static final String SRC_MAIN_RESOURCES_CITY_CSV = "./src/main/resources/city.csv";

    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository = new CityRepository(SRC_MAIN_RESOURCES_CITY_CSV);
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(cityRepository.findAll().get());
    }

    @Test
    void findById() {
        City test = cityRepository.findById(4400).get();
        log.debug(test.getName());
        Assertions.assertEquals(4400,test.getId());
    }

    @Test
    void findByField() {
        Optional<City> test = cityRepository.findByField("name","Апрелевка");
        Assertions.assertNotNull(test);
        Optional<City> test2 = cityRepository.findByField("name","some string name");
        Assertions.assertTrue(test2.isEmpty());
        Optional<City> test3 = cityRepository.findByField("1name1",99999);
        Assertions.assertTrue(test3.isEmpty());
    }

    @Test
    void create() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_CITY_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_CITY_CSV),Paths.get(newFile));
        CityRepository updateRepo = new CityRepository(newFile);
        City test = new City();
        test.setName("Unknown");
        test.setCountry_id(12349999);
        test.setId(999999);
        updateRepo.update(test);
        Optional<City> testUpd = updateRepo.findById(12349999);
        Assertions.assertNotNull(testUpd);
        Files.delete(Paths.get(newFile));
    }

    @Test
    void update() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_CITY_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_CITY_CSV),Paths.get(newFile));
        CityRepository updateRepo = new CityRepository(newFile);
        City test = updateRepo.findById(4400).get();
        String testName = "+++NaN+++";
        test.setName(testName);
        updateRepo.update(test);
        Assertions.assertEquals(testName,updateRepo.findById(4400).get().getName());
        Files.delete(Paths.get(newFile));
    }

    @Test
    void delete() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_CITY_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_CITY_CSV),Paths.get(newFile));
        CityRepository updateRepo = new CityRepository(newFile);
        City test = updateRepo.findById(4400).get();
        updateRepo.delete(4400);
        Assertions.assertTrue(updateRepo.findById(4400).isEmpty());
        Files.delete(Paths.get(newFile));
    }
}