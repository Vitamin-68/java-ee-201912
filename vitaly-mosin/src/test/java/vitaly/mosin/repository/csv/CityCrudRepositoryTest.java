package vitaly.mosin.repository.csv;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vitaly.mosin.repository.entity.City;
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
import static vitaly.mosin.repository.Constants.CITY_ID;
import static vitaly.mosin.repository.Constants.COUNTRY_ID;
import static vitaly.mosin.repository.Constants.FILE_CITY;
import static vitaly.mosin.repository.Constants.FILE_COUNTRY;
import static vitaly.mosin.repository.Constants.FILE_PATH_RESOURCE;
import static vitaly.mosin.repository.Constants.FILE_PATH_TMP;
import static vitaly.mosin.repository.Constants.FILE_REGION;
import static vitaly.mosin.repository.Constants.HEAD_NAME;
import static vitaly.mosin.repository.Constants.REGION_ID;

@Slf4j
class CityCrudRepositoryTest {

    private CityCrudRepository cityCrudRepository;
    private City realCityFromDB;


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

        cityCrudRepository = new CityCrudRepository(FILE_PATH_TMP + FILE_CITY);
        realCityFromDB = new City(4314, 3159, 4312, "Алабино");
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = cityCrudRepository.findAll();
        assertTrue(cities.isPresent());
    }

    @Test
    void findById() {
        Optional<City> cityTest = cityCrudRepository.findById(4313);
        assertEquals(4313, cityTest.get().getId());
        cityTest = cityCrudRepository.findById(-1);
        assertEquals(Optional.empty(), cityTest);
    }

    @Test
    void findByField() {
        Optional<List<City>> result = cityCrudRepository.findByField(CITY_ID, realCityFromDB.getId());
        assertEquals(1, result.get().size());
        assertEquals(realCityFromDB.getId(), result.get().get(0).getId());
        assertEquals(realCityFromDB.getCountryId(), result.get().get(0).getCountryId());
        assertEquals(realCityFromDB.getRegionId(), result.get().get(0).getRegionId());
        assertEquals(realCityFromDB.getName(), result.get().get(0).getName());

        result = cityCrudRepository.findByField(HEAD_NAME, realCityFromDB.getName());
        assertEquals(1, result.get().size());
        assertEquals(realCityFromDB.getId(), result.get().get(0).getId());
        assertEquals(realCityFromDB.getCountryId(), result.get().get(0).getCountryId());
        assertEquals(realCityFromDB.getRegionId(), result.get().get(0).getRegionId());
        assertEquals(realCityFromDB.getName(), result.get().get(0).getName());

        result = cityCrudRepository.findByField(COUNTRY_ID, realCityFromDB.getCountryId());
        assertTrue(result.get().size() > 1);
        result = cityCrudRepository.findByField(REGION_ID, realCityFromDB.getRegionId());
        assertTrue(result.get().size() > 1);

        //seek not existing Id
        result = cityCrudRepository.findByField(CITY_ID, -1);
        assertEquals(Optional.empty(), result);

        result = cityCrudRepository.findByField("not_exist_field", "none");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void create() {
        int sizeDbBeforeCreate = cityCrudRepository.findAll().get().size();
        City testCity = new City(999999999, 456, 789, "TestCityName");
        City result = cityCrudRepository.create(testCity);
        assertEquals(testCity.getName(), result.getName());
        assertEquals(testCity.getRegionId(), result.getRegionId());
        assertEquals(testCity.getCountryId(), result.getCountryId());
        assertEquals(testCity.getId(), result.getId());

        int sizeDbAfterCreate = cityCrudRepository.findAll().get().size();
        assertEquals(1, sizeDbAfterCreate - sizeDbBeforeCreate);

        //create existing object
        assertThrows(MyRepoException.class, () -> cityCrudRepository.create(realCityFromDB));
    }

    @Test
    void update() {
        City testCity = new City(realCityFromDB.getId(), realCityFromDB.getCountryId() + 1, realCityFromDB.getRegionId() + 1, "CityNewName");
        int sizeDbBeforeUpdate = cityCrudRepository.findAll().get().size();
        City result = cityCrudRepository.update(testCity);
        int sizeDbAfterUpdate = cityCrudRepository.findAll().get().size();

        assertNotNull(result);
        assertEquals(sizeDbBeforeUpdate, sizeDbAfterUpdate);
        assertEquals(realCityFromDB.getId(), result.getId());
        assertEquals(1, result.getCountryId() - realCityFromDB.getCountryId());
        assertEquals(1, result.getRegionId() - realCityFromDB.getRegionId());
        assertEquals("CityNewName", result.getName());

        //test wrong ID
        assertThrows(MyRepoException.class, () -> cityCrudRepository.update(new City(999999999,
                realCityFromDB.getCountryId() + 1, realCityFromDB.getRegionId() + 1, "CityNewName")));
    }

    @Test
    void delete() {
        int sizeDbBeforeDelete = cityCrudRepository.findAll().get().size();
        City result = cityCrudRepository.delete(realCityFromDB.getId());
        int sizeDbAfterDelete = cityCrudRepository.findAll().get().size();

        assertEquals(result.getId(), realCityFromDB.getId());
        assertEquals(result.getCountryId(), realCityFromDB.getCountryId());
        assertEquals(result.getRegionId(), realCityFromDB.getRegionId());
        assertEquals(result.getName(), realCityFromDB.getName());
        assertEquals(1, sizeDbBeforeDelete - sizeDbAfterDelete);

        //test delete wrong id
        assertThrows(MyRepoException.class, () -> cityCrudRepository.delete(99999999));

    }
}