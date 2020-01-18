package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.Country;
import ua.hillel.jdbcRepo.JdbcRepoCountry;
import ua.hillel.utils.CsvToDBLoader;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CountryJdbcTest {
    private JdbcRepoCountry repositoryRead;
    private JdbcRepoCountry repositoryDml;
    private static H2Server server;

    @BeforeEach
    void initEach() {
        repositoryRead = new JdbcRepoCountry("STUDY.COUNTRY");
        repositoryDml = new JdbcRepoCountry("STUDY.COUNTRY_TMP");
    }

    @BeforeAll
    static void initAll() {
        CsvToDBLoader loader = new CsvToDBLoader();
        server = new H2Server(9092);
        String into = "create table STUDY.COUNTRY_TMP as SELECT * FROM STUDY.COUNTRY LIMIT 0";
        try {
            server.start();
            PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(into);
            stmt.execute();
            loader.addFromCsv("src/test/resources/country.csv", "STUDY.COUNTRY_TMP");

        } catch (SQLException e) {
            log.error("error creation {}", e.getMessage());
        }
    }

    @Test
    public void findAllTest() {
        Assertions.assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<Country> expected = repositoryRead.findById(11014);
        int actual = 11014;
        Assertions.assertEquals(actual, expected.get().getCountryId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<Country>> expected = repositoryRead.findByField("country_id", "11014");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }


    @Test
    public void findByNameTest() {
        Optional<List<Country>> expected = repositoryRead.findByField("name", "Ямайка");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new Country(7895233, 12365, "Просто страна"));
        Optional<Country> country = repositoryDml.findById(7895233);
        int expected = country.get().getCountryId();
        int actual = 7895233;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateCountryIdTest() {
        repositoryDml.update(new Country(11060, 123657, "Япония"));
        Optional<Country> country = repositoryDml.findById(11060);
        int expected = country.get().getCountryId();
        int actual = 11060;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new Country(582106, 0, "Ямайка Туц-Туц"));
        Optional<Country> country = repositoryDml.findById(582106);
        String expected = country.get().getName();
        String actual = "Ямайка Туц-Туц";
        Assertions.assertEquals(actual, expected);
    }

    @AfterAll
    static void finish() throws InterruptedException {
        String sql = "drop table STUDY.COUNTRY_TMP";
        try {
            server.start();
            PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql);
            stmt.execute();
            server.stop();
            server.close();
        } catch (SQLException e) {
            log.error("error drop {}", e.getMessage());
        }
    }
}
