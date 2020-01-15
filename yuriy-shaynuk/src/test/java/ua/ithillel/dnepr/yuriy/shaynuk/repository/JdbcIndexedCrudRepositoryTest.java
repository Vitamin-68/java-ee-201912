package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

@Slf4j
class JdbcIndexedCrudRepositoryTest {
    private static final int PORT = NetUtils.getFreePort();
    private static final String TEST_DB_NAME = "test_db.sqlite3";

    private H2Server h2Server;
    private JdbcIndexedCrudRepository<City, Integer> crudRepository;

//    @BeforeEach
//    void setup() throws IOException, SQLException, ClassNotFoundException {
//        String repoRootPath = Path.of(
//                "C:\\Users\\unsreg\\AppData\\Local\\Temp\\romanGizatulin",
//                //Files.createTempDirectory("romanGizatulin").toString(),
//                TEST_DB_NAME
//        ).toString();
//        h2Server = new H2Server(PORT);
//        h2Server.start();
//
//        Class.forName("org.h2.Driver");
//        Connection connection = DriverManager.getConnection(
//                String.format("jdbc:h2:tcp://%s:%s/%s", NetUtils.getHostName(), PORT, repoRootPath));
//        crudRepository = new JdbcIndexedCrudRepository<>(connection, City.class);
//    }
//
//    @AfterEach
//    void tearDown() {
//        h2Server.stop();
//    }
//
//    @Test
//    void createOneNewEntity() {
////        testCreateOneNewEntity(crudRepository);
//    }
//
//    @Test
//    void createManyNewEntities() {
////        testCreateManyNewEntities(crudRepository);
//    }
}