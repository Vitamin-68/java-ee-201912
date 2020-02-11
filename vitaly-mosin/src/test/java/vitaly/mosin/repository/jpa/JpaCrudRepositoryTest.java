package vitaly.mosin.repository.jpa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vitaly.mosin.repository.entity.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class JpaCrudRepositoryTest {

    final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence-unit");
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private JpaCrudRepository jpaCrudRepository;

    @BeforeEach
    void setUp() {
//        entityManager = entityManagerFactory.createEntityManager();
//        transaction = entityManager.getTransaction();
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @Test
    void findAll() {
        jpaCrudRepository = new JpaCrudRepository(City.class, entityManagerFactory);
        assertNotNull(jpaCrudRepository.findAll());
    }

    @Test
    void findById() {
    }

    @Test
    void findByField() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}