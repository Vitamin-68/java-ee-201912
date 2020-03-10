package ua.ithillel.dnepr.dmytro.ostapenko.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.dmytro.ostapenko.repository.entity.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_CITY;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.FILE_PATH_RESOURCE;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.QUACK;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.TEMP_PATH;

@Slf4j
class CityCrudRepositoryTest {
    private static CityCrudRepository cityCrudRepository;
    private static City testCity;
    private static String tempFullPath;

    @BeforeAll
    static void setUp() throws IOException {
        tempFullPath = TEMP_PATH + FILE_CITY;
        if (Files.exists(Path.of(tempFullPath))) {
            tearDown();
        }
        Files.copy(Paths.get(FILE_PATH_RESOURCE + FILE_CITY), Paths.get(tempFullPath));
        cityCrudRepository = new CityCrudRepository(tempFullPath);
        testCity = new City(4340, 3159, 4312, "Дедовск");
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.delete(Paths.get(tempFullPath));
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(cityCrudRepository.findAll());
    }

    @Test
    void findById() {
        City realInputTest = cityCrudRepository.findById(4340).get();
        assertEquals(realInputTest.getCountryId(), testCity.getCountryId());
        Optional<City> wrongInputTest = cityCrudRepository.findById(-1);
        assertEquals(Optional.empty(), wrongInputTest);
    }

    @Test
    void findByField() {
        Optional<List<City>> realInputTest = cityCrudRepository.findByField("name", "Дедовск");
        Assertions.assertEquals(testCity.getCityId(), realInputTest.get().get(0).getCityId());
        Assertions.assertEquals(testCity.getCityName(), realInputTest.get().get(0).getCityName());
        Optional<List<City>> realInputTest2 = cityCrudRepository.findByField("city_id", "4340");
        Assertions.assertEquals(testCity.getRegionId(), realInputTest2.get().get(0).getRegionId());
        Assertions.assertEquals(testCity.getCityName(), realInputTest2.get().get(0).getCityName());
        Optional<List<City>> wrongInputTest = cityCrudRepository.findByField(QUACK, QUACK);
        Assertions.assertTrue(wrongInputTest.isEmpty());
        Optional<List<City>> wrongInputTest2 = cityCrudRepository.findByField("city_id", QUACK);
        Assertions.assertTrue(wrongInputTest2.isEmpty());
    }

    @Test
    void create() {
        City newCity = new City(98765432, 9876543, 8765432, QUACK);
        cityCrudRepository.create(newCity);
        Optional<City> newCreatedCity = cityCrudRepository.findById(98765432);
        Assertions.assertEquals(newCity.getCityId(), newCreatedCity.get().getCityId());
        Assertions.assertEquals(newCity.getCountryId(), newCreatedCity.get().getCountryId());
        Assertions.assertEquals(newCity.getRegionId(), newCreatedCity.get().getRegionId());
        Assertions.assertEquals(newCity.getCityName(), newCreatedCity.get().getCityName());
    }

    @Test
    void update() {
        City newCity = cityCrudRepository.findById(4400).get();
        newCity.setCityName(QUACK);
        cityCrudRepository.update(newCity);
        Assertions.assertEquals(newCity.getCityName(), cityCrudRepository.findById(4400).get().getCityName());
    }

    @Test
    void delete() {
        cityCrudRepository.delete(6932);
        Assertions.assertTrue(cityCrudRepository.findById(6932).isEmpty());
    }
}