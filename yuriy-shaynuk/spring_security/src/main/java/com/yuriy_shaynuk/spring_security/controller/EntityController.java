package com.yuriy_shaynuk.spring_security.controller;

import com.yuriy_shaynuk.spring_security.service.CityService;
import com.yuriy_shaynuk.spring_security.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntityController {
    static final int TYPE_CITY = 1;
    static final int TYPE_REGION = 2;

    @Autowired
    CityService cityService;
    @Autowired
    RegionService regionService;

    @GetMapping("/getList")
    public ResponseEntity<Object> getList(int type) {
        switch (type){
            case(TYPE_CITY):
                return new ResponseEntity<>(cityService.findAll(), HttpStatus.OK);
            case (TYPE_REGION):
                return new ResponseEntity<>(regionService.findAll(), HttpStatus.OK);
            default: return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
