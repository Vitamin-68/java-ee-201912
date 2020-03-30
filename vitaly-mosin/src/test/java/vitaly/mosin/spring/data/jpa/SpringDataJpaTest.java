package vitaly.mosin.spring.data.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import vitaly.mosin.repository.exceptions.MyRepoException;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;
import vitaly.mosin.spring.data.jpa.repository.SpringDataRepository;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SpringDataJpaTest {
    private static final int PORT = NetUtils.getFreePort();
    private static final H2Server h2Server = new H2Server(PORT);
    private static EntityManagerFactory entityManagerFactory;
    private static SpringDataRepository dataRepo;

    private final int NEW_CITY_ID = 987654;
    private final int NEW_CITY_COUNTRY_ID = 987;
    private final int NEW_CITY_REGION_ID = 654;
    private final int NOT_EXIST_ID = 100000001;
    private final int REALLY_EXISTING_ID = 10;
    private final String NEW_CITY_NAME = "Зажопинск";
    private final String NEW_CITY_NAME_UPDATE = "Рукоблудівка";
    private final String EXISTING_CITY_NAME = "Гилонг";
    private final String NAME_FIELD_4SEEK = "name";

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
            assertTrue(result.size() > 0);
        }
    }

    @Test
    void findById() {
        dataRepo.setClazz(CityJdata.class);
        dataRepo.setEntityManagerFactory(entityManagerFactory);
        Optional<CityJdata> result = dataRepo.findById(REALLY_EXISTING_ID);
        result.ifPresent(cityJdata -> assertEquals(EXISTING_CITY_NAME, cityJdata.getName()));

        result = dataRepo.findById(NOT_EXIST_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByField() {
        dataRepo.setEntityManagerFactory(entityManagerFactory);
        dataRepo.setClazz(CityJdata.class);
        Optional<List<CityJdata>> result = (Optional<List<CityJdata>>) dataRepo
                .findByField(NAME_FIELD_4SEEK, EXISTING_CITY_NAME);
        if (result.isPresent()) {
            assertEquals(1, result.get().size());
            for (CityJdata city : result.get()) {
                assertEquals(REALLY_EXISTING_ID, city.getId());
            }
        }
        result = (Optional<List<CityJdata>>) dataRepo.findByField(NAME_FIELD_4SEEK, "QWERTYг");
        assertEquals(0, result.get().size());

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
        dataRepo.create(createNewCity(NEW_CITY_ID, NEW_CITY_COUNTRY_ID,
                NEW_CITY_REGION_ID, NEW_CITY_NAME));
        AbstractEntity entity = dataRepo.update(createNewCity(NEW_CITY_ID, NEW_CITY_COUNTRY_ID,
                NEW_CITY_REGION_ID, NEW_CITY_NAME_UPDATE));
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

        //attempt to delete a nonexistent entity
        assertThrows(MyRepoException.class, () -> dataRepo.delete(NOT_EXIST_ID));
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