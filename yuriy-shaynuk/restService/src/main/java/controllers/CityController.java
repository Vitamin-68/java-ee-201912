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
import service.CityService;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    CityService restService;

    public CityController(CityService restService) {
        this.restService = restService;
    }

    @GetMapping("")
    @ResponseBody
    public List<City> doGet (){
        return restService.findAll();
    }

    @GetMapping("/{entityId}")
    @ResponseBody
    public City doGet (@PathVariable int entityId){
        return restService.findById(entityId);
    }

    @PostMapping("/{entityId}")
    @ResponseBody
    public City doPost(@PathVariable int entityId, @RequestBody City city){
        System.out.println(entityId);
        return restService.create(city);
    }

    @DeleteMapping("/{entityId}")
    @ResponseBody
    public City doDelete(@PathVariable int entityId){
        return restService.delete(entityId);
    }

    @PutMapping("/{entityId}")
    @ResponseBody
    public City doPut(@PathVariable int entityId, @RequestBody City city){
        return restService.update(city);
    }
}
