package vitaly.mosin.repository.csv;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vitaly.mosin.repository.entity.Country;
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
import static vitaly.mosin.repository.Constants.COUNTRY_ID;
import static vitaly.mosin.repository.Constants.FILE_CITY;
import static vitaly.mosin.repository.Constants.FILE_COUNTRY;
import static vitaly.mosin.repository.Constants.FILE_PATH_RESOURCE;
import static vitaly.mosin.repository.Constants.FILE_PATH_TMP;
import static vitaly.mosin.repository.Constants.FILE_REGION;
import static vitaly.mosin.repository.Constants.HEAD_NAME;

@Slf4j
class CountryCrudRepositoryTest {
    private CountryCrudRepository countryCrudRepository;
    private Country realCountryFromDB;

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

        countryCrudRepository = new CountryCrudRepository(FILE_PATH_TMP + FILE_COUNTRY);
        realCountryFromDB = new Country(3159, "Россия");
    }

    @Test
    void findAll() {
        Optional<List<Country>> countries = countryCrudRepository.findAll();
        assertTrue(countries.isPresent());
    }

    @Test
    void findById() {
        Optional<Country> countryTest = countryCrudRepository.findById(3159);
        assertEquals(3159, countryTest.get().getId());

        //test wrong id
        countryTest = countryCrudRepository.findById(-1);
        assertEquals(Optional.empty(), countryTest);
    }

    @Test
    void findByField() {
        Optional<List<Country>> result = countryCrudRepository.findByField(COUNTRY_ID, realCountryFromDB.getId());
        assertEquals(1, result.get().size());
        assertEquals(realCountryFromDB.getId(), result.get().get(0).getId());
        assertEquals(realCountryFromDB.getName(), result.get().get(0).getName());

        result = countryCrudRepository.findByField(HEAD_NAME, realCountryFromDB.getName());
        assertEquals(1, result.get().size());
        assertEquals(realCountryFromDB.getId(), result.get().get(0).getId());
        assertEquals(realCountryFromDB.getName(), result.get().get(0).getName());

        //seek not existing Id
        result = countryCrudRepository.findByField(COUNTRY_ID, -1);
        assertEquals(Optional.empty(), result);

        result = countryCrudRepository.findByField("not_exist_field", "none");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void create() {
        int sizeDbBeforeCreate = countryCrudRepository.findAll().get().size();
        Country testCountry = new Country(999999999, "TestCountryName");
        Country result = countryCrudRepository.create(testCountry);
        assertEquals(testCountry.getName(), result.getName());
        assertEquals(testCountry.getId(), result.getId());

        int sizeDbAfterCreate = countryCrudRepository.findAll().get().size();
        assertEquals(1, sizeDbAfterCreate - sizeDbBeforeCreate);

        //create existing object
        assertThrows(MyRepoException.class, () -> countryCrudRepository.create(realCountryFromDB));
    }

    @Test
    void update() {
        Country testCountry = new Country(realCountryFromDB.getId(), "CountryNewName");
        int sizeDbBeforeUpdate = countryCrudRepository.findAll().get().size();
        Country result = countryCrudRepository.update(testCountry);
        int sizeDbAfterUpdate = countryCrudRepository.findAll().get().size();

        assertNotNull(result);
        assertEquals(sizeDbBeforeUpdate, sizeDbAfterUpdate);
        assertEquals(realCountryFromDB.getId(), result.getId());
        assertEquals("CountryNewName", result.getName());

        //test wrong ID
        assertThrows(MyRepoException.class, () -> countryCrudRepository.update(new Country(999999999, "CountryNewName")));
    }

    @Test
    void delete() {
        //country has cities or regions
        assertThrows(MyRepoException.class, () -> countryCrudRepository.delete(realCountryFromDB.getId()));

        //country hasn't cities or regions
        Country testCountryNoCitiesAndRegions = new Country(888888888, "TestCountryName");
        countryCrudRepository.create(testCountryNoCitiesAndRegions);
        int sizeDbBeforeDelete = countryCrudRepository.findAll().get().size();
        Country result = countryCrudRepository.delete(testCountryNoCitiesAndRegions.getId());
        int sizeDbAfterDelete = countryCrudRepository.findAll().get().size();

        assertEquals(result.getId(), testCountryNoCitiesAndRegions.getId());
        assertEquals(result.getName(), testCountryNoCitiesAndRegions.getName());
        assertEquals(1, sizeDbBeforeDelete - sizeDbAfterDelete);

        //test wrong ID
        assertThrows(MyRepoException.class, () -> countryCrudRepository.delete(99999999));

    }
}