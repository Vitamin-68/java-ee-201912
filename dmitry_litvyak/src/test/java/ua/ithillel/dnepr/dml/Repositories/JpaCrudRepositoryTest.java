package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.domain.jpa.City;
import ua.ithillel.dnepr.dml.domain.jpa.Country;
import ua.ithillel.dnepr.dml.domain.jpa.Region;
import ua.ithillel.dnepr.dml.domain.jpa.User;

import javax.persistence.EntityManager;
import java.util.Objects;
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
            settings.put(Environment.DRIVER,"org.h2.Driver");
            settings.put(Environment.SHOW_SQL,true);
            settings.put(Environment.HBM2DDL_AUTO,"validate");
            settings.put(Environment.DIALECT,"org.hibernate.dialect.H2Dialect");
            //settings.put(Environment.)
            config.setProperties(settings);
            config.addAnnotatedClass(testClass);
            config.addAnnotatedClass(Country.class);
            config.addAnnotatedClass(Region.class);
            config.addAnnotatedClass(User.class);
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
        JpaCrudRepository userRepo = new JpaCrudRepository(entityManager, User.class);
        User user = new User();
        user.setId(1);
        user.setFName("Jhon");
        user.setLName("Dow");
        userRepo.create(user);
//        Integer lId = 99998;
//        City tmpCity = new City();
//        tmpCity.setName("Bandershtadt");
//        tmpCity.setId(lId);
//        jpaCrudRepository.create(tmpCity);
//        Optional<City> optTmpCity = jpaCrudRepository.findById(lId);
//        assertTrue(optTmpCity.isPresent());
//        jpaCrudRepository.delete(tmpCity);
//        optTmpCity = jpaCrudRepository.findById(lId);
//        assertTrue(optTmpCity.isEmpty());
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

