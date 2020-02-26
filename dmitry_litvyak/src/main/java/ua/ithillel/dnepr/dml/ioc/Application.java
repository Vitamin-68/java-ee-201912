package ua.ithillel.dnepr.dml.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.dml.Repositories.CityRepository;
import ua.ithillel.dnepr.dml.Repositories.CountryRepository;
import ua.ithillel.dnepr.dml.Repositories.JdbcCrudRepositoryImpl;
import ua.ithillel.dnepr.dml.Repositories.RegionRepository;

import static ua.ithillel.dnepr.dml.ioc.Programm.objClass;

/**
 * азработать приложение, которое сможет перемещать данные, из одного файла в другой (например CSV <-> H2)
 * Приложение должно принимать на вход следующие параметры:
 * тип входного файла + путь к входному файлу
 * тип выходного файла + путь к выходному файлу
 * Т.е. Функция “мейн” + параметры + репозитории +Spring IoC
 */

@Slf4j
@Component
@Configuration
@ComponentScan("ua.ithillel.dnepr.dml.ioc")
@SuppressWarnings("rawtypes")
public class Application {

    @Bean(name = "cityRepo")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CityRepository getCityRepo() {
        CityRepository repo = new CityRepository();
        repo.setDelimiter(';');
        return repo;
    }

    @Bean(name = "countryRepo")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CountryRepository getCountryRepo() {
        CountryRepository repo = new CountryRepository();
        repo.setDelimiter(';');
        return repo;
    }

    @Bean(name = "regionRepo")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public RegionRepository getRegionRepo() {
        RegionRepository repo = new RegionRepository();
        repo.setDelimiter(';');
        return repo;
    }

    @Bean(name = "h2Repo")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public JdbcCrudRepositoryImpl getH2Repo() {
        JdbcCrudRepositoryImpl result = new JdbcCrudRepositoryImpl();
        result.setClazz(objClass);
        return result;
    }
}
