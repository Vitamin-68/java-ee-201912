package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.Region;
import ua.hillel.jdbcRepo.JdbcRepoRegion;
import ua.hillel.utils.CsvToDBLoader;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RegionJdbcTest {

    private JdbcRepoRegion repositoryRead;
    private JdbcRepoRegion repositoryDml;
    private static H2Server server;

    @BeforeEach
    void initEach() {
        repositoryRead = new JdbcRepoRegion("STUDY.REGION");
        repositoryDml = new JdbcRepoRegion("STUDY.REGION_TMP");
    }

    @BeforeAll
    static void initAll() {
        CsvToDBLoader loader = new CsvToDBLoader();
        server = new H2Server(9092);
        String into = "create table STUDY.REGION_TMP as SELECT * FROM STUDY.REGION LIMIT 0";
        try {
            server.start();
            PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(into);
            stmt.execute();
            loader.addFromCsv("src/test/resources/region.csv", "STUDY.REGION_TMP");
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
        Optional<Region> expected = repositoryRead.findById(3407);
        int actual = 3407;
        Assertions.assertEquals(actual, expected.get().getRegionId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("Region_id", "3407");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("country_id", "3159");
        int actual = 77;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("region_id", "3407");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("name", "Бурятия");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new Region(1852456, 12365, 212, "Просто регион"));
        Optional<Region> region = repositoryDml.findById(1852456);
        int expected = region.get().getRegionId();
        int actual = 1852456;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateCountryTest() {
        repositoryDml.update(new Region(3407, 315945, 0, "Бурятия"));
        Optional<Region> region = repositoryDml.findById(3407);
        int expected = region.get().getCountryId();
        int actual = 315945;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateRegionTest() {
        repositoryDml.update(new Region(1998532, 3159, 120, "Адыгея"));
        Optional<Region> region = repositoryDml.findById(1998532);
        int expected = region.get().getRegionId();
        int actual = 1998532;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new Region(1998532, 3159, 0, "Кофу Туц-Туц"));
        Optional<Region> region = repositoryDml.findById(1998532);
        String expected = region.get().getName();
        String actual = "Кофу Туц-Туц";
        Assertions.assertEquals(actual, expected);
    }

    @AfterAll
    static void finish() throws InterruptedException {
        String sql = "drop table STUDY.REGION_TMP";
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
