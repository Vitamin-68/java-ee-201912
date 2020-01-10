package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class RegionCrudRepositoryTest {
    static File regionFile;
    static private CrudRepository<Region, Integer> regionRepository;

    @BeforeAll
    static void setUp() {
        regionFile = createTempFile("region.csv");

        if (regionFile != null) {
            regionRepository = new CrudRepositoryImp<Region, Integer>(regionFile.getPath(), Region.class);
        }
    }

    @Test
    void findAll() {
        Optional<List<Region>> cities = regionRepository.findAll();
        assertFalse(cities.get().isEmpty());
    }

    @Test
    void findById() {
        Optional<Region> test = regionRepository.findById(4312);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<Region>> test = regionRepository.findByField("name", "Адыгея");
        Assertions.assertFalse(test.get().isEmpty());
        Optional<List<Region>> test2 = regionRepository.findByField("name", "some string name");
        Assertions.assertTrue(test2.get().isEmpty());
        Optional<List<Region>> test3 = regionRepository.findByField("1name1", 99999);
        Assertions.assertTrue(test3.get().isEmpty());
    }

    @Test
    void create() {
        Region testRegion = new Region();
        testRegion.setName("testName");
        testRegion.setCountry_id(111);
        testRegion.setCity_id(0);
        testRegion.setId(99999);
        regionRepository.create(testRegion);
        Optional<List<Region>> test3 = regionRepository.findByField("name", "testName");
        Assertions.assertFalse(test3.get().isEmpty());
    }

    @Test
    void update() {
        Region testRegion = new Region();
        testRegion.setName("Москва и обл");
        testRegion.setCountry_id(31599);
        testRegion.setCity_id(0);
        testRegion.setId(4312);

        Region test = regionRepository.update(testRegion);
        Assertions.assertNotNull(test);
    }

    @Test
    void delete() {
        Region test = regionRepository.delete(3160);
        Assertions.assertNotNull(test);
    }

    private static File createTempFile(String resourcePath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile("tmp", resourcePath);
            //tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            log.debug("tmp file path: "+tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            log.error("getResourceAsFile exception",e);
            return null;
        }
    }

}