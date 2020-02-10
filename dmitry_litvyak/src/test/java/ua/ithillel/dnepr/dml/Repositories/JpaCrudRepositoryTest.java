package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.jpa.City;
import ua.ithillel.dnepr.dml.domain.jpa.Country;
import ua.ithillel.dnepr.dml.domain.jpa.Region;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JpaCrudRepositoryTest {

    private JpaCrudRepository jpaCrudRepository;
    private Class testClass = City.class;
    private EntityManager entityManager;

    public JpaCrudRepositoryTest() {

        entityManager = Persistence.createEntityManagerFactory("persistence-unit-dml").createEntityManager();
    }

    @BeforeEach
    void setUp() {
        jpaCrudRepository = new JpaCrudRepository(entityManager, testClass);
    }

    @Test
    void findAll() {
        assertTrue(jpaCrudRepository.findAll().isPresent());
    }

    @Test
    void findById() {
        Optional<City> tmpCity = jpaCrudRepository.findById(4331);
        assertTrue(tmpCity.isPresent());
        assertEquals(tmpCity.get().getName(), "Вождь Пролетариата");
    }

    @Test
    void findByField() {
        Optional<City> tmpCity = jpaCrudRepository.findByField("name", "Лобня");
        assertTrue(tmpCity.isPresent());
    }

    @Test
    void createDelete() {

        Integer lId = 99998;
        City tmpCity = new City();
        tmpCity.setName("Bandershtadt");
        tmpCity.setId(lId);
        Region region = new Region();
        region.setId(lId);
        tmpCity.setRegion(region);
        Country country = new Country();
        country.setId(lId);
        tmpCity.setCountry(country);
        jpaCrudRepository.create(tmpCity);
        Optional<City> optTmpCity = jpaCrudRepository.findById(lId);
        assertTrue(optTmpCity.isPresent());
        jpaCrudRepository.delete(tmpCity);
        optTmpCity = jpaCrudRepository.findById(lId);
        assertTrue(optTmpCity.isEmpty());
    }

    @Test
    void update() {
        Optional<City> optTmpCity = jpaCrudRepository.findById(4331);
        if (optTmpCity.isPresent()) {
            City tmpCity = optTmpCity.get();
            String oldName = tmpCity.getName();
            tmpCity.setName(tmpCity.getName() + " UPDATED");
            jpaCrudRepository.update(tmpCity);
            City newTmpCity = (City) jpaCrudRepository.findById(4331).get();
            assertTrue(Objects.equals(tmpCity.getName(), newTmpCity.getName()));
            tmpCity.setName(oldName);
            jpaCrudRepository.update(tmpCity);
        }
    }

}

