package vitaly.mosin.repository.csv;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vitaly.mosin.repository.entity.Region;
import vitaly.mosin.repository.exceptions.MyRepoException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vitaly.mosin.repository.Constants.FILE_CITY;
import static vitaly.mosin.repository.Constants.FILE_COUNTRY;
import static vitaly.mosin.repository.Constants.FILE_PATH_RESOURCE;
import static vitaly.mosin.repository.Constants.FILE_PATH_TMP;
import static vitaly.mosin.repository.Constants.FILE_REGION;
import static vitaly.mosin.repository.Constants.HEAD_NAME;
import static vitaly.mosin.repository.Constants.REGION_ID;

@Slf4j
class RegionCrudRepositoryTest {

    private RegionCrudRepository regionCrudRepository;
    private Region realRegionFromDB;

    @BeforeEach
    void setUp() {
        File folder = new File(FILE_PATH_TMP);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String[] listFiles = {FILE_COUNTRY, FILE_REGION, FILE_CITY};
        try {
            for (String listFile : listFiles) {
                Files.copy(new File(FILE_PATH_RESOURCE + listFile).toPath(),
                        new File(FILE_PATH_TMP + listFile).toPath(), REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.error("Failed to copy files", e);
        }

        regionCrudRepository = new RegionCrudRepository(FILE_PATH_TMP + FILE_REGION);
        realRegionFromDB = new Region(3407, 3159, "Бурятия");
    }

    @Test
    void findAll() {
        Optional<List<Region>> regions = regionCrudRepository.findAll();
        assertTrue(regions.isPresent());
    }

    @Test
    void findById() {
        Optional<Region> regionTest = regionCrudRepository.findById(3407);
        assertEquals(3407, regionTest.get().getId());

        //test wrong id
        regionTest = regionCrudRepository.findById(-1);
        assertEquals(Optional.empty(), regionTest);
    }

    @Test
    void findByField() {
        Optional<List<Region>> result = regionCrudRepository.findByField(REGION_ID, realRegionFromDB.getId());
        assertEquals(1, result.get().size());
        assertEquals(realRegionFromDB.getId(), result.get().get(0).getId());
        assertEquals(realRegionFromDB.getName(), result.get().get(0).getName());

        result = regionCrudRepository.findByField(HEAD_NAME, realRegionFromDB.getName());
        assertEquals(1, result.get().size());
        assertEquals(realRegionFromDB.getId(), result.get().get(0).getId());
        assertEquals(realRegionFromDB.getName(), result.get().get(0).getName());

        //seek not existing Id
        result = regionCrudRepository.findByField(REGION_ID, -1);
        assertEquals(Optional.empty(), result);

        result = regionCrudRepository.findByField("not_exist_field", "none");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void create() {
        int sizeDbBeforeCreate = regionCrudRepository.findAll().get().size();
        Region testRegion = new Region(999999999, 1234, "TestRegionName");
        Region result = regionCrudRepository.create(testRegion);
        assertEquals(testRegion.getName(), result.getName());
        assertEquals(testRegion.getId(), result.getId());

        int sizeDbAfterCreate = regionCrudRepository.findAll().get().size();
        assertEquals(1, sizeDbAfterCreate - sizeDbBeforeCreate);

        //create existing object
        assertThrows(MyRepoException.class, () -> regionCrudRepository.create(realRegionFromDB));
    }

    @Test
    void update() {
        Region testRegion = new Region(realRegionFromDB.getId(), realRegionFromDB.getCountryId(), "RegionNewName");
        int sizeDbBeforeUpdate = regionCrudRepository.findAll().get().size();
        Region result = regionCrudRepository.update(testRegion);
        int sizeDbAfterUpdate = regionCrudRepository.findAll().get().size();

        assertNotNull(result);
        assertEquals(sizeDbBeforeUpdate, sizeDbAfterUpdate);
        assertEquals(realRegionFromDB.getId(), result.getId());
        assertEquals("RegionNewName", result.getName());

        //test wrong ID
        assertThrows(MyRepoException.class, () -> regionCrudRepository.update(new Region(999999999, 1234, "RegionNewName")));
    }

    @Test
    void delete() {
        //region has cities
        assertThrows(MyRepoException.class, () -> regionCrudRepository.delete(realRegionFromDB.getId()));

        //region hasn't cities
        Region testRegionNoCitiesAndRegions = new Region(888888888, 88888888, "TestRegionName");
        regionCrudRepository.create(testRegionNoCitiesAndRegions);
        int sizeDbBeforeDelete = regionCrudRepository.findAll().get().size();
        Region result = regionCrudRepository.delete(testRegionNoCitiesAndRegions.getId());
        int sizeDbAfterDelete = regionCrudRepository.findAll().get().size();

        assertEquals(result.getId(), testRegionNoCitiesAndRegions.getId());
        assertEquals(result.getName(), testRegionNoCitiesAndRegions.getName());
        assertEquals(1, sizeDbBeforeDelete - sizeDbAfterDelete);

        //test wrong ID
        assertThrows(MyRepoException.class, () -> regionCrudRepository.delete(99999999));

    }
}