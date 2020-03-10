package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.City;
import ua.hillel.jdbcRepo.JdbcRepoCity;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CityJdbcTestDML {

    private static JdbcRepoCity repositoryDml;
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";
    private static H2Server server;
    private static Connection connection;

    @BeforeAll
    public static void init() {
        String into = "create table STUDY.CITY_TMP as SELECT * FROM STUDY.CITY";
        try {
            server = new H2Server(PORT);
            connection = server.getConnection(DBNAME);
            PreparedStatement stmt = connection.prepareStatement(into);
            stmt.executeUpdate();
            repositoryDml = new JdbcRepoCity("STUDY.CITY_TMP", PORT, DBNAME, server, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @AfterAll
    static void finish() throws InterruptedException {
        String sql = "drop table STUDY.CITY_TMP";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            connection.close();
            server.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void createTest() {
        repositoryDml.create(new City(1852456, 12365, 212, "Просто город"));
        Optional<City> city = repositoryDml.findById(1852456);
        int expected = city.get().getCityId();
        assertEquals(1852456, expected);
    }

    @Test
    public void updateCountryTest() {
        repositoryDml.update(new City(11358, 123657, 11354, "Убе"));
        Optional<City> city = repositoryDml.findById(11358);
        int expected = city.get().getCountryId();
        assertEquals(123657, expected);
    }

    @Test
    public void updateRegionTest() {
        repositoryDml.update(new City(11359, 11060, 2120, "Хаги"));
        Optional<City> city = repositoryDml.findById(11359);
        int expected = city.get().getRegionId();
        assertEquals(2120, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new City(11364, 11060, 11363, "Кофу Туц-Туц"));
        Optional<City> city = repositoryDml.findById(11364);
        String expected = city.get().getName();
        assertEquals("Кофу Туц-Туц", expected);
    }
}
