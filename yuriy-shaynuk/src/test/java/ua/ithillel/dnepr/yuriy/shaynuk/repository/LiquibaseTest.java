package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc.CqrsRepositoryImp;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
class LiquibaseTest {
    private static final int PORT = NetUtils.getFreePort();
    private static H2Server h2Server;
    private static final String TEST_DB_NAME = "main";
    //private static final H2Server H2_SERVER = new H2Server();
    static Connection connection;

    @BeforeAll
    static void setup() throws IOException, SQLException {
        String repoRootPath = Paths.get("./target/classes/dev.db/", TEST_DB_NAME).toAbsolutePath().toString();
        log.info("Database path: {}", repoRootPath);
        //connection = H2_SERVER.getConnection(repoRootPath);
        h2Server = new H2Server(PORT);
        h2Server.start();
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:h2:file:".concat(repoRootPath),"sa","");
    }

    @AfterAll
    static void tearDown() {
        //H2_SERVER.stop();
    }

    @Test
    void countCities() {
         Assertions.assertTrue(getSQLNumRows("cities") > 0);
    }

    @Test
    void countRegions() {
        Assertions.assertTrue(getSQLNumRows("regions") > 0);
    }

    @Test
    void countCountries() {
        Assertions.assertTrue(getSQLNumRows("countries") > 0);
    }

    @Test
    void tableUserPresent() {
        boolean tableExist = false;
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "USER", null);
            if (tables.next()) {
                tableExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(tableExist);
    }

    private int getSQLNumRows(String value){
        int numRows = 0;
        String sqlSelect = "SELECT count(*) as numRows FROM "+value;
        try (PreparedStatement statement = connection.prepareStatement(sqlSelect)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            numRows = resultSet.getInt("numRows");
            resultSet.close();
        } catch (SQLException e) {
            log.error("getSQLNumRows error",e);
            e.printStackTrace();
        }
        return numRows;
    }

//    @Test
//    void delete() {
//        City testCity = new City();
//        testCity.setName("deleteCity");
//        testCity.setCountry_id(11);
//        testCity.setRegion_id(22);
//        testCity.setId(333);
//        cityRepository.create(testCity);
//
//        City test = cityRepository.delete(333);
//        Assertions.assertNotNull(cityRepository.findById(333));
//    }
}