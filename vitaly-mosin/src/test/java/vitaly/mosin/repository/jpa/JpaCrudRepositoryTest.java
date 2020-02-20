package vitaly.mosin.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vitaly.mosin.repository.exceptions.MyRepoException;
import vitaly.mosin.repository.jpa.entity.CityJpa;
import vitaly.mosin.repository.jpa.entity.CountryJpa;
import vitaly.mosin.repository.jpa.entity.RegionJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class JpaCrudRepositoryTest {

    static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence-unit");
    private static final String DB_PATH_TMP = "./target/classes/dev/db/";
    private static final String DB_PATH_RESOURCE = "./src/main/resources/dev/db/";
    private static final String DB_FILE = "mainRepoVM.mv.db";
    private JpaCrudRepository jpaCrudRepository;

    @BeforeAll
    static void setUp() {
//        File folder = new File(DB_PATH_TMP);
//        if (!folder.exists()) {
//            folder.mkdir();
//        }
//        try {
//            Files.copy(new File(DB_PATH_RESOURCE + DB_FILE).toPath(),
//                    new File(DB_PATH_TMP + DB_FILE).toPath(), REPLACE_EXISTING);
//        } catch (IOException e) {
//            log.error("Failed to copy files", e);
//        }
    }

    @AfterAll
    static void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void findAll() {
        Optional<List<EntityType>> result;
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        result = jpaCrudRepository.findAll();
        assertEquals(10969, result.get().size());

        jpaCrudRepository = new JpaCrudRepository(CountryJpa.class, entityManagerFactory);
        result = jpaCrudRepository.findAll();
        assertEquals(106, result.get().size());

        jpaCrudRepository = new JpaCrudRepository(RegionJpa.class, entityManagerFactory);
        result = jpaCrudRepository.findAll();
        assertEquals(922, result.get().size());
    }

    @Test
    void findById() {
        Optional<CityJpa> resultCity;
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        resultCity = jpaCrudRepository.findById(10184);

        //поиск в City
        if (resultCity.isPresent()) {
            assertEquals("Киев", resultCity.get().getName());
            assertEquals("Украина", resultCity.get().getCountry().getName());
            assertEquals("Киевская обл.", resultCity.get().getRegion().getName());
        }

        //поиск несуществующего города
        resultCity = jpaCrudRepository.findById(-1);
        assertEquals(Optional.empty(), resultCity);

        //поиск в Country
        jpaCrudRepository = new JpaCrudRepository(CountryJpa.class, entityManagerFactory);
        Optional<CountryJpa> resultCountry = jpaCrudRepository.findById(4);
        if (resultCountry.isPresent()) {
            assertEquals("Австралия", resultCountry.get().getName());
        }
    }

    @Test
    void findByField() {
        Optional<List<CityJpa>> result;
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        // поиск по имени
        result = jpaCrudRepository.findByField("name", "'Киев'");
        if (result.isPresent()) {
            for (CityJpa city : result.get()) {
                assertEquals("Киев", city.getName());
            }
        }

        //поиск по региону
        result = jpaCrudRepository.findByField("region", 5);
        if (result.isPresent()) {
            assertEquals(10, result.get().size());
            for (CityJpa city : result.get()) {
                assertEquals("Виктория", city.getRegion().getName());
                assertEquals("Австралия", city.getCountry().getName());
            }
        }

        //поиск по стране, id Австралии = 4
        result = jpaCrudRepository.findByField("country", 4);
        if (result.isPresent()) {
            assertEquals(50, result.get().size());
            for (CityJpa city : result.get()) {
                assertEquals("Австралия", city.getCountry().getName());
            }
        }

        //неверное имя
        result = jpaCrudRepository.findByField("name", "'Киев123'");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void create() {
        int testId = 87654321;
        int counId = 4;
        int regId = 5;
        String testName = "Test City";

        Optional<List<EntityType>> quantityRecordsBeforeCreate, quantityRecordsAfterCreate;
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        CityJpa testCity = makeTestCity(testId, regId, counId, testName);
        quantityRecordsBeforeCreate = jpaCrudRepository.findAll();
        jpaCrudRepository.create(testCity);
        Optional<CityJpa> result = jpaCrudRepository.findById(testId);
        quantityRecordsAfterCreate = jpaCrudRepository.findAll();

        result.ifPresent(cityJpa -> assertEquals("Test City", cityJpa.getName()));
        assertEquals(1,
                quantityRecordsAfterCreate.get().size() - quantityRecordsBeforeCreate.get().size());

        //try create already existing object with same id
        assertThrows(MyRepoException.class, () -> jpaCrudRepository.create(testCity));

        //rollback - delete testCity
        jpaCrudRepository.delete(testId);
    }

    CityJpa makeTestCity(int cityId, int regionId, int countryId, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CityJpa testCity = new CityJpa();
        testCity.setId(cityId);
        testCity.setRegion(entityManager.find(RegionJpa.class, regionId));
        testCity.setCountry(entityManager.find(CountryJpa.class, countryId));
        testCity.setName(name);
        entityManager.close();
        return testCity;
    }

    @Test
    void update() {
        int testId = 9977;
        int cntryId = 9908;
        int regId = 9964;
        String testName = "Дніпро is the best";

        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        CityJpa testCity = makeTestCity(testId, regId, cntryId, testName);
        jpaCrudRepository.update(testCity);
        Optional<CityJpa> result = jpaCrudRepository.findById(testId);
        result.ifPresent(cityJpa -> assertEquals("Дніпро is the best", cityJpa.getName()));

        //rollback - set old name
        testCity.setName("Днепропетровск");
        jpaCrudRepository.update(testCity);
    }

    @Test
    void delete() {
        Optional<CityJpa> resultCity;
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        resultCity = jpaCrudRepository.findById(7);
        Optional<List<EntityType>> quantityRecordsBeforeDelete, quantityRecordsAfterDelete;

        quantityRecordsBeforeDelete = jpaCrudRepository.findAll();
        jpaCrudRepository.delete(7);
        quantityRecordsAfterDelete = jpaCrudRepository.findAll();
        assertEquals(1,
                quantityRecordsBeforeDelete.get().size() - quantityRecordsAfterDelete.get().size());

        //rollback - restore deleted city
        jpaCrudRepository.create(resultCity.get());
    }
}