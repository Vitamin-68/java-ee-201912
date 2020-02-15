package ua.ithillel.dnepr.common.test.repository.yuriy.shaynuk;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsCrudRepository;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.Utils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc.CqrsRepositoryImp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class JdbcIndexedCrudRepositoryIntegrationTest {
    //private static final int PORT = NetUtils.getFreePort();
    private static final String TEST_DB_NAME = "test_sqlite.db";

    //private static H2Server h2Server;
    private static CqrsCrudRepository<TestEntity, Integer> cqrsRepository;
    private static CrudRepository<TestEntity, Integer> csvCrudRepository;

    @BeforeEach
    void setup() throws IOException, SQLException, ClassNotFoundException {
        String tempDirectory = Files.createTempDirectory("jdbcTest").toString();
        String repoRootPath = Path.of(
                //"C:\\Temp\\",
                tempDirectory,
                TEST_DB_NAME
        ).toString();
        log.debug(repoRootPath);
//        h2Server = new H2Server(PORT);
//        h2Server.start();
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:".concat(repoRootPath));
        File dataFile = Utils.createTempFile("test.csv");
        if (dataFile != null) {
            csvCrudRepository = new CrudRepositoryImp<>(dataFile.getPath(), TestEntity.class);
        }
        cqrsRepository = new CqrsRepositoryImp<>(connection, TestEntity.class, csvCrudRepository);
    }

//    @AfterEach
//    void tearDown() {
//        h2Server.stop();
//    }

    @Test
    void createOneNewEntity() {
        testCreateOneNewEntity(cqrsRepository);
    }

    @Test
    void createManyNewEntities() {
       testCreateManyNewEntities(cqrsRepository);
    }
}