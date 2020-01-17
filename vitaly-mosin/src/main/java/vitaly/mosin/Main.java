package vitaly.mosin;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.jdbc.JdbcIndexedCrudRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("=== My application started ===");
        final int PORT = NetUtils.getFreePort();
        final String TEST_DB_NAME = "test_db";
        final String PATH_TEST_DB = "./vitaly-mosin/target/temp_DB/";
        Connection connection;


        H2Server h2Server;
        if (!Files.exists(Path.of(PATH_TEST_DB))) {
            Files.createDirectory(Path.of(PATH_TEST_DB));
        }
        String repoRootPath = PATH_TEST_DB + TEST_DB_NAME;

        h2Server = new H2Server(PORT);
        try {
            h2Server.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(
                String.format("jdbc:h2:tcp://%s:%s/%s", NetUtils.getHostName(), PORT, repoRootPath));
        JdbcIndexedCrudRepository crudRepository = new JdbcIndexedCrudRepository(connection, City.class);

//        crudRepository.findAll();
//        crudRepository.findById(4400);
//        crudRepository.findByField("id", 4313);
        crudRepository.findByField("name", "Москва");
        System.out.println("Done");

        h2Server.stop();

    }
}
