package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class LiquibaseTest {

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;

    private int countQuery(String sql) {
        int count = 0;
        try {
            rs = stmt.executeQuery(sql);
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
        return count;
    }

    @BeforeAll
    static void init() {
        String url = "jdbc:h2:file:" + System.getProperty("user.dir") + "/target/classes/dev/db/main";
        try {
            String driver = "org.h2.Driver";
            String username = "sa";
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, "");
            stmt = conn.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            log.warn("Connection failed");
        }
    }

    @AfterAll
    static void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            log.warn("Don't close");
        }
    }

    @Test
    void countCityTest() {
        assertEquals(10970, countQuery("select count(*) from CITY"));
    }

    @Test
    void countCountryTest() {
        assertEquals(106, countQuery("select count(*) from COUNTRY"));
    }

    @Test
    void countRegionTest() {
        assertEquals(922, countQuery("select count(*) from REGION"));
    }

    @Test
    void updateCityTest() {
        assertEquals(1, countQuery("select count(*) from CITY where country_id_1 = 33333"));
    }

    @Test
    void modifyColumn() {
        assertEquals(1, countQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'CITY' AND COLUMN_NAME = 'COUNTRY_ID_1'"));
    }

    @Test
    void dropColumn() {
        assertEquals(0, countQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'CITY' AND COLUMN_NAME = 'REGION_ID'"));
    }

    @Test
    void addColumn() {
        assertEquals(1, countQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'CITY' AND COLUMN_NAME = 'REGION_ID_1'"));
    }
}
