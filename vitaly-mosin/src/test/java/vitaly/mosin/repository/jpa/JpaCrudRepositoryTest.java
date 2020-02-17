package vitaly.mosin.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.jpa.entity.CityJpa;
import vitaly.mosin.repository.jpa.entity.CountryJpa;
import vitaly.mosin.repository.jpa.entity.RegionJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class JpaCrudRepositoryTest<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {

    final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence-unit");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    ;
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
        assertEquals("Киев", resultCity.get().getName());
        assertEquals("Украина", resultCity.get().getCountry().getName());
        assertEquals("Киевская обл.", resultCity.get().getRegion().getName());

        //поиск несуществующего города
        resultCity = jpaCrudRepository.findById(-1);
        assertEquals(Optional.empty(), resultCity);

        Optional<CountryJpa> resultCountry;
        jpaCrudRepository = new JpaCrudRepository(CountryJpa.class, entityManagerFactory);
        resultCountry = jpaCrudRepository.findById(4);
        assertEquals("Австралия", resultCountry.get().getName());
    }

    @Test
    void findByField() {
        Optional<List<CityJpa>> result;
        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
        // поиск по имени
        result = jpaCrudRepository.findByField("name", "'Киев'");
        for (CityJpa city : result.get()) {
            assertEquals("Киев", city.getName());
        }

        //поиск по региону
        result = jpaCrudRepository.findByField("region", 5);
        assertEquals(10, result.get().size());
        for (CityJpa city : result.get()) {
            assertEquals("Виктория", city.getRegion().getName());
            assertEquals("Австралия", city.getCountry().getName());
        }
        //поиск по стране, id Австралии = 4
        result = jpaCrudRepository.findByField("country", 4);
        assertEquals(50, result.get().size());
        for (CityJpa city : result.get()) {
            assertEquals("Австралия", city.getCountry().getName());
        }

        //неверное имя
        result = jpaCrudRepository.findByField("name", "'Киев123'");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void create() {
        Integer testId = 87654321;
//        jpaCrudRepository = new JpaCrudRepository(CityJpa.class, entityManagerFactory);
//        CityJpa testCity = makeNewCity(testId);

        transaction = entityManager.getTransaction();
        transaction.begin();
        CityJpa testCity = new CityJpa();
        testCity.setId(testId);
        testCity.setCountry(entityManager.find(CountryJpa.class, 4));
        testCity.setRegion(entityManager.find(RegionJpa.class, 5));
        testCity.setName("Test City");
        entityManager.persist(testCity);
        transaction.commit();

//        jpaCrudRepository.create(testCity);
//        assertEquals(testId, jpaCrudRepository.create(testCity).getId());
    }

    CityJpa makeNewCity(Integer testId) {
        CityJpa testCity = new CityJpa();
        testCity.setId(testId);
        testCity.setCountry(entityManager.find(CountryJpa.class, 4));
        testCity.setRegion(entityManager.find(RegionJpa.class, 5));
        testCity.setName("Test City");
        return testCity;
    }

    @Test
    void update() {
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