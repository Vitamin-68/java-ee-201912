package ua.ithillel.dnepr.eugenekovalov;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.eugenekovalov.repository.CountryCrudRepo;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Country;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class CountryCrudRepoTest {

    Path pathToOrigin = Paths.get("src/main/resources/country.csv");
    Path pathToWorkingCopy = Paths.get("src/main/resources/tmp.csv");

    CountryCrudRepo countryCrudRepo = new CountryCrudRepo(pathToWorkingCopy, ';');

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
        assertNotNull(countryCrudRepo.findAll());
    }

    @Test
    void findByIdPositive() {
        Integer CountryId = 3661568;
        Optional<Country> Country = countryCrudRepo.findById(CountryId);
        Country result = Country.get();

        assertEquals(result.getId(), CountryId);
    }

    @Test
    void findByIdNegative() {
        Integer CountryId = 3661568;
        Optional<Country> Country = countryCrudRepo.findById(CountryId);
        Country result = Country.get();

        assertNotEquals(result.getId(), 33231);
    }

    @Test
    void findByField() {
        String field = "name";
        String value = "Малайзия";
        Optional<List<Country>> countries = countryCrudRepo.findByField(field, value);

        assertEquals(1, countries.get().size());
    }

    @Test
    void addCountry() {
        Country country = new Country();
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        country.setId(id);
        country.setCityId(23);
        country.setName("Impl");

        countryCrudRepo.create(country);

        assertEquals(countryCrudRepo.findById(id).get().getName(), country.getName());
    }

    @Test
    void updateCountry() {
        Integer countryId = 3661568;

        Country country = new Country();
        country.setId(countryId);
        country.setCityId(23);
        country.setName("Impl");

        countryCrudRepo.update(country);

        assertTrue(countryCrudRepo.findById(countryId).get().getName().equals("Impl"));
    }

    @Test
    void deleteCountry() {
        Integer countryId = 3661568;

        assertTrue(countryCrudRepo.findById(countryId).isPresent());
        countryCrudRepo.delete(countryId);

        assertFalse(countryCrudRepo.findById(countryId).isPresent());
    }
}
