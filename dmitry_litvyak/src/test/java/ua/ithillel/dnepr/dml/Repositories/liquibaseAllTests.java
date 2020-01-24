package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class liquibaseAllTests {

    private Connection conn;
    private String databaseName;
    private Statement stmt;

    @BeforeAll
    void generalSetup() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.h2.Driver");
        databaseName = System.getProperty("user.dir") + "\\target\\classes\\dev\\db\\main";
    }

    @BeforeEach
    void setUp() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:file:" + databaseName, "sa", "");
            stmt = conn.createStatement();
        } catch (Exception e) {
            log.error("Connection problem", e);
        }
    }

    @Test
    void isTablePresent() throws SQLException {
        assertTrue(stmt.execute("select count(*) from city;"));
        assertTrue(stmt.execute("select count(*) from region;"));
        assertTrue(stmt.execute("select count(*) from country;"));
        assertTrue(stmt.execute("select count(*) from log;"));
    }

    @Test
    void isRostovUkranianCity() throws SQLException {
        ResultSet rst = stmt.executeQuery("SELECT country_id FROM city WHERE id=4848;");
        assertTrue(rst.next());
    }

    @Test
    void isLogHasColunDiff() throws SQLException {
        boolean result = false;
        ResultSet rst = stmt.executeQuery("SELECT * FROM LOG;");
        ResultSetMetaData metaData = rst.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (metaData.getColumnName(i).equals("DIFF")) {
                result = true;
            }
        }
        assertTrue(result, "column DIFF");
    }

    @Test
    void isLogDontHasColunDescription() throws SQLException {
        boolean result = false;
        ResultSet rst = stmt.executeQuery("SELECT * FROM LOG;");
        ResultSetMetaData metaData = rst.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (metaData.getColumnName(i).equals("DESCRIPTION")) {
                result = true;
            }
        }
        assertFalse(result, "column DESCRIPTION");
    }

}
