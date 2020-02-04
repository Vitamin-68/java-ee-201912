package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.crud.CrudRepoImpl;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Country;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountryCrudRepoTest {
    static Path pathToWorkingCopy = null;
    static CrudRepoImpl<Country, Integer> countryIntegerCrudRepo;

    @SneakyThrows
    @BeforeAll
    static void prepare() {
        pathToWorkingCopy = Files.createTempFile("tmpCountryK", ".csv");
        countryIntegerCrudRepo = new CrudRepoImpl<>(pathToWorkingCopy, Country.class);

        countryIntegerCrudRepo.create(createCountry(1,  1, "Dunwich"));
        countryIntegerCrudRepo.create(createCountry(2, 6, "Dunwich"));
        countryIntegerCrudRepo.create(createCountry(423, 7, "Darn"));
        countryIntegerCrudRepo.create(createCountry(777, 24, "Maluoka"));
    }

    @SneakyThrows
    @AfterAll
    static void clean() {
        if (pathToWorkingCopy != null) {
            Files.deleteIfExists(pathToWorkingCopy);
            pathToWorkingCopy.toFile().deleteOnExit();
        }
    }

    @Test
    void findAll() {
        assertNotNull(countryIntegerCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Optional<Country> country = countryIntegerCrudRepo.findById(423);
        Country result = country.get();

        assertEquals(result.getId(), 423);
    }

    @Test
    void findByIdNegative() {
        Optional<Country> country = countryIntegerCrudRepo.findById(423);
        Country result = country.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Dunwich";
        Optional<List<Country>> countries = countryIntegerCrudRepo.findByField(field, value);

        assertEquals(2, countries.get().size());
    }

    @Test
    void addCity() {
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

        countryIntegerCrudRepo.create(createCountry(id, 10, "Malcolm"));

        Optional<Country> expOpt = countryIntegerCrudRepo.findById(id);
        Country actual =  countryIntegerCrudRepo.findById(id).get();
        Country expected = expOpt.get();

        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void updateCity() {
        Country country = countryIntegerCrudRepo.findById(423).get();
        country.setName("San-Diego");
        countryIntegerCrudRepo.update(country);

        assertTrue(countryIntegerCrudRepo.findById(423).get().getName().equals("San-Diego"));
    }

    @Test
    void deleteCity() {
        Integer countryId = 777;

        assertTrue(countryIntegerCrudRepo.findById(countryId).isPresent());
        countryIntegerCrudRepo.delete(countryId);

        assertTrue(countryIntegerCrudRepo.findById(countryId).isEmpty());
    }

    private static Country createCountry(int id, int cityId, String name) {
        Country country = new Country();
        country.setId(id);
        country.setCity_id(cityId);
        country.setName(name);
        return country;
    }
}