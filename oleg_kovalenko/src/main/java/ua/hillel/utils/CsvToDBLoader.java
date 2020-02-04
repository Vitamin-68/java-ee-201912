package ua.hillel.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hillel.csvRepo.CrudRepositoryCity;
import ua.hillel.csvRepo.CrudRepositoryCountry;
import ua.hillel.csvRepo.CrudRepositoryRegion;
import ua.hillel.entity.City;
import ua.hillel.entity.Country;
import ua.hillel.entity.Region;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class CsvToDBLoader {

    private char delimeter;
    private H2Server server;
    private Connection connection;
    private final int PORT = 9092;
    private final String DBNAME = "~/test";
    private final String createCity = "create table if not exists STUDY.City(city_id int primary key , country_id int, region_id int, name varchar(100))";
    private final String createCountry = "create table if not exists STUDY.Country(country_id int primary key , city_id int, name varchar(100))";
    private final String createRegion = "create table if not exists STUDY.REGION(region_id int primary key, country_id int , city_id int, name varchar(100))";
    private final String createLogger = "create table if not exists STUDY.LOGGER(id int primary key auto_increment, event_date varchar(MAX), event_message varchar(150))";


    public void init(String fileName, String tableName) {
        ClassLoader loader = CsvToDBLoader.class.getClassLoader();
        URL path = loader.getResource(fileName);
        addFromCsv(path.getPath(), tableName);
    }

    public void addFromCsv(String pathToCsv, String tableName) {
        String fileName = pathToCsv.substring(pathToCsv.lastIndexOf("/") + 1);
        PreparedStatement statement;
        String fillCity = String.format("insert into %s values (?,?,?,?)", tableName);
        String fillCountry = String.format("insert into %s values (?,?,?)", tableName);
        String fillRegion = String.format("insert into %s values (?,?,?,?)", tableName);

        try {
            if (fileName.equals("city.csv")) {
                CrudRepository repoCity = new CrudRepositoryCity(pathToCsv, delimeter);
                List<City> cities = (List<City>) repoCity.findAll().get();
                statement = connection.prepareStatement(fillCity);
                for (City city : cities) {
                    statement.setInt(1, city.getCityId());
                    statement.setInt(2, city.getCountryId());
                    statement.setInt(3, city.getRegionId());
                    statement.setString(4, city.getName());
                    statement.executeUpdate();
                }
            } else if (fileName.equals("country.csv")) {
                CrudRepository repoCountry = new CrudRepositoryCountry(pathToCsv, delimeter);
                List<Country> countries = (List<Country>) repoCountry.findAll().get();
                statement = connection.prepareStatement(fillCountry);
                for (Country country : countries) {
                    statement.setInt(1, country.getCountryId());
                    statement.setInt(2, country.getCityId());
                    statement.setString(3, country.getName());
                    statement.executeUpdate();
                }
            } else if (fileName.equals("region.csv")) {
                CrudRepository repoRegion = new CrudRepositoryRegion(pathToCsv, delimeter);

                List<Region> regions = (List<Region>) repoRegion.findAll().get();
                statement = connection.prepareStatement(fillRegion);
                for (Region region : regions) {
                    statement.setInt(1, region.getRegionId());
                    statement.setInt(2, region.getCountryId());
                    statement.setInt(3, region.getCityId());
                    statement.setString(4, region.getName());
                    statement.executeUpdate();
                }
            } else {
                log.info("File is not correct");
            }
        } catch (SQLException e) {
            log.info(e.getMessage());
        }
    }

    public void createSchema() {
        try {
            PreparedStatement statement = connection.prepareStatement("create schema if not exists STUDY");
            statement.executeUpdate();
            log.info("Schema created ");
        } catch (SQLException e) {
            log.error("Error create schema {}", e.getMessage());
        }
    }

    public void createTables() {
        try {
            PreparedStatement cityStmt = connection.prepareStatement(createCity);
            PreparedStatement countryStmt = connection.prepareStatement(createCountry);
            PreparedStatement regionStmt = connection.prepareStatement(createRegion);
            PreparedStatement triggerStmt = connection.prepareStatement(createLogger);
            cityStmt.executeUpdate();
            countryStmt.executeUpdate();
            regionStmt.executeUpdate();
            triggerStmt.executeUpdate();
            log.info("create successfull");
        } catch (SQLException e) {
            log.error("create table failed {}", e.getMessage());
        }
    }
}