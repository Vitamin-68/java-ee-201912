package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

@Slf4j
class JpaCrudTest {
    @BeforeAll
    static void setUp() {
        final EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("persistence-unit");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        final List<City> result = entityManager.createQuery("from City", City.class).getResultList();
    }

    @Test
    void countCities() {

    }
}
