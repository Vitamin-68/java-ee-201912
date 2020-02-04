package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.crud.CrudRepoImpl;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.City;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CityCrudRepoTest {

    static Path pathToWorkingCopy = null;
    static CrudRepoImpl<City, Integer> cityIntegerCrudRepo;

    @SneakyThrows
    @BeforeAll
    static void prepare() {
        pathToWorkingCopy = Files.createTempFile("tmpCityK", ".csv");
        cityIntegerCrudRepo = new CrudRepoImpl<>(pathToWorkingCopy, City.class);

        cityIntegerCrudRepo.create(createCity(1, 1, 1, "Dunwich"));
        cityIntegerCrudRepo.create(createCity(2, 3, 6, "Dunwich"));
        cityIntegerCrudRepo.create(createCity(423, 4, 7, "Darn"));
        cityIntegerCrudRepo.create(createCity(777, 24, 71, "Maluoka"));
    }

    @SneakyThrows
    @AfterAll
    static void clean() {
        if (pathToWorkingCopy != null) {
            Files.delete(pathToWorkingCopy);
            pathToWorkingCopy.toFile().deleteOnExit();
        }
    }

    @Test
    void findAll() {
        assertNotNull(cityIntegerCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Optional<City> city = cityIntegerCrudRepo.findById(423);
        City result = city.get();

        assertEquals(result.getId(), 423);
    }

    @Test
    void findByIdNegative() {
        Optional<City> city = cityIntegerCrudRepo.findById(423);
        City result = city.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Dunwich";
        Optional<List<City>> cities = cityIntegerCrudRepo.findByField(field, value);

        assertEquals(2, cities.get().size());
    }

    @Test
    void addCity() {
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

        cityIntegerCrudRepo.create(createCity(id, 10, 10, "Malcolm"));

        Optional<City> expOpt = cityIntegerCrudRepo.findById(id);
        City actual =  cityIntegerCrudRepo.findById(id).get();
        City expected = expOpt.get();

        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void updateCity() {
        City city = cityIntegerCrudRepo.findById(423).get();
        city.setName("San-Diego");
        cityIntegerCrudRepo.update(city);

        assertTrue(cityIntegerCrudRepo.findById(423).get().getName().equals("San-Diego"));
    }

    @Test
    void deleteCity() {
        Integer cityId = 777;

        assertTrue(cityIntegerCrudRepo.findById(cityId).isPresent());
        cityIntegerCrudRepo.delete(cityId);

        assertTrue(cityIntegerCrudRepo.findById(cityId).isEmpty());
    }

    private static City createCity(int id, int regionId, int countryId, String name) {
        City city = new City();
        city.setId(id);
        city.setRegion_id(regionId);
        city.setCountry_id(countryId);
        city.setName(name);
        return city;
    }
}
