package vitaly.mosin.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import vitaly.mosin.repository.csv.CityCrudRepository;
import vitaly.mosin.repository.csv.CountryCrudRepository;
import vitaly.mosin.repository.csv.RegionCrudRepository;
import vitaly.mosin.repository.jpa.JpaCrudRepository;

import static vitaly.mosin.repository.Constants.FILE_CITY;
import static vitaly.mosin.repository.Constants.FILE_PATH_RESOURCE;

@Component
@Configuration
@ComponentScan({"vitaly.mosin.ioc"})
public class AppConfig {
    private CityCrudRepository cityCsvRepo;
    private RegionCrudRepository regionCsvRepo;
    private CountryCrudRepository countryCsvRepo;
    private JpaCrudRepository jpaRepo;

    @Bean
    public CityCrudRepository cityCsvRepo() {
        return new CityCrudRepository(FILE_PATH_RESOURCE+FILE_CITY);
    }
}
