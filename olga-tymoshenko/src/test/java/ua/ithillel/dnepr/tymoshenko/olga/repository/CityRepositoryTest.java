package ua.ithillel.dnepr.tymoshenko.olga.repository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import ua.ithillel.dnepr.tymoshenko.olga.entity.City;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CityRepositoryTest {

    private CityRepository repository;
    private final Character DELIMITER = ';';

    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("."));

    @BeforeEach
    void setUp() throws IOException {
        final String FILE_NAME = "city.csv";
        final String RESOURSE_ROOT_PATH = "../common/src/main/resources/";
        Path sourse = Path.of(RESOURSE_ROOT_PATH, FILE_NAME);
        File tmpFile;
        folder.create();
        tmpFile = folder.newFile(FILE_NAME);
        Files.copy(sourse, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        repository = new CityRepository(tmpFile, DELIMITER);
    }

    @AfterEach
    void tearDown() {
        folder.delete();
    }

    @Test
    void RepositoryFileIsEmpty() {
        File fileName = new File("");
        try {
            CityRepository testRepo = new CityRepository(fileName, DELIMITER);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findAll() {
        Optional<List<City>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Integer id = 55;
        Optional<City> city = repository.findById(id);
        assertFalse(city.isEmpty());
    }

    @Test
    void findByFailId() {
        Integer id = 55000;
        Optional<City> city = repository.findById(id);
        assertTrue(city.isEmpty());
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Москва";
        Optional<List<City>> city = repository.findByField(field, value);
        assertFalse(city.isEmpty());
    }

    @Test
    void findByEmptyField() {
        String field = "";
        Integer value = 4312;
        try {
            repository.findByField(field, value);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByNullField() {
        String field = null;
        Integer value = 4312;
        try {
            repository.findByField(field, value);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByIllegalField() {
        String field = "regionid";
        Integer value = 4312;
        Optional<List<City>> city = repository.findByField(field, value);
        assertTrue(city.isEmpty());

    }

    @Test
    void findByIllegalValue() {
        String field = "city_id";
        Integer value = -1;
        Optional<List<City>> cities = repository.findByField(field, value);
        assertTrue(cities.isEmpty());
    }

    @Test
    void create() {
        City city = new City();
        city.setId(1);
        city.setRegionId(2);
        city.setCountryId(3);
        city.setName("city");
        City actual = repository.create(city);
        assertEquals(city, actual);
    }

    @Test
    void updateCityFind() {
        City city = new City();
        city.setId(4400);
        city.setRegionId(3159);
        city.setCountryId(4312);
        city.setName("Moskva");

        City actual = repository.update(city);
        assertEquals(city, actual);
    }

    @Test
    void updateCityNotFind() {
        City city = new City();
        city.setId(-20);
        city.setRegionId(3159);
        city.setCountryId(4312);
        city.setName("Mockva");

        City actual = repository.update(city);
        assertEquals(city, actual);
    }

    @Test
    void deleteCityFind() {
        Integer id = 4315;
        City actual = repository.delete(id);
        assertEquals(id, actual.getId());
    }

    @Test
    void deleteNotCityFind() {
        Integer id = -20;
        City actual = repository.delete(id);
        assertNull(actual);
    }
}