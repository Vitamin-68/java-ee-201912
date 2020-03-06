package ua.ithillel.dnepr.tymoshenko.olga.repository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import ua.ithillel.dnepr.tymoshenko.olga.entity.Region;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RegionRepositoryTest {
    private RegionRepository repository;
    private final Character DELIMITER = ';';

    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("."));

    @BeforeEach
    void setUp() throws IOException {
        String fileName = "region.csv";
        final String RESOURSE_ROOT_PATH = "../common/src/main/resources/";
        Path sourse = Path.of(RESOURSE_ROOT_PATH, fileName);
        File tmpFile;
        folder.create();
        tmpFile = folder.newFile(fileName);
        Files.copy(sourse, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        repository = new RegionRepository(tmpFile, DELIMITER);
    }

    @AfterEach
    void tearDown() {
        folder.delete();
    }

    @Test
    void RepositoryFileIsEmpty() {
        File fileName = new File("");
        try {
            CityRepository testRepo = new CityRepository(fileName, DELIMITER);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findAll() {
        Optional<List<Region>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Integer id = 4312;
        Optional<Region> region = repository.findById(id);
        assertFalse(region.isEmpty());
    }

    @Test
    void findByFailId() {
        Integer id = 55000;
        Optional<Region> region = repository.findById(id);
        assertTrue(region.isEmpty());
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Адыгея";
        Optional<List<Region>> regions = repository.findByField(field, value);
        assertFalse(regions.isEmpty());
    }

    @Test
    void findByEmptyField() {
        String field = "";
        Integer value = 4312;
        try {
            repository.findByField(field, value);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByNullField() {
        String field = null;
        Integer value = 4312;
        try {
            repository.findByField(field, value);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByIllegalField() {
        String field = "regionid";
        Integer value = 4312;
        Optional<List<Region>> regions = repository.findByField(field, value);
        assertTrue(regions.isEmpty());
    }

    @Test
    void findByIllegalValue() {
        String field = "region_id";
        Integer value = -1;
        Optional<List<Region>> regions = repository.findByField(field, value);
        assertTrue(regions.isEmpty());
    }

    @Test
    void create() {
        Region region = new Region();
        region.setCityId(1);
        region.setId(2);
        region.setCountryId(3);
        region.setName("Region");
        Region actual = repository.create(region);
        assertEquals(region, actual);
    }

    @Test
    void updateRegionFind() {
        Region region = new Region();
        region.setCityId(0);
        region.setId(3407);
        region.setCountryId(3159);
        region.setName("Region");
        Region actual = repository.update(region);
        assertEquals(region, actual);
    }

    @Test
    void updateRegionNotFind() {

        Region region = new Region();
        region.setCityId(0);
        region.setId(-20);
        region.setCountryId(3159);
        region.setName("Region");
        Region actual = repository.update(region);
        assertEquals(region, actual);
    }

    @Test
    void deleteRegionFind() {
        Integer id = 3407;
        Region actual = repository.delete(id);
        assertEquals(id, actual.getId());
    }

    @Test
    void deleteNotRegionFind() {
        Integer id = -20;
        Region actual = repository.delete(id);
        assertNull(actual);
    }
}