package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.AppConfig;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.SpringCity;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.SpringRegion;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository.SpringCityRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository.SpringRegionRepositoryImp;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpringDataTest {
    static final H2Server h2Server = new H2Server(12345);
    static private EntityManagerFactory entityManagerFactory;
    static private SpringCityRepositoryImp cityRepository;
    static private SpringRegionRepositoryImp regionRepository;

    @BeforeAll
    static void setup(){
        try {
            h2Server.start();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        final AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);
        entityManagerFactory = annotationConfigApplicationContext.getBean(EntityManagerFactory.class);
        cityRepository = annotationConfigApplicationContext.getBean(SpringCityRepositoryImp.class);
        regionRepository = annotationConfigApplicationContext.getBean(SpringRegionRepositoryImp.class);

        //String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(AbstractEntity.class, City.class));
        //Repository<T> repository = (Repository<T>) annotationConfigApplicationContext.getBean(beanNamesForType[0]);
    }

    @Test
    @Order(1)
    void createCity(){
        SpringCity testCity = new SpringCity();
        testCity.setName("testName");
        testCity.setCountryId(111);
        testCity.setRegionId(222);
        testCity.setId(9999);
        cityRepository.create(testCity);
        Optional<SpringCity> test3 = cityRepository.findById(9999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    void createRegion(){
        SpringRegion testRegion = new SpringRegion();
        testRegion.setName("testName");
        testRegion.setCountryId(111);
        testRegion.setCityId(222);
        testRegion.setId(777);
        regionRepository.create(testRegion);
        Optional<SpringRegion> test3 = regionRepository.findById(777);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    @Order(2)
    void findAll() {
        Optional<List<SpringCity>> cities = cityRepository.findAll();
        Assertions.assertFalse(cities.get().isEmpty());
    }

    @Test
    @Order(2)
    void findById() {
        Optional<SpringCity> test = cityRepository.findById(9999);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    @Order(2)
    void findByField() {
        Optional<List<SpringCity>> test = cityRepository.findByField("name", "testName");
        Assertions.assertFalse(test.isEmpty());
        Optional<List<SpringCity>> test2 = cityRepository.findByField("countryId", 111);
        Assertions.assertFalse(test2.isEmpty());
    }

    @Test
    @Order(5)
    void delete() {
        SpringCity testCity = new SpringCity();
        testCity.setName("deleteCity");
        testCity.setCountryId(111);
        testCity.setRegionId(222);
        testCity.setId(333);
        cityRepository.create(testCity);

        SpringCity test = cityRepository.delete(333);
        Assertions.assertNotNull(cityRepository.findById(333));
    }

    @Test
    @Order(3)
    void update() {
        SpringCity testCity = new SpringCity();
        testCity.setName("????????????");
        testCity.setCountryId(333);
        testCity.setRegionId(33);
        testCity.setId(9999);

        SpringCity test = cityRepository.update(testCity);
        Assertions.assertNotNull(test);
    }

    @AfterAll
    static void close(){
        entityManagerFactory.close();
        h2Server.stop();
    }
}
