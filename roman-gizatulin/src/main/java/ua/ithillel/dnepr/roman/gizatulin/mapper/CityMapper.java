package ua.ithillel.dnepr.roman.gizatulin.mapper;

import ua.ithillel.dnepr.roman.gizatulin.dto.CityDTO;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.City;

public class CityMapper {
    public static CityDTO cityToDto(City city) {
        CityDTO result = new CityDTO();
        result.setName(city.getName());
        return result;
    }
}
