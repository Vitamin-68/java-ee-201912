package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.crud.CrudRepoImpl;
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
    Path pathToWorkingCopy = Paths.get("src/test/resources/tmp.csv");

    CrudRepoImpl<City, Integer> cityIntegerCrudRepo = new CrudRepoImpl<>(pathToWorkingCopy, City.class);

    @SneakyThrows
    @BeforeEach
    void prepare() {
        Files.deleteIfExists(pathToWorkingCopy);
        Files.copy(pathToOrigin, pathToWorkingCopy);
    }

    @Test
    void findAll() {
        assertNotNull(cityIntegerCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Integer cityId = 10504604;
        Optional<City> city = cityIntegerCrudRepo.findById(cityId);
        City result = city.get();

        assertEquals(result.getId(), cityId);
    }

    @Test
    void findByIdNegative() {
        Integer cityId = 10504604;
        Optional<City> city = cityIntegerCrudRepo.findById(cityId);
        City result = city.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Байконур";
        Optional<List<City>> cities = cityIntegerCrudRepo.findByField(field, value);

        assertEquals(2, cities.get().size());
    }

    @Test
    void addCity() {
        City city = new City();
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        city.setId(id);

        city.setCountry_id(23);
        city.setRegion_id(123);
        city.setName("Impl");

        cityIntegerCrudRepo.create(city);

        Optional<City> expOpt = cityIntegerCrudRepo.findById(id);
        City expected = expOpt.get();

        assertEquals(expected.getName(), city.getName());
    }

    @Test
    void updateCity() {
        Integer cityId = 10504604;

        City city = new City();
        city.setId(cityId);
        city.setCountry_id(23);
        city.setRegion_id(123);
        city.setName("Impl");

        cityIntegerCrudRepo.update(city);

        Optional<City> expOpt = cityIntegerCrudRepo.findById(cityId);
        City expected = expOpt.get();

        assertTrue(expected.getName().equals("Impl"));
    }

    @Test
    void deleteCity() {
        Integer cityId = 10504604;

        assertTrue(cityIntegerCrudRepo.findById(cityId).isPresent());
        cityIntegerCrudRepo.delete(cityId);

        assertTrue(cityIntegerCrudRepo.findById(cityId).isEmpty());
    }
}
