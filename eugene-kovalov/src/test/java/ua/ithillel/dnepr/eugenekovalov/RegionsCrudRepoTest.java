package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.crud.CrudRepoImpl;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Region;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegionsCrudRepoTest {
    static Path pathToWorkingCopy = null;
    static CrudRepoImpl<Region, Integer> regionIntegerCrudRepo;

    @SneakyThrows
    @BeforeAll
    static void prepare() {
        pathToWorkingCopy = Files.createTempFile("tmpRegionK", ".csv");
        regionIntegerCrudRepo = new CrudRepoImpl<>(pathToWorkingCopy, Region.class);

        regionIntegerCrudRepo.create(createRegion(1, 1, 5, "Dunwich"));
        regionIntegerCrudRepo.create(createRegion(2, 6, 2, "Dunwich"));
        regionIntegerCrudRepo.create(createRegion(423, 7, 6, "Darn"));
        regionIntegerCrudRepo.create(createRegion(777, 24, 8, "Maluoka"));
    }

    @SneakyThrows
    @AfterAll
    static void clean() {
        if (pathToWorkingCopy != null) {
            pathToWorkingCopy.toFile().deleteOnExit();
        }
    }

    @Test
    void findAll() {
        assertNotNull(regionIntegerCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Optional<Region> region = regionIntegerCrudRepo.findById(423);
        Region result = region.get();

        assertEquals(result.getId(), 423);
    }

    @Test
    void findByIdNegative() {
        Optional<Region> region = regionIntegerCrudRepo.findById(423);
        Region result = region.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Dunwich";
        Optional<List<Region>> regions = regionIntegerCrudRepo.findByField(field, value);

        assertEquals(2, regions.get().size());
    }

    @Test
    void addCity() {
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

        regionIntegerCrudRepo.create(createRegion(id, 4,10, "Malcolm"));

        Optional<Region> expOpt = regionIntegerCrudRepo.findById(id);
        Region actual = regionIntegerCrudRepo.findById(id).get();
        Region expected = expOpt.get();

        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void updateCity() {
        Region region = regionIntegerCrudRepo.findById(423).get();
        region.setName("San-Diego");
        regionIntegerCrudRepo.update(region);

        assertTrue(regionIntegerCrudRepo.findById(423).get().getName().equals("San-Diego"));
    }

    @Test
    void deleteCity() {
        Integer regionId = 777;

        assertTrue(regionIntegerCrudRepo.findById(regionId).isPresent());
        regionIntegerCrudRepo.delete(regionId);

        assertTrue(regionIntegerCrudRepo.findById(regionId).isEmpty());
    }

    private static Region createRegion(int id, int countryId, int cityId, String name) {
        Region region = new Region();
        region.setId(id);
        region.setCountry_id(countryId);
        region.setCity_id(cityId);
        region.setName(name);
        return region;
    }
}