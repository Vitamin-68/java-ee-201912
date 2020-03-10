package ua.ithillel.dnepr.dmytro.ostapenko.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dmytro.ostapenko.repository.entity.Country;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_COUNTRY;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_PATH_RESOURCE;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.QUACK;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.TEMP_PATH;

@Slf4j
class CountryCrudRepositoryTest {
    private static CountryCrudRepository countryCrudRepository;
    private static Country testCountry;
    private static String tempFullPath;

    @BeforeAll
    static void setUp() throws IOException {
        tempFullPath = TEMP_PATH + FILE_COUNTRY;
        if (Files.exists(Path.of(tempFullPath))) {
            tearDown();
        }
        Files.copy(Paths.get(FILE_PATH_RESOURCE + FILE_COUNTRY), Paths.get(tempFullPath));
        countryCrudRepository = new CountryCrudRepository(tempFullPath);
        testCountry = new Country(9908, 0, "Украина");
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.delete(Paths.get(tempFullPath));
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(countryCrudRepository.findAll());
    }

    @Test
    void findById() {
        Country realInputTest = countryCrudRepository.findById(9908).get();
        assertEquals(realInputTest.getCountryId(), testCountry.getCountryId());
        Optional<Country> wrongInputTest = countryCrudRepository.findById(-1);
        assertEquals(Optional.empty(), wrongInputTest);
    }

    @Test
    void findByField() {
        Optional<List<Country>> realInputTest = countryCrudRepository.findByField("name", "Украина");
        Assertions.assertEquals(testCountry.getCountryId(), realInputTest.get().get(0).getCountryId());
        Assertions.assertEquals(testCountry.getCountryName(), realInputTest.get().get(0).getCountryName());
        Optional<List<Country>> realInputTest2 = countryCrudRepository.findByField("country_id", "9908");
        Assertions.assertEquals(testCountry.getCountryId(), realInputTest2.get().get(0).getCountryId());
        Assertions.assertEquals(testCountry.getCountryName(), realInputTest2.get().get(0).getCountryName());
        Optional<List<Country>> wrongInputTest = countryCrudRepository.findByField(QUACK, QUACK);
        Assertions.assertTrue(wrongInputTest.isEmpty());
        Optional<List<Country>> wrongInputTest2 = countryCrudRepository.findByField("country_id", QUACK);
        Assertions.assertTrue(wrongInputTest2.isEmpty());
    }

    @Test
    void create() {
        Country newCountry = new Country(98765432, 0, QUACK);
        countryCrudRepository.create(newCountry);
        Optional<Country> newCreatedCountry = countryCrudRepository.findById(98765432);
        Assertions.assertEquals(newCountry.getCountryId(), newCreatedCountry.get().getCountryId());
        Assertions.assertEquals(newCountry.getCityId(), newCreatedCountry.get().getCityId());
        Assertions.assertEquals(newCountry.getCountryName(), newCreatedCountry.get().getCountryName());
    }

    @Test
    void update() {
        Country newCountry = countryCrudRepository.findById(1007).get();
        newCountry.setCountryName(QUACK);
        countryCrudRepository.update(newCountry);
        Assertions.assertEquals(newCountry.getCountryName(), countryCrudRepository.findById(1007).get().getCountryName());
    }

    @Test
    void delete() {
        countryCrudRepository.delete(3159);
        Assertions.assertTrue(countryCrudRepository.findById(3159).isEmpty());
    }
}