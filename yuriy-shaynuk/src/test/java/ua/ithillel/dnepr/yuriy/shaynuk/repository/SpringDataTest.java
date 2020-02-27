package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.AppConfig;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository.SpringCityRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository.TestRepository;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class SpringDataTest {
    static final H2Server h2Server = new H2Server(12345);
    static private EntityManagerFactory entityManagerFactory;
    static private SpringCityRepositoryImp cityRepository;

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

        //TestRepository<City, Integer> testRepository = new TestRepository<>();
        String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(AbstractEntity.class, City.class));

        //Repository<T> repository = (Repository<T>) annotationConfigApplicationContext.getBean(beanNamesForType[0]);
        log.warn("ss");
    }

    @Test
    void start(){
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountryId(111);
        testCity.setRegionId(222);
        testCity.setId(999);
        cityRepository.create(testCity);
        Optional<City> test3 = cityRepository.findById(999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @AfterAll
    static void close(){
        entityManagerFactory.close();
        h2Server.stop();
    }
}
