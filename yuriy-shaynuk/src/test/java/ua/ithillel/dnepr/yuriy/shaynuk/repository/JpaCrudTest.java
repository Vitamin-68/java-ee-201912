package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.JpaCrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity.Country;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity.Region;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

@Slf4j
class JpaCrudTest {
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;
    private static CrudRepository<City, Integer> jpaCityRepository;
//    private static CrudRepository<Country, Integer> jpaCountryRepository;
//    private static CrudRepository<Region, Integer> jpaRegionRepository;

    @BeforeAll
    static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();

       jpaCityRepository = new JpaCrudRepository<>(entityManager, City.class);
//        jpaCountryRepository = new JpaCrudRepository<>(entityManager, Country.class);
//        jpaRegionRepository = new JpaCrudRepository<>(entityManager, Region.class);
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = jpaCityRepository.findAll();
        Assertions.assertFalse(cities.get().isEmpty());
    }

    @Test
    void findById() {
        Optional<City> test = jpaCityRepository.findById(999);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<City>> test = jpaCityRepository.findByField("name", "testName");
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void create() {
        Country testCountry = new Country();
        testCountry.setId(111);
        testCountry.setName("testCountry");
        //jpaCountryRepository.create(testCountry);

        Region testRegion = new Region();
        testRegion.setId(11);
        testRegion.setName("testRegion");
        //jpaRegionRepository.create(testRegion);

        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountry(testCountry);
        testCity.setRegion(testRegion);
        testCity.setId(9999);
        jpaCityRepository.create(testCity);
        Optional<City> test3 = jpaCityRepository.findById(9999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    void delete() {
        City testCity = new City();
        testCity.setName("deleteCity");
        //testCity.setCountryId(11);
       // testCity.setRegionId(22);
        testCity.setId(333);
        jpaCityRepository.create(testCity);

        City test = jpaCityRepository.delete(333);
        Assertions.assertNotNull(jpaCityRepository.findById(333));
    }

    @Test
    void update() {
        City testCity = new City();
        testCity.setName("Москва");
        //testCity.setCountryId(3159);
        //testCity.setRegionId(4312);
        testCity.setId(9999);

        City test = jpaCityRepository.update(testCity);
        Assertions.assertNotNull(test);
    }

    @AfterAll
    static void close(){
        entityManager.close();
        entityManagerFactory.close();
    }
}
