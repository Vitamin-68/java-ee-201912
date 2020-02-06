package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.jpa.City;
import ua.ithillel.dnepr.dml.domain.jpa.Country;
import ua.ithillel.dnepr.dml.domain.jpa.Region;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JpaCrudRepositoryTest {

    private static SessionFactory sessionFactory;
    private final String DATABASE_NAME = System.getProperty("user.dir") + "\\target\\classes\\dev\\db\\main";
    private JpaCrudRepository jpaCrudRepository;
    private Class testClass = City.class;
    private EntityManager entityManager;

    public JpaCrudRepositoryTest() {
        setupsession();
    }

    private void setupsession() {

        if (sessionFactory == null) {
            Configuration config = new Configuration();
            Properties settings = new Properties();
            settings.put(Environment.URL, "jdbc:h2:file:\\" + DATABASE_NAME);
            settings.put(Environment.USER, "sa");
            settings.put(Environment.PASS, "");
            config.setProperties(settings);
            config.addAnnotatedClass(testClass);
            config.addAnnotatedClass(Country.class);
            config.addAnnotatedClass(Region.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
            sessionFactory = config.buildSessionFactory(serviceRegistry);
        }
        try {
            entityManager = sessionFactory.createEntityManager();
        } catch (Exception e) {
            log.error("Unable init entity manager:", e);
        }
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
        Optional<City> tmpCity = jpaCrudRepository.findById(Long.valueOf(4331));
        assertTrue(tmpCity.isPresent());
        assertEquals(tmpCity.get().getName(),"Вождь Пролетариата");
    }

    @Test
    @Disabled
    void findByField() {
        Optional<City> tmpCity = jpaCrudRepository.findByField("name","Лобня");
        assertTrue(tmpCity.isPresent());
    }

    @Test
    void createUpdateDelete() {
        Long lId = Long.valueOf(99999);
        City tmpCity = new City();
        tmpCity.setName("Bandershtadt");
        tmpCity.setLId(lId);
        jpaCrudRepository.update(tmpCity);
        Optional<City> optTmpCity = jpaCrudRepository.findById(lId);
        assertTrue(optTmpCity.isPresent());
        jpaCrudRepository.delete(tmpCity);
        optTmpCity = jpaCrudRepository.findById(lId);
        assertTrue(optTmpCity.isEmpty());
    }

}

