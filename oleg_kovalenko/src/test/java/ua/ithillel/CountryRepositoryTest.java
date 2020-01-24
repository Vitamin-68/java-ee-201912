package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.Country;
import ua.hillel.csvRepo.CrudRepositoryCountry;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CountryRepositoryTest {

    private CrudRepository repositoryRead;
    private CrudRepository repositoryDml;
    String pathForRead = "../oleg_kovalenko/src/test/resources/country.csv";
    String pathForDml = "../oleg_kovalenko/src/test/resources/countryTest.csv";
    char delimeter = ';';

    @BeforeEach
    void init() {
        repositoryRead = new CrudRepositoryCountry(pathForRead, delimeter);
        repositoryDml = new CrudRepositoryCountry(pathForDml, delimeter);
    }

    @Test
    public void findAllTest() {
        Assertions.assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<Country> expected = repositoryRead.findById(11014);
        int actual = 11014;
        Assertions.assertEquals(actual, expected.get().getCountryId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<Country>> expected = repositoryRead.findByField("countryId", "11014");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }


    @Test
    public void findByNameTest() {
        Optional<List<Country>> expected = repositoryRead.findByField("name", "Ямайка");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new Country(1852456, 12365, "Просто страна"));
        Optional<Country> country = repositoryDml.findById(1852456);
        int expected = country.get().getCountryId();
        int actual = 1852456;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateCityIdTest() {
        repositoryDml.update(new Country(11060, 123657, "Япония"));
        Optional<Country> country = repositoryDml.findById(11060);
        int expected = country.get().getCityId();
        int actual = 123657;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new Country(582106, 0, "Ямайка Туц-Туц"));
        Optional<Country> country = repositoryDml.findById(582106);
        String expected = country.get().getName();
        String actual = "Ямайка Туц-Туц";
        Assertions.assertEquals(actual, expected);
    }
}
