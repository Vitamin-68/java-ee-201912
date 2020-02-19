package vitaly.mosin.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vitaly.mosin.repository.exceptions.MyRepoException;
import vitaly.mosin.repository.jpa.entity.CityJpa;
import vitaly.mosin.repository.jpa.entity.CountryJpa;
import vitaly.mosin.repository.jpa.entity.RegionJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class JpaCrudRepositoryTest {

    final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence-unit");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    private EntityTransaction transaction;
    private JpaCrudRepository jpaCrudRepository;

    @BeforeEach
    void setUp() {
//        entityManager = entityManagerFactory.createEntityManager();
//        transaction = entityManager.getTransaction();
    }

    @AfterEach
    void tearDown() {
//        entityManager.close();
//        entityManagerFactory.close();
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
        if (resultCity.isPresent()) {
            assertEquals("Киев", resultCity.get().getName());
            assertEquals("Украина", resultCity.get().getCountry().getName());
            assertEquals("Киевская обл.", resultCity.get().getRegion().getName());
        }

        //поиск несуществующего города
        resultCity = jpaCrudRepository.findById(-1);
        assertEquals(Optional.empty(), resultCity);

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
        quantityRecordsBeforeCreate.get().size();
        jpaCrudRepository.create(testCity);
        Optional<CityJpa> result = jpaCrudRepository.findById(testId);
        quantityRecordsAfterCreate = jpaCrudRepository.findAll();

        result.ifPresent(cityJpa -> assertEquals("Test City", cityJpa.getName()));
        assertEquals(1,
                quantityRecordsAfterCreate.get().size() - quantityRecordsBeforeCreate.get().size());

        //try create already existing object with same id
        assertThrows(MyRepoException.class, () -> jpaCrudRepository.create(testCity));
    }

    CityJpa makeTestCity(int cityId, int regionId, int countryId, String name){
        CityJpa testCity = new CityJpa();
        testCity.setId(cityId);
        testCity.setRegion(entityManager.find(RegionJpa.class, regionId));
        testCity.setCountry(entityManager.find(CountryJpa.class, countryId));
        testCity.setName(name);
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

        //rollback
        testCity.setName("Днепропетровск");
        jpaCrudRepository.update(testCity);
    }

    @Test
    void delete() {
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);

//        CountryJpa testCountry = new CountryJpa();
//        RegionJpa testRegion = new RegionJpa();
//        CityJpa testCity = new CityJpa();
//
//        testCountry.setId(4);
//        testRegion.setId(5);

//        testCity.setName("Бендиго");
//        testCity.setRegion(testRegion);
//        testCity.setCountry(testCountry);
        jpaCrudRepository.delete(7);

    }
}