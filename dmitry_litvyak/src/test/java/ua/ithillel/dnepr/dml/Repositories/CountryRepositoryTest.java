package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.Country;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
class CountryRepositoryTest {

    public static final String SRC_MAIN_RESOURCES_COUNTRY_CSV = "./src/main/resources/country.csv";

    private CountryRepository countryRepository;
    @BeforeEach
    void setUp() {

        countryRepository = new CountryRepository(SRC_MAIN_RESOURCES_COUNTRY_CSV);

    }

    @Test
    void findAll() {
        Assertions.assertNotNull(countryRepository.findAll().get());
    }

    @Test
    void findById() {
        Country test = countryRepository.findById(7716093).get();
        log.debug(test.getName());
        Assertions.assertEquals(7716093,test.getId());
    }

    @Test
    void findByField() {
        Optional<Country> test = countryRepository.findByField("name","Белиз");
        Assertions.assertNotNull(test);
        Optional<Country> test2 = countryRepository.findByField("name","some string name");
        Assertions.assertTrue(test2.isEmpty());
        Optional<Country> test3 = countryRepository.findByField("1name1",99999);
        Assertions.assertTrue(test3.isEmpty());
    }

    @Test
    void create() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_COUNTRY_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_COUNTRY_CSV),Paths.get(newFile));
        CountryRepository updateRepo = new CountryRepository(newFile);
        Country test = new Country();
        test.setName("Unknown");
        test.setId(999999);
        test.setCity_id(0);
        updateRepo.update(test);
        Optional<Country> testUpd = updateRepo.findById(12349999);
        Assertions.assertNotNull(testUpd);
        Files.delete(Paths.get(newFile));
    }

    @Test
    void update() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_COUNTRY_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_COUNTRY_CSV),Paths.get(newFile));
        CountryRepository updateRepo = new CountryRepository(newFile);
        Country test = updateRepo.findById(1258).get();
        String testName = "+++NaN+++";
        test.setName(testName);
        updateRepo.update(test);
        Assertions.assertEquals(testName,updateRepo.findById(1258).get().getName());
        Files.delete(Paths.get(newFile));
    }

    @Test
    void delete() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_COUNTRY_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_COUNTRY_CSV),Paths.get(newFile));
        CountryRepository updateRepo = new CountryRepository(newFile);
        updateRepo.delete(1258);
        Assertions.assertTrue(updateRepo.findById(1258).isEmpty());
        Files.delete(Paths.get(newFile));
    }
}