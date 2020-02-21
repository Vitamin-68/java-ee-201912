package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.rules.TemporaryFolder;
import ua.ithillel.dnepr.tymoshenko.olga.entity.City;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMutableRepositoryImpTest {
    private JdbcMutableRepositoryImp repository;
    private Connection connection;
    private City city;
    private BaseTest test;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("."));

    @BeforeEach
    void setUp() throws SQLException, IOException {
        test = new BaseTest();
        connection = test.initTest(folder);
        city = new City();
        city.setId(10);
        city.setCityid(1);
        city.setCountryId(15);
        city.setRegionId(20);
        city.setName("Zagopinsk");
        repository = new JdbcMutableRepositoryImp(connection, city.getClass());
    }

    @AfterEach
    void tearDown() throws SQLException {
        test.endTest();
    }

    @Test
    void create() {
        City actual = createCity();
        assertEquals(city, actual);
    }

    @Test
    void createExistEntity() {
        createCity();
        try {
            repository.create(city);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void createNullArgument() {
        try {
            repository.create(null);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void update() {
        City actual = createCity();
        assertEquals(city, actual);
        city.setCountryId(99999);
        city.setRegionId(11111);
        City act = (City) repository.update(city);
        assertEquals(city, act);
    }


    @Test
    void updateNotExistEntity() {

        City act = (City) repository.update(city);
        assertEquals(city, act);
    }

    @Test
    void updateNullArgument() {
        try {
            repository.update(null);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void getEntityById() {
        createCity();
        Integer id = city.getId();
        Optional<City> result = repository.getEntityById(id);
        assertFalse(result.isEmpty());
    }

    @Test
    void getEntityByFailedId() {
        createCity();
        Integer id = city.getId() + 10;
        Optional<City> result = repository.getEntityById(id);
        assertTrue(result.isEmpty());
    }

    @Test
    void delete() {
        createCity();
        Integer id = city.getId();
        City result = (City) repository.delete(id);
        assertEquals(id, result.getId());
    }

    @Test
    void deleteNotFindCity() {
        createCity();
        Integer id = city.getId() + 5;
        try {
            repository.delete(id);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    private City createCity() {
        return (City) repository.create(city);
    }
}