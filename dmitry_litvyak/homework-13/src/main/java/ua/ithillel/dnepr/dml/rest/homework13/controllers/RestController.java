package ua.ithillel.dnepr.dml.rest.homework13.controllers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.dnepr.dml.Repositories.CityRepository;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/cities")
public class RestController {

    private final CityRepository cityRepository;

    public RestController() throws IOException {
        Path fl1 = Files.createTempFile("city", "csv");
        this.cityRepository = new CityRepository(fl1.toString());
    }

    @GetMapping("")
    @Cacheable(cacheNames = "cities")
    public List<City> getReq(){
        List<City> cities = cityRepository.findAll().get();
        return cities;
    }


    @GetMapping("/{Id}")
    @Cacheable(cacheNames = "cities")
    public City doGet (@PathVariable int Id){
        City city = cityRepository.findById(Id).get();
        return city;
    }

    @PutMapping("/{Id}")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "cities",allEntries = true)
    public City doPut(@PathVariable int Id, @RequestBody City city){
        city.setId(Id);
        city.setRegion_id(0);
        city.setCountry_id(0);
        cityRepository.update(city);
        return city;
    }

    @PostMapping("/{Id}")
    @ResponseStatus(HttpStatus.CREATED)
    @CachePut(cacheNames = "cities")
    public City doPost(@PathVariable int Id, @RequestBody City city){
        city.setId(Id);
        city.setRegion_id(0);
        city.setCountry_id(0);
        cityRepository.create(city);
        return city;
    }

    @DeleteMapping("/{Id}")
    @ResponseStatus(HttpStatus.OK)
    public int doDelete(@PathVariable int Id){
        cityRepository.delete(Id);
        return Id;
    }
}
