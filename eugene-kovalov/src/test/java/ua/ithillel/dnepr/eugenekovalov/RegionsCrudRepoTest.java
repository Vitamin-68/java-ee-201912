package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.crud.CrudRepoImpl;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Region;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegionsCrudRepoTest {

    static Path pathToOrigin = Paths.get("src/main/resources/region.csv");
    static Path pathToWorkingCopy = Paths.get("src/test/resources/tmpRegion.csv");

    CrudRepoImpl<Region, Integer> regionIntegerCrudRepo = new CrudRepoImpl<>(pathToWorkingCopy, Region.class);

    @SneakyThrows
    @BeforeAll
    static void prepare() {
        clean();
        Files.copy(pathToOrigin, pathToWorkingCopy);
    }

    @SneakyThrows
    @AfterAll
    static void clean() {
        Files.deleteIfExists(pathToWorkingCopy);
    }

    @Test
    void findAll() {
        assertNotNull(regionIntegerCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Integer regionId = 277657;
        Optional<Region> region = regionIntegerCrudRepo.findById(regionId);
        Region result = region.get();

        assertEquals(result.getId(), regionId);
    }

    @Test
    void findByIdNegative() {
        Integer regionId = 277657;
        Optional<Region> region = regionIntegerCrudRepo.findById(regionId);
        Region result = region.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Великобритания";
        Optional<List<Region>> regions = regionIntegerCrudRepo.findByField(field, value);

        assertEquals(1, regions.get().size());
    }

    @Test
    void addRegion() {
        Region region = new Region();
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        region.setId(id);
        region.setCountry_id(23);
        region.setCity_id(123);
        region.setName("Impl");

        regionIntegerCrudRepo.create(region);

        assertEquals(regionIntegerCrudRepo.findById(id).get().getName(), region.getName());
    }

    @Test
    void updateRegion() {
        Integer regionId = 3223;

        Region region = new Region();
        region.setId(regionId);
        region.setCountry_id(23);
        region.setCity_id(123);
        region.setName("Impl");

        regionIntegerCrudRepo.update(region);

        assertTrue(regionIntegerCrudRepo.findById(regionId).get().getName().equals("Impl"));
    }

    @Test
    void deleteRegion() {
        Integer regionId = 3407;

        assertTrue(regionIntegerCrudRepo.findById(regionId).isPresent());
        regionIntegerCrudRepo.delete(regionId);

        assertFalse(regionIntegerCrudRepo.findById(regionId).isPresent());
    }
}
