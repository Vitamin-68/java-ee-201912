package ua.ithillel.dnepr.roman.gizatulin.repository;

import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.Country;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CountryMyCrudRepoTest {
    private CrudRepository<Country, Integer> crudRepository = new MyCrudRepo<>("");

    @Test
    void findAll() {
        Optional<List<Country>> countries = crudRepository.findAll();
        assertFalse(countries.isPresent());
    }

    @Test
    void findById() {
        Optional<Country> country = crudRepository.findById(345);
        assertFalse(country.isPresent());
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
}