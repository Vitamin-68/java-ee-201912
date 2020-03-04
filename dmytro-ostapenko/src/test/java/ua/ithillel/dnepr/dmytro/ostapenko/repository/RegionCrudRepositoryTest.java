package ua.ithillel.dnepr.dmytro.ostapenko.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dmytro.ostapenko.repository.entity.Region;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_CITY;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_PATH_RESOURCE;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_REGION;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.QUACK;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.TEMP_PATH;

@Slf4j
class RegionCrudRepositoryTest {
    private static RegionCrudRepository regionCrudRepository;
    private static Region testRegion;
    private static String tempFullPath;

    @BeforeAll
    static void setUp() throws IOException {
        tempFullPath = TEMP_PATH + FILE_CITY;
        if (Files.exists(Path.of(tempFullPath))) {
            tearDown();
        }
        Files.copy(Paths.get(FILE_PATH_RESOURCE + FILE_REGION), Paths.get(tempFullPath));
        regionCrudRepository = new RegionCrudRepository(tempFullPath);
        testRegion = new Region(3407, 3159, 0, "Бурятия");
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.delete(Paths.get(tempFullPath));
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(regionCrudRepository.findAll());
    }

    @Test
    void findById() {
        Region realInputTest = regionCrudRepository.findById(3407).get();
        assertEquals(realInputTest.getCountryId(), testRegion.getCountryId());
        Optional<Region> wrongInputTest = regionCrudRepository.findById(-1);
        assertEquals(Optional.empty(), wrongInputTest);
    }

    @Test
    void findByField() {
        Optional<List<Region>> realInputTest = regionCrudRepository.findByField("name", "Бурятия");
        Assertions.assertEquals(testRegion.getRegionId(), realInputTest.get().get(0).getRegionId());
        Assertions.assertEquals(testRegion.getRegionName(), realInputTest.get().get(0).getRegionName());
        Optional<List<Region>> realInputTest2 = regionCrudRepository.findByField("region_id", "3407");
        Assertions.assertEquals(testRegion.getRegionId(), realInputTest2.get().get(0).getRegionId());
        Assertions.assertEquals(testRegion.getRegionName(), realInputTest2.get().get(0).getRegionName());
        Optional<List<Region>> wrongInputTest = regionCrudRepository.findByField(QUACK, QUACK);
        Assertions.assertTrue(wrongInputTest.isEmpty());
        Optional<List<Region>> wrongInputTest2 = regionCrudRepository.findByField("region_id", QUACK);
        Assertions.assertTrue(wrongInputTest2.isEmpty());
    }

    @Test
    void create() {
        Region newCity = new Region(98765432, 9876543, 8765432, QUACK);
        regionCrudRepository.create(newCity);
        Optional<Region> newCreatedCity = regionCrudRepository.findById(98765432);
        Assertions.assertEquals(newCity.getRegionId(), newCreatedCity.get().getRegionId());
        Assertions.assertEquals(newCity.getCountryId(), newCreatedCity.get().getCountryId());
        Assertions.assertEquals(newCity.getCityId(), newCreatedCity.get().getCityId());
        Assertions.assertEquals(newCity.getRegionName(), newCreatedCity.get().getRegionName());
    }

    @Test
    void update() {
        Region newCity = regionCrudRepository.findById(4312).get();
        newCity.setRegionName(QUACK);
        regionCrudRepository.update(newCity);
        Assertions.assertEquals(newCity.getRegionName(), regionCrudRepository.findById(4312).get().getRegionName());
    }

    @Test
    void delete() {
        regionCrudRepository.delete(9778);
        Assertions.assertTrue(regionCrudRepository.findById(9778).isEmpty());
    }
}