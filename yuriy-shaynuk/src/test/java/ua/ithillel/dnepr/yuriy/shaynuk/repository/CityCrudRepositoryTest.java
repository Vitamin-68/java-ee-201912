package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class CityCrudRepositoryTest {
    static File cityFile;
    static private CrudRepository<City, Integer> cityRepository;

    @BeforeAll
    static void setUp() {
        cityFile = createTempFile("city.csv");
        if (cityFile != null) {
            cityRepository = new CrudRepositoryImp<City, Integer>(cityFile.getPath(),City.class);
        }
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = cityRepository.findAll();
        assertFalse(cities.get().isEmpty());
    }

    @Test
    void findById() {
        Optional<City> test = cityRepository.findById(4400);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByField() {
        Optional<List<City>> test = cityRepository.findByField("name", "Апрелевка");
        Assertions.assertFalse(test.get().isEmpty());
        Optional<List<City>> test2 = cityRepository.findByField("name", "some string name");
        Assertions.assertTrue(test2.get().isEmpty());
        Optional<List<City>> test3 = cityRepository.findByField("1name1", 99999);
        Assertions.assertTrue(test3.get().isEmpty());
    }

    @Test
    void create() {
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountry_id(111);
        testCity.setRegion_id(222);
        testCity.setId(999);
        cityRepository.create(testCity);
        Optional<List<City>> test3 = cityRepository.findByField("name", "testName");
        Assertions.assertFalse(test3.get().isEmpty());
    }

    @Test
    void update() {
        City testCity = new City();
        testCity.setName("Москва");
        testCity.setCountry_id(31599);
        testCity.setRegion_id(4312);
        testCity.setId(4400);

        City test = cityRepository.update(testCity);
        Assertions.assertNotNull(test);
    }

    @Test
    void delete() {
        City test = cityRepository.delete(4313);
        Assertions.assertNotNull(test);
    }

    private static File createTempFile(String resourcePath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile("tmp", resourcePath);
            //tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            log.debug("tmp file path: "+tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            log.error("getResourceAsFile exception",e);
            return null;
        }
    }

}