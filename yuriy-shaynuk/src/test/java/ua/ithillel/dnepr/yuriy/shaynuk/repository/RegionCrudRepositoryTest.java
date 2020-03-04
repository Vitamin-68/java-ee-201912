package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.Utils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.File;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class RegionCrudRepositoryTest {
    static File dataFile =null;
    static private CrudRepository<Region, Integer> regionRepository;

    @BeforeAll
    static void setUp() {
        dataFile = Utils.createTempFile("region.csv");
        if (dataFile != null) {
            regionRepository = new CrudRepositoryImp<>(dataFile.getPath(),Region.class);
        }
    }

    @Test
    void findAll() {
        Optional<List<Region>> regions = regionRepository.findAll();
        assertFalse(regions.isEmpty());
    }

    @Test
    void findById() {
        Optional<Region> test = regionRepository.findById(99999);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<Region>> test = regionRepository.findByField("name", "Москва!!");
        Assertions.assertFalse(test.isEmpty());
        Optional<List<Region>> test2 = regionRepository.findByField("name", "some string name");
        Assertions.assertTrue(test2.isEmpty());
        Optional<List<Region>> test3 = regionRepository.findByField("1name1", 99999);
        Assertions.assertTrue(test3.isEmpty());
    }

    @Test
    void create() {
        Region testRegion = new Region();
        testRegion.setName("testName");
        testRegion.setCountry_id(111);
        testRegion.setCity_id(222);
        testRegion.setId(99999);
        regionRepository.create(testRegion);
        Optional<Region> test3 = regionRepository.findById(99999);
        Assertions.assertFalse(test3.isEmpty());
    }

    @Test
    void update() {
        Region testRegion = new Region();
        testRegion.setName("Москва!!");
        testRegion.setCountry_id(31599);
        testRegion.setCity_id(4312);
        testRegion.setId(99999);

        Region test = regionRepository.update(testRegion);
        Assertions.assertNotNull(test);
    }

    @Test
    void delete() {
        Region testRegion = new Region();
        testRegion.setName("deleteRegion");
        testRegion.setCountry_id(11);
        testRegion.setCity_id(22);
        testRegion.setId(33333);
        regionRepository.create(testRegion);

        Region test = regionRepository.delete(33333);
        Assertions.assertNotNull(test);
    }
}