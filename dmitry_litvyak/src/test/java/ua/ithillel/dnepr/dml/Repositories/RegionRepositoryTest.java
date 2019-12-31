package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dml.Repositories.RegionRepository;
import ua.ithillel.dnepr.dml.domain.Region;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
class RegionRepositoryTest {

    public static final String SRC_MAIN_RESOURCES_REGION_CSV = "./src/main/resources/region.csv";

    private RegionRepository regionRepository;
    @BeforeEach
    void setUp() {

        regionRepository = new RegionRepository(SRC_MAIN_RESOURCES_REGION_CSV);

    }

    @Test
    void findAll() {
        Assertions.assertNotNull(regionRepository.findAll().get());
    }

    @Test
    void findById() {
        Region test = regionRepository.findById(3468).get();
        log.debug(test.getName());
        Assertions.assertEquals(3468,test.getId());
    }

    @Test
    void findByField() {
        Optional<Region> test = regionRepository.findByField("name","Еврейская обл.");
        Assertions.assertNotNull(test);
        Optional<Region> test2 = regionRepository.findByField("name","some string name");
        Assertions.assertTrue(test2.isEmpty());
        Optional<Region> test3 = regionRepository.findByField("1name1",99999);
        Assertions.assertTrue(test3.isEmpty());

    }

    @Test
    void create() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_REGION_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_REGION_CSV),Paths.get(newFile));
        RegionRepository updateRepo = new RegionRepository(newFile);
        Region test = new Region();
        test.setName("Unknown");
        test.setCountry_id(12349999);
        test.setId(999999);
        test.setCity_id(0);
        updateRepo.update(test);
        Optional<Region> testUpd = updateRepo.findById(12349999);
        Assertions.assertNotNull(testUpd);
        Files.delete(Paths.get(newFile));
    }

    @Test
    void update() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_REGION_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_REGION_CSV),Paths.get(newFile));
        RegionRepository updateRepo = new RegionRepository(newFile);
        Region test = updateRepo.findById(3468).get();
        String testName = "+++NaN+++";
        test.setName(testName);
        updateRepo.update(test);
        Assertions.assertEquals(testName,updateRepo.findById(3468).get().getName());
        Files.delete(Paths.get(newFile));
    }

    @Test
    void delete() throws IOException {
        String newFile = SRC_MAIN_RESOURCES_REGION_CSV + ".out.csv";
        Files.copy(Paths.get(SRC_MAIN_RESOURCES_REGION_CSV),Paths.get(newFile));
        RegionRepository updateRepo = new RegionRepository(newFile);
        Region test = updateRepo.findById(3468).get();
        updateRepo.delete(3468);
        Assertions.assertTrue(updateRepo.findById(3468).isEmpty());
        Files.delete(Paths.get(newFile));

    }
}