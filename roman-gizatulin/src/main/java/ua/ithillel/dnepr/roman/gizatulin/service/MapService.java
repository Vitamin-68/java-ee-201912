package ua.ithillel.dnepr.roman.gizatulin.service;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.dto.CityDTO;
import ua.ithillel.dnepr.roman.gizatulin.mapper.CityMapper;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.City;

import java.util.*;

public class MapService {
    private CrudRepository<City, Integer> cityCrudRepository;
    private final Map<Integer, City> integerCityMap = new HashMap<>();

    public List<CityDTO> findCityByName(String name) {
        List<CityDTO> result = new ArrayList<>();
        cityCrudRepository.findByField("name", name)
                .ifPresent(cities -> {
                    cities.forEach(city -> {
                        result.add(CityMapper.cityToDto(city));
                        integerCityMap.put(city.getId(), city);
                    });
                });
        return result;
    }


}
