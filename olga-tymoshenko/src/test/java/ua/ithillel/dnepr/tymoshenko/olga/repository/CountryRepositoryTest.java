package ua.ithillel.dnepr.tymoshenko.olga.repository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import ua.ithillel.dnepr.tymoshenko.olga.entity.Country;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CountryRepositoryTest {
    private CountryRepository repository;
    private final Character DELIMITER = ';';

    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("."));

    @BeforeEach
    void setUp() throws IOException {
        String fileName = "country.csv";
        final String RESOURSE_ROOT_PATH = "../common/src/main/resources/";
        Path sourse = Path.of(RESOURSE_ROOT_PATH, fileName);
        File tmpFile;
        folder.create();
        tmpFile = folder.newFile(fileName);
        Files.copy(sourse, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        repository = new CountryRepository(tmpFile, DELIMITER);
    }

    @AfterEach
    void tearDown() {
        folder.delete();
    }

    @Test
    void RepositoryFileIsEmpty() {
        File fileName = new File("");
        try {
            CountryRepository testRepo = new CountryRepository(fileName, DELIMITER);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findAll() {
        Optional<List<Country>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Integer id = 81;
        Optional<Country> region = repository.findById(id);
        assertFalse(region.isEmpty());
    }

    @Test
    void findByFailId() {
        Integer id = 55000;
        Optional<Country> country = repository.findById(id);
        assertTrue(country.isEmpty());
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Россия";
        Optional<List<Country>> country = repository.findByField(field, value);
        assertFalse(country.isEmpty());
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
        Optional<List<Country>> countries = repository.findByField(field, value);
        assertTrue(countries.isEmpty());
    }

    @Test
    void findByIllegalValue() {
        String field = "country_id";
        Integer value = -1;
        Optional<List<Country>> countries = repository.findByField(field, value);
        assertTrue(countries.isEmpty());
    }

    @Test
    void create() {
        Country country = new Country();
        country.setCityId(1);
        country.setId(2);
        country.setName("Country");
        Country actual = repository.create(country);
        assertEquals(country, actual);
    }

    @Test
    void updateCountryFind() {
        Country country = new Country();
        country.setCityId(0);
        country.setId(173);
        country.setName("Country");
        Country actual = repository.update(country);
        assertEquals(country, actual);
    }

    @Test
    void updateCountryNotFind() {
        Country country = new Country();
        country.setCityId(0);
        country.setId(-20);
        country.setName("Country");
        Country actual = repository.update(country);
        assertEquals(country, actual);
    }

    @Test
    void deleteRegionFind() {
        Integer id = 173;
        Country actual = repository.delete(id);
        assertEquals(id, actual.getId());
    }

    @Test
    void deleteNotCityFind() {
        Integer id = -20;
        Country actual = repository.delete(id);
        assertNull(actual);
    }
}