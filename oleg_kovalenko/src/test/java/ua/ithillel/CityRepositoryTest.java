package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.hillel.entity.City;
import ua.hillel.csvRepo.CrudRepositoryCity;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CityRepositoryTest {

    private CrudRepository repositoryRead;
    private CrudRepository repositoryDml;
    String pathForRead = "../oleg_kovalenko/src/test/resources/city.csv";
    String pathForDml = "../oleg_kovalenko/src/test/resources/cityTest.csv";
    char delimeter = ';';

    @BeforeEach
    void init() {
        repositoryRead = new CrudRepositoryCity(pathForRead, delimeter);
        repositoryDml = new CrudRepositoryCity(pathForDml, delimeter);
    }

    @Test
    public void findAllTest() {
        Assertions.assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<City> expected = repositoryRead.findById(4400);
        int actual = 4400;
        Assertions.assertEquals(actual, expected.get().getCityId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("cityId", "4313");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("countryId", "3159");
        int actual = 2505;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("regionId", "4312");
        int actual = 172;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<City>> expected = repositoryRead.findByField("name", "Абрамцево");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new City(1852456, 12365, 212, "Просто город"));
        Optional<City> city = repositoryDml.findById(1852456);
        int expected = city.get().getCityId();
        int actual = 1852456;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateCountryTest() {
        repositoryDml.update(new City(11358, 123657, 11354, "Убе"));
        Optional<City> city = repositoryDml.findById(11358);
        int expected = city.get().getCountryId();
        int actual = 123657;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateRegionTest() {
        repositoryDml.update(new City(11359, 11060, 2120, "Хаги"));
        Optional<City> city = repositoryDml.findById(11359);
        int expected = city.get().getRegionId();
        int actual = 2120;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new City(11364, 11060, 11363, "Кофу Туц-Туц"));
        Optional<City> city = repositoryDml.findById(11364);
        String expected = city.get().getName();
        String actual = "Кофу Туц-Туц";
        Assertions.assertEquals(actual, expected);
    }
}