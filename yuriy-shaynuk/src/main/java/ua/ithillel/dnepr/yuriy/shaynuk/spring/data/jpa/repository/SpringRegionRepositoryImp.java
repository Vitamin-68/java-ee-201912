package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity.Region;

@Slf4j
@Component
public class SpringRegionRepositoryImp extends BaseSpringRepositoryImp<Region,Integer> {
    public SpringRegionRepositoryImp(RegionCrudRepository regionCrudRepository) {
        super.crudRepository = regionCrudRepository;
    }
}
