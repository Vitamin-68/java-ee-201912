package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yevhen.lepet.entity.Country;

import java.util.List;
import java.util.Optional;


@Slf4j
public class CountryCrudRepoTest {
    private CountryCrudRepo countryCrudRepo;
    private static final String PATH_COUNTRY_CSV = "./src/main/resources/country.csv";

    @BeforeEach
    void setUp() {
        countryCrudRepo = new CountryCrudRepo(PATH_COUNTRY_CSV);
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(countryCrudRepo.findAll().get());
    }
    @Test
    void findById(){
        Optional<Country> testCountry = countryCrudRepo.findById(3159);
        Assertions.assertEquals(3159, testCountry.get().getId());
    }

    @Test
    void findByField(){
        Optional<List<Country>> countries = countryCrudRepo.findByField("name", "Люксембург");
        Assertions.assertTrue(countries.isPresent());
    }

    @Test
    void create(){
        Country testCountry = new Country();
        testCountry.setId(111212121);
        testCountry.setName("NewCountry");
        Country result = countryCrudRepo.update(testCountry);
        Assertions.assertNotNull(result);
    }

    @Test
    void update(){
        Country testCountry = countryCrudRepo.findById(3159).get();
        testCountry.setName("NewCountry");
        countryCrudRepo.update(testCountry);
        Assertions.assertEquals(testCountry.getName(), countryCrudRepo.findById(3159).get().getName());
    }

    @Test
    void delete(){
        countryCrudRepo.delete(111212121);
        Assertions.assertTrue(countryCrudRepo.findById(111212121).isEmpty());
    }
}
