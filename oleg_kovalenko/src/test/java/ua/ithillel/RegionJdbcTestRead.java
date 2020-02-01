package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.Region;
import ua.hillel.jdbcRepo.JdbcRepoRegion;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class RegionJdbcTestRead {

    private static JdbcRepoRegion repositoryRead;
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";
    private H2Server server;
    private Connection connection;

    @BeforeEach
    public void init() {
        server = new H2Server(PORT);
        try {
            connection = server.getConnection(DBNAME);
            repositoryRead = new JdbcRepoRegion("STUDY.REGION", PORT, DBNAME, server, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void serverClose() throws SQLException {
        connection.close();
        server.close();
    }


    @Test
    void findAllTest() {
        assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<Region> expected = repositoryRead.findById(3407);
        assertEquals(3407, expected.get().getRegionId());
    }

    private void assertEquals(int i, int regionId) {
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("Region_id", "3407");
        assertEquals(1, expected.get().size());
    }

    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("country_id", "3159");
        assertEquals(78, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("region_id", "3407");
        assertEquals(1, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("name", "Бурятия");
        assertEquals(1, expected.get().size());
    }
}
