package ua.hillel.utils;

import lombok.extern.slf4j.Slf4j;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.City;
import ua.hillel.entity.Country;
import ua.hillel.csvRepo.CrudRepositoryCity;
import ua.hillel.csvRepo.CrudRepositoryCountry;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Slf4j
public class CsvToDBLoader {

    public void init() {
        CsvToDBLoader loader = new CsvToDBLoader();
        try (H2Server h2Server = new H2Server(9092)) {
            h2Server.start();
            loader.addFromCsv("/home/oleg/study/prj_hillel_java_ee_201912/oleg_kovalenko/src/main/resources/city.csv");
            loader.addFromCsv("/home/oleg/study/prj_hillel_java_ee_201912/oleg_kovalenko/src/main/resources/country.csv");
            h2Server.stop();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addFromCsv(String pathToCsv) {

        String fileName = pathToCsv.substring(pathToCsv.lastIndexOf("/") + 1);
        PreparedStatement statement;
        createSchema();
        createTables();

        try {
            if (fileName.equals("city.csv")) {
                CrudRepository repoCity = new CrudRepositoryCity(pathToCsv, ';');

                List<City> cities = (List<City>) repoCity.findAll().get();
                statement = DbConfig.getConnectJdbc().prepareStatement("insert into STUDY.CITY values (?,?,?,?)");
                for (City city : cities) {
                    statement.setInt(1, city.getCityId());
                    statement.setInt(2, city.getCountryId());
                    statement.setInt(3, city.getRegionId());
                    statement.setString(4, city.getName());
                    statement.executeUpdate();
                }
            } else if (fileName.equals("country.csv")) {
                CrudRepository repoCountry = new CrudRepositoryCountry(pathToCsv, ';');
                List<Country> countries = (List<Country>) repoCountry.findAll().get();

                statement = DbConfig.getConnectJdbc().prepareStatement("insert into STUDY.COUNTRY values (?,?,?)");
                for (Country country : countries) {
                    statement.setInt(1, country.getCountryId());
                    statement.setInt(2, country.getCityId());
                    statement.setString(3, country.getName());
                    statement.executeUpdate();
                }
            } else {
                log.info("File is not correct");
            }

        } catch (SQLException e) {
            log.info(e.getMessage());
        }
    }

    private void createSchema() {
        try {
            PreparedStatement statement = DbConfig.getConnectJdbc().prepareStatement("create schema if not exists STUDY");
            statement.executeUpdate();
            log.info("Schema created ");
        } catch (SQLException e) {
            log.error("Error create schema {}", e.getMessage());
        }
    }

    private void createTables() {
        String createCity = "create table if not exists STUDY.City(city_id int primary key , country_id int, region_id int, name varchar(100))";
        String createCountry = "create table if not exists STUDY.Country(country_id int primary key , city_id int, name varchar(100))";
        try {
            PreparedStatement cityStmt = DbConfig.getConnectJdbc().prepareStatement(createCity);
            PreparedStatement countryStmt = DbConfig.getConnectJdbc().prepareStatement(createCountry);
            cityStmt.executeUpdate();
            countryStmt.executeUpdate();
            log.info("create successfull");
        } catch (SQLException e) {
            log.error("create table failed {}", e);
        }
    }
}
