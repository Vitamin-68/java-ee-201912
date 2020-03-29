package service;

import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.util.List;

public interface CityService {
    List<City> findAll();

    City findById(Integer id);

    City create(City entity);

    City update(City entity);

    City delete(Integer id);
}
