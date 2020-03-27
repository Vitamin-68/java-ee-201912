package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.AppConfig;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.City;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CitySpringRepositoryImplTest {
    private EntityManagerFactory managerFactory;
    private CitySpringRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);
        managerFactory = ctx.getBean(EntityManagerFactory.class);
        repository = ctx.getBean(CitySpringRepositoryImpl.class);
    }

    @AfterEach
    void tearDown() {
        managerFactory.close();
    }

    @Test
    void findAll() {
        Optional<List<City>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Integer id = 10;
        Optional<City> actual = repository.findById(id);
        assertTrue(actual.isPresent());

    }

    @Test
    void findByFieldCountryID() {
        String fieldName = "countryId";
        Integer value = 4;
        Optional<List<City>> actual = repository.findByField(fieldName, value);
        assertNotNull(actual);
    }

    @Test
    void findByFieldRegionID() {
        String fieldName = "regionId";
        Integer value = 5;
        Optional<List<City>> actual = repository.findByField(fieldName, value);
        assertNotNull(actual);
    }

    @Test
    void findByFieldName() {
        String fieldName = "name";
        String value = "Бендиго";
        Optional<List<City>> actual = repository.findByField(fieldName, value);
        assertNotNull(actual);
    }

    @Test
    void findByWrongFieldValue() {
        String fieldName = "regionId";
        Integer value = 11111;
        Optional<List<City>> actual = repository.findByField(fieldName, value);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByWrongField() {
        String fieldName = "regionID";
        Integer value = 5;
        try {
            repository.findByField(fieldName, value);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByEmptyField() {
        String fieldName = "";
        Integer value = 5;
        try {
            repository.findByField(fieldName, value);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByBlankField() {
        String fieldName = " ";
        Integer value = 5;
        try {
            repository.findByField(fieldName, value);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByNullField() {
        String fieldName = null;
        Integer value = 5;
        try {
            repository.findByField(fieldName, value);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void createNotDetectedEntity() {
        City city = new City();
        city.setCountryId(11111);
        city.setRegionId(11111);
        city.setName("City");
        City actual = repository.create(city);
        assertEquals(city, actual);
        actual = repository.delete(actual.getId());
    }

    @Test
    void createDetectedEntity() {
        City city = new City();
        city.setId(6);
        city.setCountryId(4);
        city.setRegionId(5);
        city.setName("Балларат");
        City actual = repository.create(city);
        assertEquals(city, actual);
    }

    @Test
    void updateNotDetectedEntity() {
        City city = new City();
        city.setCountryId(11111);
        city.setRegionId(11111);
        city.setName("City");
        City actual = repository.update(city);
        assertEquals(city, actual);
        repository.delete(actual.getId());

    }

    @Test
    void updateDetectedEntity() {
        City city = new City();
        city.setCountryId(11111);
        city.setRegionId(11111);
        city.setName("City");
        City actual = repository.create(city);
        actual.setCountryId(11000);
        actual.setRegionId(11000);
        actual.setName("Actual");
        City test = repository.update(actual);
        assertEquals(test, actual);
        repository.delete(actual.getId());
    }

    @Test
    void deleteDetectedEntity() {
        City city = new City();
        city.setCountryId(11111);
        city.setRegionId(11111);
        city.setName("City");
        City test = repository.create(city);
        Integer id = test.getId();
        City actual = repository.delete(id);
        assertEquals(actual, test);
    }

    @Test
    void deleteNotDetectedEntity() {
        Integer id = -1;
        City actual = repository.delete(id);
        assertNull(actual);
    }
}