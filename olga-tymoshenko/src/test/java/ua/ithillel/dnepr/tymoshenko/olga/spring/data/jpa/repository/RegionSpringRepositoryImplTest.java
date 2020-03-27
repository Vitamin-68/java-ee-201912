package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.AppConfig;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.Region;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RegionSpringRepositoryImplTest {
    private EntityManagerFactory managerFactory;
    private RegionSpringRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);
        managerFactory = ctx.getBean(EntityManagerFactory.class);
        repository = ctx.getBean(RegionSpringRepositoryImpl.class);
    }

    @AfterEach
    void tearDown() {
        managerFactory.close();
    }

    @Test
    void findAll() {
        Optional<List<Region>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Integer id = 4312;
        Optional<Region> actual = repository.findById(id);
        assertTrue(actual.isPresent());
    }

    @Test
    void findByFieldCountryID() {
        String fieldName = "countryId";
        Integer value = 3159;
        Optional<List<Region>> actual = repository.findByField(fieldName, value);
        assertNotNull(actual);
    }

    @Test
    void findByFieldName() {
        String fieldName = "name";
        String value = "Бурятия";
        Optional<List<Region>> actual = repository.findByField(fieldName, value);
        assertNotNull(actual);
    }

    @Test
    void findByWrongFieldValue() {
        String fieldName = "countryId";
        Integer value = 111111;
        Optional<List<Region>> actual = repository.findByField(fieldName, value);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByWrongField() {
        String fieldName = "countryID";
        Integer value = 3159;
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
        Integer value = 3159;
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
        Integer value = 3159;
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
        Integer value = 3159;
        try {
            repository.findByField(fieldName, value);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void createNotDetectedEntity() {
        Region region = new Region();
        region.setCountryId(111111);
        region.setCityId(0);
        region.setName("Region");
        Region actual = repository.create(region);
        assertEquals(region, actual);
        actual = repository.delete(actual.getId());
    }

    @Test
    void createDetectedEntity() {
        Region region = new Region();
        region.setId(3407);
        region.setCountryId(3159);
        region.setCityId(0);
        region.setName("Бурятия");
        Region actual = repository.create(region);
        assertEquals(region, actual);
    }

    @Test
    void updateNotDetectedEntity() {
        Region region = new Region();
        region.setCountryId(111111);
        region.setCityId(0);
        region.setName("Region");
        Region actual = repository.update(region);
        assertEquals(region, actual);
        repository.delete(actual.getId());
    }

    @Test
    void updateDetectedEntity() {
        Region region = new Region();
        region.setCountryId(111111);
        region.setCityId(0);
        region.setName("Region");
        Region actual = repository.create(region);
        actual.setCountryId(111000);
        region.setCityId(25);
        actual.setName("Actual");
        Region test = repository.update(actual);
        assertEquals(test, actual);
        repository.delete(actual.getId());
    }

    @Test
    void deleteDetectedEntity() {
        Region region = new Region();
        region.setCityId(0);
        region.setCountryId(11111);
        region.setName("Region");
        Region test = repository.create(region);
        Integer id = test.getId();
        Region actual = repository.delete(id);
        assertEquals(actual, test);
    }

    @Test
    void deleteNotDetectedEntity() {
        Integer id = -1;
        Region actual = repository.delete(id);
        assertNull(actual);
    }
}