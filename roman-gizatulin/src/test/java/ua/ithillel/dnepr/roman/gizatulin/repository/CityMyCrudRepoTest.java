package ua.ithillel.dnepr.roman.gizatulin.repository;

import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.City;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CityMyCrudRepoTest {
    private CrudRepository<City, Integer> crudRepository = new MyCrudRepo<>("");
    private CrudRepository<City, Integer> crudRepository2 = new MyCrudRepo<>("");

    @Test
    void findAll() {
//        for (City city : crudRepository.findAll().get()) {
//            crudRepository2.create(city);
//        }


        Optional<List<City>> cities = crudRepository.findAll();
        assertFalse(cities.isPresent());
    }

    @Test
    void findById() {
        Optional<City> City = crudRepository.findById(345);
        assertFalse(City.isPresent());
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