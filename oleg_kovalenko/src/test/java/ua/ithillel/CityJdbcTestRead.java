package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.City;
import ua.hillel.jdbcRepo.JdbcRepoCity;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class CityJdbcTestRead {

    private static JdbcRepoCity repositoryRead;
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";
    private static H2Server server;
    private static Connection connection;

    @BeforeAll
    public static void init() {
        server = new H2Server(PORT);
        try {
            connection = server.getConnection(DBNAME);
            repositoryRead = new JdbcRepoCity("STUDY.CITY", PORT, DBNAME, server, connection);
        } catch (SQLException e) {
            log.error("Error {} ", e.getMessage());
        }
    }

    @AfterAll
    public static void finish() throws SQLException {
        connection.close();
        server.close();
    }

    @Test
    void findAllTest() {
        assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<City> expected = repositoryRead.findById(4400);
        assertEquals(4400, expected.get().getCityId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("city_id", "4313");
        assertEquals(1, expected.get().size());
    }

    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("country_id", "3159");
        assertEquals(2505, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("region_id", "4312");
        assertEquals(172, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<City>> expected = repositoryRead.findByField("name", "Абрамцево");
        assertEquals(1, expected.get().size());
    }
}
