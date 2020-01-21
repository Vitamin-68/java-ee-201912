package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Slf4j
class CityRepositoryTest {

    public static final String SRC_MAIN_RESOURCES_CITY_CSV = "./src/main/resources/city.csv";
    private CityRepository cityRepository, updateRepo;
    private String newFile;

    @BeforeEach
    void setUp() throws IOException {

        cityRepository = new CityRepository(SRC_MAIN_RESOURCES_CITY_CSV);
        newFile = System.getProperty("java.io.tmpdir") + Math.random() + "city.out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_CITY_CSV), Paths.get(newFile));
        updateRepo = new CityRepository(newFile);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.delete(Paths.get(newFile));
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(cityRepository.findAll().get());
    }

    @Test
    void findById() {
        City test = cityRepository.findById(4400).get();
        log.debug(test.getName());
        Assertions.assertEquals(4400, test.getId());
    }

    @Test
    void findByField() {
        Optional<List<City>> test = cityRepository.findByField("name", "Апрелевка");
        Assertions.assertNotNull(test);
        Optional<List<City>> test2 = cityRepository.findByField("name", "some string name");
        Assertions.assertTrue(test2.get().isEmpty());
        Optional<List<City>> test3 = cityRepository.findByField("1name1", 99999);
        Assertions.assertTrue(test3.isEmpty());
    }

    @Test
    void create() throws IOException {
        City test = new City();
        test.setName("Unknown");
        test.setCountry_id(12349999);
        test.setId(999999);
        updateRepo.update(test);
        Optional<City> testUpd = updateRepo.findById(12349999);
        Assertions.assertNotNull(testUpd);
    }

    @Test
    void update() throws IOException {
        City test = updateRepo.findById(4400).get();
        String testName = "+++NaN+++";
        test.setName(testName);
        updateRepo.update(test);
        Assertions.assertEquals(testName, updateRepo.findById(4400).get().getName());
    }

    @Test
    void delete() throws IOException {
        City test = updateRepo.findById(4400).get();
        updateRepo.delete(4400);
        Assertions.assertTrue(updateRepo.findById(4400).isEmpty());
    }
}