package ua.ithillel.dnepr.dml.Repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.dml.domain.City;
import ua.ithillel.dnepr.dml.service.LoggerTrigger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class jdbcCrudRepositoryTest {

    private jdbcCrudRepositoryImpl testRepository;
    H2Server dbserver;
    private Connection conn;
    private City tmpCity;
    private LoggerTrigger trigger;

    @BeforeAll
    void generalSetup() throws SQLException, ClassNotFoundException {
        dbserver = new H2Server(NetUtils.getFreePort());
        dbserver.start();
        Class.forName("org.h2.Driver");
    }

    @AfterAll
    void generalClose(){
        dbserver.stop();
    }

    @BeforeEach
    void setUp() throws SQLException{
        conn = DriverManager.getConnection("jdbc:h2:file:./src/main/resources/h2crudbase","sa","");

        Statement stmt = conn.createStatement();
        stmt.execute("DROP TRIGGER IF EXISTS CITY_INSERT; CREATE TRIGGER CITY_INSERT AFTER UPDATE ON CITY FOR EACH ROW CALL \"ua.ithillel.dnepr.dml.service.LoggerTrigger\" ");

        testRepository = new jdbcCrudRepositoryImpl(conn, City.class);
        tmpCity = new City();
        tmpCity.setName("Bandershtadt");
        tmpCity.setRegion_id(1000);
        tmpCity.setId(99999);
        tmpCity.setCountry_id(1000);
    }

    @Test
    void findAll() {
        assertNotNull(testRepository.findAll());
    }

    @Test
    void findById() {
        assertNotNull(testRepository.findById(9999));
    }

    @Test
    void findByField() {
        assertNotNull(testRepository.findByField("Name","Абрамцево"));
        assertTrue(testRepository.findByField("Name","Нью Васюки").isEmpty());
    }

    @Test
    void addIndex() {
        testRepository.addIndex("name");
    }

    @Test
    void addIndexes() {
    }

    @Test
    void create() {

        assertEquals(testRepository.create(tmpCity),tmpCity);
        testRepository.delete(tmpCity.getId());
    }

    @Test
    void update() {
        tmpCity.setName("Lviv1715");
        assertNotEquals( ((City)testRepository.update(tmpCity)).getName(),"Bandershtadt");
    }

    @Test
    void delete() {
        testRepository.update(tmpCity);
        assertEquals(testRepository.delete(99999).getId(),99999);
    }
}