package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.CityCrudRepo;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.City;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class CityCrudRepoTest {

    Path pathToOrigin = Paths.get("src/main/resources/city.csv");
    Path pathToWorkingCopy = Paths.get("src/main/resources/tmp.csv");

    CityCrudRepo cityCrudRepo = new CityCrudRepo(pathToWorkingCopy, ';');

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
        assertNotNull(cityCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Integer cityId = 10504604;
        Optional<City> city = cityCrudRepo.findById(cityId);
        City result = city.get();

        assertEquals(result.getId(), cityId);
    }

    @Test
    void findByIdNegative() {
        Integer cityId = 10504604;
        Optional<City> city = cityCrudRepo.findById(cityId);
        City result = city.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Байконур";
        Optional<List<City>> cities = cityCrudRepo.findByField(field, value);

        assertEquals(2, cities.get().size());
    }

    @Test
    void addCity() {
        City city = new City();
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        city.setId(id);
        city.setCountryId(23);
        city.setRegionId(123);
        city.setName("Impl");

        cityCrudRepo.create(city);

        assertEquals(cityCrudRepo.findById(id).get().getName(), city.getName());
    }

    @Test
    void updateCity() {
        Integer cityId = 10504604;

        City city = new City();
        city.setId(cityId);
        city.setCountryId(23);
        city.setRegionId(123);
        city.setName("Impl");

        cityCrudRepo.update(city);

        assertTrue(cityCrudRepo.findById(cityId).get().getName().equals("Impl"));
    }

    @Test
    void deleteCity() {
        Integer cityId = 10504604;

        assertTrue(cityCrudRepo.findById(cityId).isPresent());
        cityCrudRepo.delete(cityId);

        assertFalse(cityCrudRepo.findById(cityId).isPresent());
    }
}
