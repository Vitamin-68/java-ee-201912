package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Country;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class CrudRepositoryTest {
    static File countryFile;
    static File cityFile;
    static File regionFile;

    static private CrudRepository<Country, Integer> countryRepository;
    static private CrudRepository<Region, Integer> regionRepository;
    static private CrudRepository<City, Integer> cityRepository;

    @BeforeAll
    static void setUp() {
        countryFile = createTempFile("country.csv");
        regionFile = createTempFile("region.csv");
        cityFile = createTempFile("city.csv");

        countryRepository = new CrudRepositoryImp<Country, Integer>(countryFile.getPath(),Country.class);
        regionRepository = new CrudRepositoryImp<Region, Integer>(regionFile.getPath(),Region.class);
        cityRepository = new CrudRepositoryImp<City, Integer>(cityFile.getPath(),City.class);
    }

    @Test
    void findAll() {
        Optional<List<City>> cities = cityRepository.findAll();
        assertFalse(cities.isPresent());
    }

    @Test
    void findById() {
    }

    @Test
    void findByField() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private static File createTempFile(String resourcePath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile("tmp", resourcePath);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            log.debug(tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            log.error("getResourceAsFile exception",e);
            return null;
        }
    }

}