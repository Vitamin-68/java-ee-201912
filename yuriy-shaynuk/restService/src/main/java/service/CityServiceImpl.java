package service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    CrudRepository<City, Integer> cityRepository;

    public CityServiceImpl(CrudRepository<City, Integer> cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Cacheable(cacheNames = "cities")
    public List<City> findAll() {
        return cityRepository.findAll().get();
    }

    @Override
    public City findById(Integer id) {
        return cityRepository.findById(id).get();
    }

    @Override
    public City create(int entityId, City entity) {
        entity.setId(entityId);
        return cityRepository.create(entity);
    }

    @Override
    public City update(int entityId, City entity) {
        entity.setId(entityId);
        return cityRepository.update(entity);
    }

    @Override
    public City delete(Integer id) {
        return cityRepository.delete(id);
    }
}
