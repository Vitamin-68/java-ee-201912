package ua.ithillel.dnepr.common.test.repository.roman.gizatulin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.roman.gizatulin.repository.JdbcIndexedCrudRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class JdbcIndexedCrudRepositoryIntegrationTest {
    private static final String TEST_DB_NAME = "test_db";

    private H2Server h2Server;
    private JdbcIndexedCrudRepository<TestEntity, Integer> crudRepository;

    @BeforeEach
    void setup() throws IOException, SQLException {
        String repoRootPath = Path.of(
                Files.createTempDirectory("romanGizatulin").toString(),
                TEST_DB_NAME
        ).toString();
        h2Server = new H2Server();

        log.info("Database path: {}", repoRootPath);
        Connection connection = h2Server.getConnection(repoRootPath);
        crudRepository = new JdbcIndexedCrudRepository<>(connection, TestEntity.class);
    }

    @AfterEach
    void tearDown() {
        h2Server.stop();
    }

    @Test
    void createOneNewEntity() {
        testCreateOneNewEntity(crudRepository);
    }

    @Test
    void createManyNewEntities() {
        testCreateManyNewEntities(crudRepository);
    }
}