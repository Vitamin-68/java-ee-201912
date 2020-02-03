package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.Country;
import ua.hillel.jdbcRepo.JdbcRepoCountry;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class CountryJdbcTestRead {

    private static JdbcRepoCountry repositoryRead;
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";
    private static H2Server server;
    private static Connection connection;

    @BeforeAll
    public static void initConnection() {
        try {
            server = new H2Server(PORT);
            connection = server.getConnection(DBNAME);
            repositoryRead = new JdbcRepoCountry("STUDY.COUNTRY", PORT, DBNAME, server, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @AfterAll
    public static void close() throws SQLException {
        connection.close();
        server.close();
    }

    @Test
    public void findAllTest() {
        assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<Country> expected = repositoryRead.findById(11014);
        assertEquals(11014, expected.get().getCountryId());
    }

    private void assertEquals(int i, int countryId) {
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<Country>> expected = repositoryRead.findByField("country_id", "11014");
        assertEquals(1, expected.get().size());
    }


    @Test
    public void findByNameTest() {
        Optional<List<Country>> expected = repositoryRead.findByField("name", "Ямайка");
        assertEquals(1, expected.get().size());
    }
}
