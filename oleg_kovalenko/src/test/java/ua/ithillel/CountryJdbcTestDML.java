package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.Country;
import ua.hillel.jdbcRepo.JdbcRepoCountry;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CountryJdbcTestDML {

    private static JdbcRepoCountry repositoryDml;
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";

    private static H2Server server;
    private static Connection connection;

    @BeforeAll
    public static void init() {
        String into = "create table STUDY.COUNTRY_TMP as SELECT * FROM STUDY.COUNTRY";

        try {
            server = new H2Server(PORT);
            connection = server.getConnection(DBNAME);
            PreparedStatement stmt = connection.prepareStatement(into);
            stmt.executeUpdate();
            repositoryDml = new JdbcRepoCountry("STUDY.COUNTRY_TMP", PORT, DBNAME, server, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @AfterAll
    static void finish() {
        String sql = "drop table STUDY.COUNTRY_TMP";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            connection.close();
            server.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void createTest() {
        repositoryDml.create(new Country(7895233, 12365, "Просто страна"));
        Optional<Country> country = repositoryDml.findById(7895233);
        int expected = country.get().getCountryId();
        assertEquals(7895233, expected);
    }

    @Test
    public void updateCountryIdTest() {
        repositoryDml.update(new Country(11060, 123657, "Япония"));
        Optional<Country> country = repositoryDml.findById(11060);
        int expected = country.get().getCountryId();
        assertEquals(11060, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new Country(582106, 0, "Ямайка Туц-Туц"));
        Optional<Country> country = repositoryDml.findById(582106);
        String expected = country.get().getName();
        assertEquals("Ямайка Туц-Туц", expected);
    }
}
