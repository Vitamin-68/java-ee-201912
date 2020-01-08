package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.dml.domain.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class IndexedCrudRepositoryImplTest {

    private String rootDirectory;
    private IndexedCrudRepositoryImpl indexedCrudRepository;
    private Map<String, Integer> indexes;
    private City testCity;

    @BeforeEach
    void setUp() {
        rootDirectory = System.getProperty("java.io.tmpdir") + "crudRoot/";
        CityRepository cityRepository = new CityRepository("./src/main/resources/city.csv");
        String cityDir = rootDirectory + "city";
        List<City> entities = cityRepository.findAll().orElseThrow(() -> new IllegalArgumentException());
        indexes = new HashMap<>();
        //indexes.put("City_id",0);
        indexes.put("Country_id", 1);
        indexes.put("Region_id", 2);
        indexes.put("Name", 3);
        indexedCrudRepository = new IndexedCrudRepositoryImpl(rootDirectory, indexes);

        for (City city : entities
        ) {
            indexedCrudRepository.create(city);
        }
        log.debug("Repository created at:" + rootDirectory);
        testCity = new City();
        testCity.setCountry_id(3159);
        testCity.setId(9999);
        testCity.setRegion_id(4925);
        testCity.setName("Бандерштадт");
    }

    @AfterEach
    void tearDown() {
    }

    @Disabled
    @Test
    void findAll() {
        Assertions.assertNotNull(((ArrayList)indexedCrudRepository.findAll().get()).size()>0);
    }

    @Disabled
    @Test
    void findById() {
        AbstractEntity<Integer> test = new AbstractEntity<Integer>() {};
        test.setId(4317);
        Assertions.assertNotNull(indexedCrudRepository.findById(test).get());
    }

    @Test
    void findByField() {
        AbstractEntity<Integer> test = new AbstractEntity<Integer>() {};
        test.setId(4925);
        Assertions.assertNotNull(indexedCrudRepository.findByField("Region_id",4925));
    }

    @Disabled
    @Test
    void addIndex() {
    }

    @Disabled
    @Test
    void addIndexes() {
    }

    @Test
    void create() {
        indexedCrudRepository.create(testCity);
    }

    @Test
    void update() {
        indexedCrudRepository.create(testCity);
    }

    @Test
    void delete() {
        Assertions.assertNotNull(indexedCrudRepository.delete(4400));
    }
}