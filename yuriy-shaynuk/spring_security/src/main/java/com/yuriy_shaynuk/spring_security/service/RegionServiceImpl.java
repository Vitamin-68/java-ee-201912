package com.yuriy_shaynuk.spring_security.service;

import com.google.gson.Gson;
import com.yuriy_shaynuk.spring_security.repository.RegionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    RegionRepositoryImpl regionRepository;

    @Override
    public String findAll() {
        String result = "";
        Optional<List<Region>> cityList = regionRepository.findAll();
        if (cityList.isPresent()) {
            result = new Gson().toJson(cityList.get());
        }
        return result;
    }
}
