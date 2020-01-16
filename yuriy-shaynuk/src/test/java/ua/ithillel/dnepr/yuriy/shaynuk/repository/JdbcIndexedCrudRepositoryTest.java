package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc.JdbcIndexedCrudRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class JdbcIndexedCrudRepositoryTest {
    private static final int PORT = NetUtils.getFreePort();
    private static final String TEST_DB_NAME = "test_sqlite.db";

    private static H2Server h2Server;
    private static JdbcIndexedCrudRepository<City, Integer> cityRepository;

    @BeforeAll
    static void setup() throws IOException, SQLException, ClassNotFoundException {
        //String tempDirectory = Files.createTempDirectory("jdbcTest").toString();
        String repoRootPath = Path.of(
                "C:\\Temp\\",
                //tempDirectory,
                TEST_DB_NAME
        ).toString();
//        log.debug(repoRootPath);
        h2Server = new H2Server(PORT);
        h2Server.start();
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:".concat(repoRootPath));
        cityRepository = new JdbcIndexedCrudRepository<>(connection, City.class);
    }

    @AfterAll
    static void tearDown() {
        h2Server.stop();
    }

//    @Test
//    void findAll() {
//        Optional<List<City>> cities = cityRepository.findAll();
//        assertFalse(cities.get().isEmpty());
//    }
//
//    @Test
//    void findById() {
//        Optional<City> test = cityRepository.findById(999);
//        Assertions.assertFalse(test.isEmpty());
//    }
//
//    @Test
//    void findByField() {
//        Optional<List<City>> test = cityRepository.findByField("name", "Москва");
//        Assertions.assertFalse(test.isEmpty());
//        Optional<List<City>> test2 = cityRepository.findByField("name", "some string name");
//        Assertions.assertTrue(test2.isEmpty());
//        Optional<List<City>> test3 = cityRepository.findByField("1name1", 99999);
//        Assertions.assertTrue(test3.isEmpty());
//    }

    @Test
    void create() {
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountry_id(11111);
        testCity.setRegion_id(222);
        testCity.setId(999);
        cityRepository.create(testCity);
        Optional<City> test3 = cityRepository.findById(999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    void update() {
        City testCity = new City();
        testCity.setName("Москва");
        testCity.setCountry_id(31599);
        testCity.setRegion_id(4312);
        testCity.setId(999);

        City test = cityRepository.update(testCity);
        Assertions.assertNotNull(test);
    }

    @Test
    void delete() {
        City testCity = new City();
        testCity.setName("deleteCity");
        testCity.setCountry_id(11);
        testCity.setRegion_id(22);
        testCity.setId(333);
        cityRepository.create(testCity);

        City test = cityRepository.delete(333);
        Assertions.assertNotNull(cityRepository.findById(333));
    }
}