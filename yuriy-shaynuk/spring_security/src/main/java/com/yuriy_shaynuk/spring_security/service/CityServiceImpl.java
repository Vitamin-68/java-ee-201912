package com.yuriy_shaynuk.spring_security.service;

import com.google.gson.Gson;
import com.yuriy_shaynuk.spring_security.repository.CityRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService{
    @Autowired
    CityRepositoryImpl cityRepository;

    @Override
    public String findAll() {
        String result = "";
        Optional<List<City>> cityList = cityRepository.findAll();
        if(cityList.isPresent()){
            result = new Gson().toJson(cityList.get());
        }
        return result;
    }
}
