package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.hillel.csvRepo.CrudRepositoryCity;
import ua.hillel.entity.City;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class CityRepositoryTest {

    private CrudRepository repositoryRead;
    private CrudRepository repositoryDml;
    String pathForRead = "src/test/resources/city.csv";
    String pathForDml = "src/test/resources/cityTest.csv";
    char delimeter = ';';

    @BeforeEach
    void init() {
        repositoryRead = new CrudRepositoryCity(pathForRead, delimeter);
        repositoryDml = new CrudRepositoryCity(pathForDml, delimeter);
    }

    @Test
    public void findAllTest() {
        assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<City> expected = repositoryRead.findById(4400);
        assertEquals(4400, expected.get().getCityId());
    }

    @Test
    public void findByFieldIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("cityId", "4313");
        assertEquals(1, expected.get().size());
    }

    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("countryId", "3159");
        assertEquals(2505, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<City>> expected = repositoryRead.findByField("regionId", "4312");
        assertEquals(172, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<City>> expected = repositoryRead.findByField("name", "Абрамцево");
        int actual = 1;
        assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new City(1852456, 12365, 212, "Просто город"));
        Optional<City> city = repositoryDml.findById(1852456);
        int expected = city.get().getCityId();
        assertEquals(1852456, expected);
    }

    @Test
    public void updateCountryTest() {
        repositoryDml.update(new City(11358, 123657, 11354, "Убе"));
        Optional<City> city = repositoryDml.findById(11358);
        int expected = city.get().getCountryId();
        assertEquals(123657, expected);
    }

    @Test
    public void updateRegionTest() {
        repositoryDml.update(new City(11359, 11060, 2120, "Хаги"));
        Optional<City> city = repositoryDml.findById(11359);
        int expected = city.get().getRegionId();
        assertEquals(2120, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new City(11364, 11060, 11363, "Кофу Туц-Туц"));
        Optional<City> city = repositoryDml.findById(11364);
        String expected = city.get().getName();
        assertEquals("Кофу Туц-Туц", expected);
    }
}