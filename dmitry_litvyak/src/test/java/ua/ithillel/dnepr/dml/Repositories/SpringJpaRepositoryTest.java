package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.dml.domain.jpa.City;
import ua.ithillel.dnepr.dml.spring.datajpa.CitySpringJpaRepository;
import ua.ithillel.dnepr.dml.spring.datajpa.CitySpringJpaRepositoryImpl;
import ua.ithillel.dnepr.dml.spring.datajpa.Config;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SpringJpaRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static CitySpringJpaRepository repo;
    private static CitySpringJpaRepositoryImpl repoImpl;

    @BeforeAll
    static void setup(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        entityManagerFactory = ctx.getBean("entityManagerFactory",EntityManagerFactory.class);
        repo = ctx.getBean(CitySpringJpaRepository.class);
        repoImpl = ctx.getBean(CitySpringJpaRepositoryImpl.class);

    }

    @Test
    public void customSelect(){
        Optional<List<City>> result = repo.findCityByNameContains("ква");
        Assertions.assertEquals(result.isPresent(),true);
    }

    @Test
    public void commonRepoImplementation(){
        Optional<List<City>> result = repoImpl.findByField("name","Москва");
        Assertions.assertEquals(result.isPresent(),true);
    }
}
