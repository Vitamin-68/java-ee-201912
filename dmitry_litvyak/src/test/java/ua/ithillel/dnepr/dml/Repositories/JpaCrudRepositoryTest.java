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

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JpaCrudRepositoryTest {

    private static SessionFactory sessionFactory;
    private final String DATABASE_NAME = System.getProperty("user.dir") + "\\target\\classes\\dev\\db\\main";
    private JpaCrudRepository jpaCrudRepository;
    private Class testClass = City.class;

    public JpaCrudRepositoryTest() {
        setupsession();
    }

    private void setupsession() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            Properties settings = new Properties();
            settings.put(Environment.URL, "jdbc:h2:file:\\" + DATABASE_NAME);
            settings.put(Environment.USER,"sa");
            settings.put(Environment.PASS,"");
            config.setProperties(settings);
            config.addAnnotatedClass(testClass);
            config.addAnnotatedClass(Country.class);
            config.addAnnotatedClass(Region.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
            this.sessionFactory = config.buildSessionFactory(serviceRegistry);
        }
    }

    @BeforeEach
    void setUp() {
        jpaCrudRepository = new JpaCrudRepository(testClass);
    }

    @Test
    void findAll() {
        assertTrue(jpaCrudRepository.findAll().isPresent());
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

    @Test
    void close() {
    }
}

