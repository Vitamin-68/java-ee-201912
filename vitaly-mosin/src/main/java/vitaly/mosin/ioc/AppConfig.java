package vitaly.mosin.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import vitaly.mosin.repository.csv.CityCrudRepository;
import vitaly.mosin.repository.csv.CountryCrudRepository;
import vitaly.mosin.repository.csv.RegionCrudRepository;

import java.sql.Connection;
import java.sql.SQLException;

import static vitaly.mosin.Main.clazz;
import static vitaly.mosin.Main.filePathCsv;

@Slf4j
@Component
@Configuration
@ComponentScan({"vitaly.mosin.ioc",
        "vitaly.mosin.repository.jdbc"})
public class AppConfig {
    private static final String FILE_PATH_RESOURCE = "./vitaly-mosin/src/main/resources/";
    private static final String FILE_PATH_TMP = "./vitaly-mosin/target/classes/dev/db/";
    private static final String DB_FILE = "mainRepoVM";
    private static final String FILE_CITY_TEST = "city_test.csv";
    private static final String FILE_REGION_TEST = "region_test.csv";
    private static final String FILE_COUNTRY_TEST = "country_test.csv";
    private Connection connection;

    @Bean(name = "cityCSV")
    public CityCrudRepository getCityCsvRepo() {
        return new CityCrudRepository(filePathCsv + FILE_CITY_TEST);
    }

    @Bean("regionCSV")
    public RegionCrudRepository getRegionCsvRepo() {
        return new RegionCrudRepository(filePathCsv + FILE_REGION_TEST);
    }

    @Bean(name = "countryCSV")
    public CountryCrudRepository getCountryCsvRepo() {
        return new CountryCrudRepository(filePathCsv + FILE_COUNTRY_TEST);
    }

    @Bean(name = "connection")
    public Connection getConnection() {
        try {
            connection = getServer().getConnection(FILE_PATH_TMP + DB_FILE, "sa", "");
        } catch (SQLException e) {
            log.error("Connection error.", e);
        }
        return connection;
    }

    @Bean
    public H2Server getServer() {
        H2Server h2Server = new H2Server(NetUtils.getFreePort());
        return h2Server;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Class getClazz() {
        return clazz;
    }
}
