package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.hillel.jpaRepo.JpaRepo;
import ua.hillel.jpa_entity.CityJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


@Slf4j
public class JpaTest {

    private static EntityManagerFactory factory;
    private static EntityManager manager;
    private static EntityTransaction transaction;
    private static JpaRepo cityJpa;

    @BeforeAll
    public static void init() {
        factory = Persistence.createEntityManagerFactory("persistence-config-for-test");
        manager = factory.createEntityManager();
        transaction = manager.getTransaction();
        Class<?> clazz = CityJpa.class;
        cityJpa = new JpaRepo(clazz, manager, transaction);
        cityJpa.create(new CityJpa(122348, 3159, 4312, "Some city"));
    }

    @AfterAll
    public static void clear() {
        cityJpa.delete(122347);
    }

    @Test
    void findAllTest() {
        Assertions.assertTrue(cityJpa.findAll().isPresent());
    }

    @Test
    void findByIdTest() {
        Assertions.assertTrue(cityJpa.findById(4400).isPresent());
    }

    @Test
    void findByFieldTest() {
        Assertions.assertTrue(cityJpa.findByField("id", 4400).isPresent());
    }

    @Test
    void createTest() {
        cityJpa.create(new CityJpa(122347, 3159, 4312, "ddd"));
        Assertions.assertTrue(cityJpa.findByField("name", "ddd").isPresent());
    }

    @Test
    void updateTest() {
        Assertions.assertNotNull(cityJpa.update(new CityJpa(4400, 3159, 4312, "Велыкки Вуйкы")));
    }

    @Test
    void deleteTest() {
        cityJpa.delete(122348);
        Assertions.assertNotNull(cityJpa.findById(122348).isPresent());
    }
}
