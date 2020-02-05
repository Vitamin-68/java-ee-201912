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
    private static CrudRepository<City, Integer> crudRepository;

    @BeforeAll
    static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();
        crudRepository = new JpaCrudRepository<>(entityManager, City.class);
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = crudRepository.findAll();
        Assertions.assertFalse(cities.get().isEmpty());
    }

    @Test
    void findById() {
        Optional<City> test = crudRepository.findById(999);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<City>> test = crudRepository.findByField("name", "testName");
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void create() {
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountryId(111);
        testCity.setRegionId(222);
        testCity.setId(999);
        crudRepository.create(testCity);
        Optional<City> test3 = crudRepository.findById(999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @AfterAll
    static void close(){
        entityManager.close();
        entityManagerFactory.close();
    }
}
