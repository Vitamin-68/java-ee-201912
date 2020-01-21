package vitaly.mosin.repository.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import vitaly.mosin.repository.entity.City;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vitaly.mosin.repository.Constants.FILE_PATH_RESOURCE;

@Slf4j
class JdbcIndexedCrudRepositoryTest {
    private static final int PORT = NetUtils.getFreePort();
    private final String TEST_DB_NAME = "test_db";
    private static final String PATH_TEST_DB = "./target/temp_DB/";
    private static H2Server h2Server = new H2Server(PORT);
    private Connection connection;
    private JdbcIndexedCrudRepository crudRepository;
    private String repoRootPath = PATH_TEST_DB + TEST_DB_NAME;

    private City newCity = new City(123999999, 1, 2, "NewCity");
    private City existCity = new City(10, 4, 5, "Гилонг");
    private City updCity = new City(10, 4, 500, "UpdateCity");

    @BeforeAll
    static void init() {
        if (!Files.exists(Path.of(PATH_TEST_DB))) {
            try {
                Files.createDirectory(Path.of(PATH_TEST_DB));
            } catch (IOException e) {
                log.error("Failed create directory", e);
            }
        }
        try {
            h2Server.start();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void initClose() {
        h2Server.stop();
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {

        try {
            Files.copy(new File(FILE_PATH_RESOURCE + TEST_DB_NAME + ".mv.db").toPath(),
                    new File(PATH_TEST_DB + TEST_DB_NAME + ".mv.db").toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to copy files", e);
        }
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(
                String.format("jdbc:h2:tcp://%s:%s/%s", NetUtils.getHostName(), PORT, repoRootPath));
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findAll() {
        crudRepository = new JdbcIndexedCrudRepository(connection, City.class);
        Optional<List<City>> result = crudRepository.findAll();
        assertTrue(result.isPresent());

    }

    @Test
    void findById() {
        crudRepository = new JdbcIndexedCrudRepository(connection, existCity.getClass());
        Optional<City> result = crudRepository.findById(existCity.getId());
        assertEquals(result.get().getId(), existCity.getId());
        assertEquals(result.get().getCountryId(), existCity.getCountryId());
        assertTrue(result.get().getRegionId().equals(existCity.getRegionId()));
        assertTrue(result.get().getName().equals(existCity.getName()));

        //поиск несуществующей сущности
        result = crudRepository.findById(newCity.getId());
        assertEquals(Optional.empty(), result);
    }

    @Test
    void findByField() {
        crudRepository = new JdbcIndexedCrudRepository(connection, existCity.getClass());
        //поиск по имени
        Optional<List<City>> result = crudRepository.findByField("name", existCity.getName());
        assertTrue(result.isPresent());
        for (City city : result.get()) {
            assertEquals(city.getName(), existCity.getName());
        }
        //поиск по id
        result = crudRepository.findByField("id", existCity.getId());
        assertTrue(result.get().size() == 1);
        for (City city : result.get()) {
            assertEquals(city.getName(), existCity.getName());
            assertEquals(city.getCountryId(), existCity.getCountryId());
            assertEquals(city.getId(), existCity.getId());
            assertEquals(city.getRegionId(), existCity.getRegionId());
        }
        //wrong id
        result = crudRepository.findByField("id", newCity.getId());
        assertTrue(result.get().size() == 0);
    }

    @Test
    void addIndex() {
    }

    @Test
    void addIndexes() {
    }

    @Test
    void create() {
        crudRepository = new JdbcIndexedCrudRepository(connection, newCity.getClass());
        Optional<List<City>> sizeDbBeforeCreate = crudRepository.findAll();
        assertEquals(crudRepository.create(newCity), newCity);
        Optional<List<City>> sizeDbAfterCreate = crudRepository.findAll();
        assertEquals(sizeDbAfterCreate.get().size() - sizeDbBeforeCreate.get().size(), 1);
    }

    @Test
    void update() {
        crudRepository = new JdbcIndexedCrudRepository(connection, existCity.getClass());
        Optional<List<City>> sizeDbBeforeUpdate = crudRepository.findAll();
        assertEquals(((City) crudRepository.update(updCity)).getName(), updCity.getName());
        Optional<List<City>> sizeDbAfterUpdate = crudRepository.findAll();
        assertEquals(sizeDbAfterUpdate.get().size(), sizeDbBeforeUpdate.get().size());
    }

    @Test
    void delete() {
        crudRepository = new JdbcIndexedCrudRepository(connection, existCity.getClass());
        Optional<List<City>> sizeDbBeforeDelete = crudRepository.findAll();
        assertEquals(crudRepository.delete(existCity.getId()).getId(), existCity.getId());
        Optional<List<City>> sizeDbAfterDelete = crudRepository.findAll();
        assertEquals(sizeDbBeforeDelete.get().size() - sizeDbAfterDelete.get().size(), 1);
    }

}