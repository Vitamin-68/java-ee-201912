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
}
