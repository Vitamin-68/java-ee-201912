package vitaly.mosin.spring.data.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;
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
    private static EntityManagerFactory entityManagerFactory;
    private static SpringDataRepository dataRepo;
    private final int NEW_CITY_ID = 987654;
    private final int NEW_CITY_COUNTRY_ID = 987;
    private final int NEW_CITY_REGION_ID = 654;
    private final String NEW_CITY_NAME = "Зажопинск";
    private final String NEW_CITY_NAME_UPDATE = "Рукоблудівка";

    @BeforeAll
    static void setUp() {
        try {
            h2Server.start();
            final AnnotationConfigApplicationContext annotationConfigApplicationContext =
                    new AnnotationConfigApplicationContext(SpringDataJpaConfig.class);
            entityManagerFactory = annotationConfigApplicationContext.getBean(EntityManagerFactory.class);
            dataRepo = annotationConfigApplicationContext.getBean(SpringDataRepository.class);
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

    @Test
    void findByField() {
        dataRepo.setEntityManagerFactory(entityManagerFactory);
        dataRepo.setClazz(CityJdata.class);
        Optional<List<CityJdata>> result = (Optional<List<CityJdata>>) dataRepo.findByField("name", "Гилонг");
        if (result.isPresent()) {
            assertEquals(1, result.get().size());
            for (CityJdata city : result.get()) {
                assertEquals(10, city.getId());
            }
        }
    }

    @Test
    void create() {
        AbstractEntity entity = dataRepo.create(createNewCity(NEW_CITY_ID,
                NEW_CITY_COUNTRY_ID, NEW_CITY_REGION_ID, NEW_CITY_NAME));
        Optional<CityJdata> result = dataRepo.findById(entity.getId());
        result.ifPresent(cityJdata -> assertEquals(NEW_CITY_NAME, cityJdata.getName()));
    }

    @Test
    void update() {
        dataRepo.create(createNewCity(NEW_CITY_ID,
                NEW_CITY_COUNTRY_ID, NEW_CITY_REGION_ID, NEW_CITY_NAME));
        AbstractEntity entity = dataRepo.update(createNewCity(NEW_CITY_ID,
                NEW_CITY_COUNTRY_ID, NEW_CITY_REGION_ID, NEW_CITY_NAME_UPDATE));
        Optional<CityJdata> result = dataRepo.findById(entity.getId());
        result.ifPresent(cityJdata -> assertEquals(NEW_CITY_NAME_UPDATE, cityJdata.getName()));
        dataRepo.delete(entity.getId());
    }

    @Test
    void delete() {
        AbstractEntity entity = dataRepo.delete(createNewCity(NEW_CITY_ID,
                NEW_CITY_COUNTRY_ID, NEW_CITY_REGION_ID, NEW_CITY_NAME_UPDATE).getId());
        Optional<CityJdata> result = dataRepo.findById(entity.getId());
        assertEquals(Optional.empty(), result);
    }

    private CityJdata createNewCity(int id, int countryId, int regionId, String name) {
        CityJdata newCity = new CityJdata();
        newCity.setId(id);
        newCity.setCountryId(countryId);
        newCity.setRegionId(regionId);
        newCity.setName(name);
        return newCity;
    }
}