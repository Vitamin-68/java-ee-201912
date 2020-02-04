package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.utils.NetUtils;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
class LiquibaseTest {
    private static final int PORT = NetUtils.getFreePort();
    private static final String TEST_DB_NAME = "main";
    static Connection connection;

    @BeforeAll
    static void setup() throws SQLException {
        String repoRootPath = Paths.get("./target/classes/dev.db/", TEST_DB_NAME).toAbsolutePath().toString();
        log.info("Database path: {}", repoRootPath);
//        H2Server h2Server = new H2Server(PORT);
//        h2Server.start();
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
    void addColumnToRegions() {
        int numRowsTestColumn = 0;
        String sqlSelect = "SELECT count(test_column) as numRows FROM regions";

        try (PreparedStatement statement = connection.prepareStatement(sqlSelect)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            numRowsTestColumn = resultSet.getInt("numRows");
            resultSet.close();
        } catch (SQLException e) {
            log.error("getSQLNumRows error",e);
            e.printStackTrace();
        }
        Assertions.assertTrue(getSQLNumRows("regions") == numRowsTestColumn);
    }

    @Test
    void citiesUpdated() {
        boolean wrongCountryIdPresent = false;
        String sqlSelect = "SELECT country_id FROM cities where region_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlSelect)) {
            statement.setInt(1,5);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                if(resultSet.getInt("country_id") != 444){
                    wrongCountryIdPresent = true;
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("citiesUpdated select error",e);
            e.printStackTrace();
        }
        Assertions.assertFalse(wrongCountryIdPresent);
    }

    @Test
    void cityColumnChanged(){
        String type = "null";
        String sqlSelect = "select type_name from information_schema.columns where table_name=? and column_name=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlSelect)) {
            statement.setString(1,"COUNTRIES");
            statement.setString(2,"CITY_NULL");
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                type = resultSet.getString("type_name");
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("cityColumnChanged select type_name error",e);
            e.printStackTrace();
        }

        Assertions.assertEquals("VARCHAR", type);
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

     private int getSQLNumRows(String fromValue){
        int numRows = 0;
        String sqlSelect = "SELECT count(*) as numRows FROM "+fromValue;

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
}