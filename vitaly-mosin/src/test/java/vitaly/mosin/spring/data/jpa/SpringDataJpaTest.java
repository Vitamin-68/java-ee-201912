package vitaly.mosin.spring.data.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.jdbc.JdbcIndexedCrudRepository;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;
import vitaly.mosin.spring.data.jpa.entity.CountryJdata;
import vitaly.mosin.spring.data.jpa.repository.SpringDataRepository;

import javax.persistence.EntityManagerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class SpringDataJpaTest {
    private static final int PORT = NetUtils.getFreePort();
    private static final H2Server h2Server = new H2Server(PORT);
    //    private static SpringDataJpaComponent dataJpaComponent;
    private static EntityManagerFactory entityManagerFactory;
    private static SpringDataRepository dataRepo;

    @BeforeAll
    static void setUp() {
        try {
            h2Server.start();
            final AnnotationConfigApplicationContext annotationConfigApplicationContext =
                    new AnnotationConfigApplicationContext(SpringDataJpaConfig.class);
            entityManagerFactory = annotationConfigApplicationContext.getBean(EntityManagerFactory.class);
            dataRepo = annotationConfigApplicationContext.getBean(SpringDataRepository.class);
//            dataJpaComponent = annotationConfigApplicationContext.getBean(SpringDataRepositoryImpl.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
        h2Server.stop();
    }

    @Test
    void findAll() {
//        dataRepo = new SpringDataRepository(entityManagerFactory);
        dataRepo.setClazz(CityJdata.class);
        if (dataRepo.findAll().isPresent()) {
            List<?> result = (List<?>) dataRepo.findAll().get();
            assertEquals(143, result.size());
        }
    }

    @Test
    void findById() {
        dataRepo.setClazz(CityJdata.class);
        dataRepo.setEntityManagerFactory(entityManagerFactory);
        Optional<CityJdata> result = dataRepo.findById(10);
        result.ifPresent(cityJdata -> assertEquals("Гилонг", cityJdata.getName()));
    }
}