package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.City;
import ua.hillel.jdbcRepo.JdbcRepoCity;
import ua.hillel.utils.CsvToDBLoader;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CityJdbcTest {

    private JdbcRepoCity repositoryRead;
    private JdbcRepoCity repositoryDml;
    private static H2Server server;

    @BeforeEach
    void initEach() {
        repositoryRead = new JdbcRepoCity("STUDY.CITY");
        repositoryDml = new JdbcRepoCity("STUDY.CITY_TMP");
    }

    @BeforeAll
    static void initAll() {
        CsvToDBLoader loader = new CsvToDBLoader();
        server = new H2Server(9092);
        String into = "create table STUDY.CITY_TMP as SELECT * FROM STUDY.CITY LIMIT 0";
        try {
            server.start();
            PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(into);
            stmt.execute();
            loader.addFromCsv("src/test/resources/city.csv", "STUDY.CITY_TMP");

        } catch (SQLException e) {
            log.error("error creation {}", e.getMessage());
        }
    }

    @Test
    void findAllTest() {
        Assertions.assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<City> expected = repositoryRead.findById(4400);
        int actual = 4400;
        Assertions.assertEquals(actual, expected.get().getCityId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("city_id", "4313");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("country_id", "3159");
        int actual = 2505;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("region_id", "4312");
        int actual = 172;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<City>> expected = repositoryRead.findByField("name", "Абрамцево");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new City(1852456, 12365, 212, "Просто город"));
        Optional<City> city = repositoryDml.findById(1852456);
        int expected = city.get().getCityId();
        int actual = 1852456;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateCountryTest() {
        repositoryDml.update(new City(11358, 123657, 11354, "Убе"));
        Optional<City> city = repositoryDml.findById(11358);
        int expected = city.get().getCountryId();
        int actual = 123657;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateRegionTest() {
        repositoryDml.update(new City(11359, 11060, 2120, "Хаги"));
        Optional<City> city = repositoryDml.findById(11359);
        int expected = city.get().getRegionId();
        int actual = 2120;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new City(11364, 11060, 11363, "Кофу Туц-Туц"));
        Optional<City> city = repositoryDml.findById(11364);
        String expected = city.get().getName();
        String actual = "Кофу Туц-Туц";
        Assertions.assertEquals(actual, expected);
    }

    @AfterAll
    static void finish() throws InterruptedException {
        String sql = "drop table STUDY.CITY_TMP";
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
