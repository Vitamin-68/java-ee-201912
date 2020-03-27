package controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    CrudRepository<City, Integer> cityRepository;

    public CityController(CrudRepository<City, Integer> cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping("")
    @ResponseBody
    public List<City> doGet (){
        return cityRepository.findAll().get();
    }

    @GetMapping("/{entityId}")
    @ResponseBody
    public City doGet (@PathVariable int entityId){
        return cityRepository.findById(entityId).get();
    }

    @PostMapping("/{entityId}")
    public City doPost(@PathVariable int entityId, @RequestBody City city){
        return cityRepository.create(city);
    }

    @DeleteMapping("/{entityId}")
    public City doDelete(@PathVariable int entityId){
        return cityRepository.delete(entityId);
    }

    @PutMapping("/{entityId}")
    public City doPut(@PathVariable int entityId, @RequestBody City city){
        return cityRepository.update(city);
    }
}
