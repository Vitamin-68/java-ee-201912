package com.yuriy_shaynuk.spring_security.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Repository
public class RegionRepositoryImpl implements CrudRepository<Region, Integer> {
    private CrudRepository<Region, Integer> crudRepository;

    public RegionRepositoryImpl() {
        File dataFile = Utils.createTempFile(getClass(),"region.csv");
        if (dataFile != null) {
            crudRepository = new CrudRepositoryImp<>(dataFile.getPath(),Region.class);
        }
    }

    @Override
    @Cacheable(cacheNames = "regions")
    public Optional<List<Region>> findAll() {
        return crudRepository.findAll();
    }

    @Override
    public Optional<Region> findById(Integer id) {
        return crudRepository.findById(id);
    }

    @Override
    public Optional<List<Region>> findByField(String s, Object o) {
        return Optional.empty();
    }

    @Override
    public Region create(Region region) {
        return crudRepository.create(region);
    }

    @Override
    public Region update(Region region) {
        return crudRepository.update(region);
    }

    @Override
    public Region delete(Integer id) {
        return crudRepository.delete(id);
    }
}
