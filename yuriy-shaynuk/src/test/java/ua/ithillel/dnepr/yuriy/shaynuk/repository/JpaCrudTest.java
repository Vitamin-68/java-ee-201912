package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.JpaCrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

@Slf4j
class JpaCrudTest {
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;
    private static CrudRepository<City, Integer> jpaCrudRepository;

    @BeforeAll
    static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();
        jpaCrudRepository = new JpaCrudRepository<>(entityManager, City.class);
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = jpaCrudRepository.findAll();
        Assertions.assertFalse(cities.get().isEmpty());
    }

    @Test
    void findById() {
        Optional<City> test = jpaCrudRepository.findById(999);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<City>> test = jpaCrudRepository.findByField("name", "testName");
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void create() {
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountryId(111);
        testCity.setRegionId(222);
        testCity.setId(9999);
        jpaCrudRepository.create(testCity);
        Optional<City> test3 = jpaCrudRepository.findById(9999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    void delete() {
        City testCity = new City();
        testCity.setName("deleteCity");
        testCity.setCountryId(11);
        testCity.setRegionId(22);
        testCity.setId(333);
        jpaCrudRepository.create(testCity);

        City test = jpaCrudRepository.delete(333);
        Assertions.assertNotNull(jpaCrudRepository.findById(333));
    }

    @Test
    void update() {
        City testCity = new City();
        testCity.setName("Москва");
        testCity.setCountryId(3159);
        testCity.setRegionId(4312);
        testCity.setId(9999);

        City test = jpaCrudRepository.update(testCity);
        Assertions.assertNotNull(test);
    }

    @AfterAll
    static void close(){
        entityManager.close();
        entityManagerFactory.close();
    }
}
