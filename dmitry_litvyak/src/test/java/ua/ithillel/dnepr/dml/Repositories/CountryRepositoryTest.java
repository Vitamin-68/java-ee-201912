package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.Country;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Slf4j
class CountryRepositoryTest {

    public static final String SRC_MAIN_RESOURCES_COUNTRY_CSV = "./src/main/resources/country.csv";

    private CountryRepository countryRepository, updateRepo;
    private String newFile;

    @BeforeEach
    void setUp() throws IOException {
        countryRepository = new CountryRepository(SRC_MAIN_RESOURCES_COUNTRY_CSV);
        newFile = System.getProperty("java.io.tmpdir") + Math.random() + "country.out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_COUNTRY_CSV), Paths.get(newFile));
        updateRepo = new CountryRepository(newFile);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.delete(Paths.get(newFile));
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(countryRepository.findAll().get());
    }

    @Test
    void findById() {
        Country test = countryRepository.findById(7716093).get();
        log.debug(test.getName());
        Assertions.assertEquals(7716093, test.getId());
    }

    @Test
    void findByField() {
        Optional<List<Country>> test = countryRepository.findByField("name", "Белиз");
        Assertions.assertNotNull(test);
        Optional<List<Country>> test2 = countryRepository.findByField("name", "some string name");
        Assertions.assertTrue(test2.get().isEmpty());
        Optional<List<Country>> test3 = countryRepository.findByField("1name1", 99999);
        Assertions.assertTrue(test3.isEmpty());
    }

    @Test
    void create() throws IOException {
        Country test = new Country();
        test.setName("Unknown");
        test.setId(999999);
        test.setCity_id(0);
        updateRepo.update(test);
        Optional<Country> testUpd = updateRepo.findById(12349999);
        Assertions.assertNotNull(testUpd);
    }

    @Test
    void update() throws IOException {
        Country test = updateRepo.findById(1258).get();
        String testName = "+++NaN+++";
        test.setName(testName);
        updateRepo.update(test);
        Assertions.assertEquals(testName, updateRepo.findById(1258).get().getName());
    }

    @Test
    void delete() throws IOException {
        updateRepo.delete(1258);
        Assertions.assertTrue(updateRepo.findById(1258).isEmpty());
    }
}