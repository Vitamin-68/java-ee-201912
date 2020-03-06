package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.rules.TemporaryFolder;
import ua.ithillel.dnepr.tymoshenko.olga.entity.City;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;



@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcImmutableRepositoryImpTest {

    private JdbcImmutableRepositoryImp repository;

    private City city = new City();
    private BaseTest test;
    JdbcMutableRepositoryImp repo;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("."));

    @BeforeAll
    void generalSetUp() throws SQLException, IOException {
        test = new BaseTest();
        Connection connection = test.initTest(folder);
        city = new City();
        city.setId(10);
        city.setCityid(1);
        city.setCountryId(15);
        city.setRegionId(20);
        city.setName("Zagopinsk");
        repo = new JdbcMutableRepositoryImp(connection, city.getClass());
        repo.create(city);
        repository = new JdbcImmutableRepositoryImp(connection, city.getClass());
    }

    @AfterAll
    void generalClose() throws SQLException {
        test.endTest();
    }

    @Test
    void findAll() {
        Optional<List<City>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Optional<City> actual = repository.findById(city.getId());
        assertFalse(actual.isEmpty());
    }

    @Test
    void findByNotId() {
        Integer id = -20;
        Optional<City> actual = repository.findById(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByField() {
        String field = "regionid";
        Integer value = 20;
        Optional<List<City>> list = repository.findByField(field, value);
        assertFalse(list.isEmpty());
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
        String field = "region_id";
        Integer value = 4312;
        try {
            repository.findByField(field, value);
            Assert.fail("Expected SQLException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByIllegalValue() {
        String field = "regionid";
        Integer value = 999;
        Optional<List<City>> actual = repository.findByField(field, value);
        assertTrue(actual.isEmpty());
    }

}