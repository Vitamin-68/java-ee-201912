package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.RegionCrudRepo;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Region;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class RegionsCrudRepoTest {

    Path pathToOrigin = Paths.get("src/main/resources/region.csv");
    Path pathToWorkingCopy = Paths.get("src/main/resources/tmp.csv");

    RegionCrudRepo regionCrudRepo = new RegionCrudRepo(pathToWorkingCopy, ';');

    @SneakyThrows
    @BeforeEach
    void prepare() {
        Files.copy(pathToOrigin, pathToWorkingCopy);
    }

    @SneakyThrows
    @AfterEach
    void clean() {
        Files.deleteIfExists(pathToWorkingCopy);
    }

    @Test
    void findAll() {
        assertNotNull(regionCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Integer regionId = 277657;
        Optional<Region> region = regionCrudRepo.findById(regionId);
        Region result = region.get();

        assertEquals(result.getId(), regionId);
    }

    @Test
    void findByIdNegative() {
        Integer regionId = 277657;
        Optional<Region> region = regionCrudRepo.findById(regionId);
        Region result = region.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Великобритания";
        Optional<List<Region>> regions = regionCrudRepo.findByField(field, value);

        assertEquals(1, regions.get().size());
    }

    @Test
    void addCity() {
        Region region = new Region();
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        region.setId(id);
        region.setCountryId(23);
        region.setCityId(123);
        region.setName("Impl");

        regionCrudRepo.create(region);

        assertEquals(regionCrudRepo.findById(id).get().getName(), region.getName());
    }

    @Test
    void updateCity() {
        Integer regionId = 277657;

        Region region = new Region();
        region.setId(regionId);
        region.setCountryId(23);
        region.setCityId(123);
        region.setName("Impl");

        regionCrudRepo.update(region);

        assertTrue(regionCrudRepo.findById(regionId).get().getName().equals("Impl"));
    }

    @Test
    void deleteCity() {
        Integer regionId = 277657;

        assertTrue(regionCrudRepo.findById(regionId).isPresent());
        regionCrudRepo.delete(regionId);

        assertFalse(regionCrudRepo.findById(regionId).isPresent());
    }
}
