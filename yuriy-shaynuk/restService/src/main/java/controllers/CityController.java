package controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import repository.CityRepositoryImpl;
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

    @GetMapping("/")
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
    void doPost(@PathVariable int entityId){
    }

    @DeleteMapping("/{entityId}")
    void doDelete(@PathVariable int entityId){

    }

    @PutMapping("/{entityId}")
    void doPut(@PathVariable int entityId){

    }
}
