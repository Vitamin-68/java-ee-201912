package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.Region;
import ua.hillel.jdbcRepo.JdbcRepoRegion;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class RegionJdbcTestDML {

    private static JdbcRepoRegion repositoryDml;
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";
    private static H2Server server;
    private static Connection connection;

    @BeforeAll
    public static void init() {
        String into = "create table STUDY.REGION_TMP as SELECT * FROM STUDY.REGION";
        try {
            server = new H2Server(PORT);
            connection = server.getConnection(DBNAME);
            PreparedStatement stmt = connection.prepareStatement(into);
            stmt.executeUpdate();
            repositoryDml = new JdbcRepoRegion("STUDY.REGION_TMP", PORT, DBNAME, server, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @AfterAll
    static void finish() {
        String sql = "drop table STUDY.REGION_TMP";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            connection.close();
            server.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }


    @Test
    public void createTest() {
        repositoryDml.create(new Region(1852456, 12365, 212, "Просто регион"));
        Optional<Region> region = repositoryDml.findById(1852456);
        int expected = region.get().getRegionId();
        assertEquals(1852456, expected);
    }

    @Test
    public void updateCountryTest() {
        repositoryDml.update(new Region(3407, 315945, 0, "Бурятия"));
        Optional<Region> region = repositoryDml.findById(3407);
        int expected = region.get().getCountryId();
        assertEquals(315945, expected);
    }

    @Test
    public void updateRegionTest() {
        repositoryDml.update(new Region(1998532, 3159, 120, "Адыгея"));
        Optional<Region> region = repositoryDml.findById(1998532);
        int expected = region.get().getRegionId();
        assertEquals(1998532, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new Region(1998532, 3159, 0, "Кофу Туц-Туц"));
        Optional<Region> region = repositoryDml.findById(1998532);
        String expected = region.get().getName();
        assertEquals("Кофу Туц-Туц", expected);
    }
}
