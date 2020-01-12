package ua.ithillel.dnepr.common.test.repository.dmitry.litvyak;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.dml.Repositories.jdbcCrudRepositoryImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

public class JdbcIndexedCrudRepository {
    private static final int PORT = NetUtils.getFreePort();

    private H2Server h2Server;
    private jdbcCrudRepositoryImpl<TestEntity, Integer> crudRepository;
    private String repoRootPath;

    @BeforeEach
    void setup() throws IOException, SQLException, ClassNotFoundException {
        repoRootPath = File.createTempFile("h2DB", "test").getPath().toString();// "/./src/main/resources/h2crudbase_test";

        h2Server = new H2Server(PORT);
        h2Server.start();

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(
                String.format("jdbc:h2:tcp://%s:%s/%s", NetUtils.getHostName(), PORT, repoRootPath));
        crudRepository = new jdbcCrudRepositoryImpl<>(connection, TestEntity.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        h2Server.stop();
        //не удаляет из темпа - а перегружать машину я нежелаю
        Files.deleteIfExists(Path.of(repoRootPath));
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
