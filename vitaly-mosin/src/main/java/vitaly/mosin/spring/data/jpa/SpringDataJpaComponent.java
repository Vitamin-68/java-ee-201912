package vitaly.mosin.spring.data.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;
import vitaly.mosin.spring.data.jpa.repository.CitySpringDataRepository;
import vitaly.mosin.spring.data.jpa.repository.CountrySpringDataRepository;
import vitaly.mosin.spring.data.jpa.repository.RegionSpringDataRepository;

import javax.transaction.Transactional;

//@Slf4j
//@Service
public class SpringDataJpaComponent {
//    private CitySpringDataRepository cityRepo;
//    private RegionSpringDataRepository regionRepo;
//    private CountrySpringDataRepository countryRepo;
////    private T entityRepo;
//
//    public SpringDataJpaComponent(CitySpringDataRepository cityRepo,
//                                  RegionSpringDataRepository regionRepo,
//                                  CountrySpringDataRepository countryRepo) {
//        this.cityRepo = cityRepo;
//        this.regionRepo = regionRepo;
//        this.countryRepo = countryRepo;
//    }

//    public SpringDataJpaComponent(T entityRepo) {
//this.entityRepo = entityRepo;
//    }

//    @Transactional
//    void findAll() {
//        Optional<Iterable<CityJdata>> result = Optional.ofNullable(cityRepo.findAll());
//        Iterable<CityJdata> result = cityRepo.findAll();
//        result.forEach(city -> System.out.println(city.toString()));
//    }
//    void createEntities() {
//        cityRepo.findAll();
//
//    }
}
