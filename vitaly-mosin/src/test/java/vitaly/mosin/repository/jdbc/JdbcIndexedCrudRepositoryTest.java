package vitaly.mosin.repository.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static vitaly.mosin.repository.Constants.FILE_PATH_RESOURCE;
import static vitaly.mosin.repository.Constants.FILE_PATH_TMP;

@Slf4j
class JdbcIndexedCrudRepositoryTest {
    private final int PORT = NetUtils.getFreePort();
    private final String TEST_DB_NAME = "test_db";
    private final String PATH_TEST_DB = "./vitaly-mosin/target/temp_DB/";
    private H2Server h2Server;
    private Connection connection;
    private String repoRootPath = PATH_TEST_DB + TEST_DB_NAME;

    @BeforeAll
    void init() {
        try {
            h2Server.start();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {

        try {
            if (!Files.exists(Path.of(PATH_TEST_DB))) {
                Files.createDirectory(Path.of(PATH_TEST_DB));
            }
            Files.copy(new File(FILE_PATH_RESOURCE + TEST_DB_NAME).toPath(),
                    new File(FILE_PATH_TMP + TEST_DB_NAME).toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to copy files", e);
        }
        connection = DriverManager.getConnection(
                String.format("jdbc:h2:tcp://%s:%s/%s", NetUtils.getHostName(), PORT, repoRootPath));
        JdbcIndexedCrudRepository crudRepository = new JdbcIndexedCrudRepository(connection, City.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByField() {
    }

    @Test
    void addIndex() {
    }

    @Test
    void addIndexes() {
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

}