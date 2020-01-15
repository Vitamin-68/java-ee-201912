package ua.hillel.utils;

import lombok.extern.slf4j.Slf4j;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.City;
import ua.hillel.repository.CrudRepositoryCity;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Slf4j
public class CsvToDBLoader {

    public void createSchema() {
        try {
            PreparedStatement statement = DbConfig.getConnectJdbc().prepareStatement("create schema study");
            log.info("Schema created ");
        } catch (SQLException e) {
            log.error("Error create schema {}", e.getMessage());
        }
    }


    public void createTables(){

    }



    public void addFromCsv(String pathToCsv) {

        CrudRepository repository = new CrudRepositoryCity(pathToCsv, ';');
        List<City> cities = (List<City>) repository.findAll().get();

        try {
            PreparedStatement statement = DbConfig.getConnectJdbc().prepareStatement("insert into HILLEL.CITY values (?,?,?,?)");
            for (City city : cities) {
                statement.setInt(1, city.getCityId());
                statement.setInt(2, city.getCountryId());
                statement.setInt(3, city.getRegionId());
                statement.setString(4, city.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            log.info("");
        }
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        CsvToDBLoader loader = new CsvToDBLoader();

        H2Server h2Server = new H2Server(9092);
        h2Server.start();
        loader.addFromCsv("/home/oleg/study/prj_hillel_java_ee_201912/oleg_kovalenko/src/main/resources/city.csv");
        h2Server.stop();
        h2Server.close();
//
//
////        Thread.sleep(10000);
//        System.out.println(dbConfig.getConnectJdbc());


    }
}
