package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class jdbcCrudRepositoryTest {

    private JdbcCrudRepositoryImpl testRepository;
    H2Server dbserver;
    private Connection conn;
    private City tmpCity;
    private int DBPort;
    private String databaseName;

    @BeforeAll
    void generalSetup() throws SQLException, ClassNotFoundException, IOException {
        DBPort = NetUtils.getFreePort();
        dbserver = new H2Server(DBPort);
        dbserver.start();
        log.info("Server started on "+NetUtils.getHostName()+" port:"+DBPort);
        Class.forName("org.h2.Driver");
        databaseName = File.createTempFile("dml_", LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"))).toString();
    }

    @AfterAll
    void generalClose(){
        dbserver.stop();
    }

    @BeforeEach
    void setUp() throws SQLException{
        conn = DriverManager.getConnection("jdbc:h2:tcp://"+NetUtils.getHostName()+':'+DBPort+'/'+databaseName,"sa","");
        tmpCity = new City();
        tmpCity.setName("Bandershtadt");
        tmpCity.setRegion_id(1000);
        tmpCity.setId(99999);
        tmpCity.setCountry_id(1000);
        testRepository = new JdbcCrudRepositoryImpl(conn, City.class);
    }

    @Test
    void findAll() {
        testRepository.update(tmpCity);
        assertNotNull(testRepository.findAll());
    }

    @Test
    void findById() {
        testRepository.update(tmpCity);
        assertNotNull(testRepository.findById(9999));
    }

    @Test
    void findByField() {
        testRepository.update(tmpCity);
        assertNotNull(testRepository.findByField("Name","Bandershtadt"));
        assertTrue(testRepository.findByField("Name","Нью Васюки").isEmpty());
    }

    @Test
    void addIndex() {
        testRepository.addIndex("name");
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
        testRepository.delete(tmpCity.getId());
    }

    @Test
    void delete() {
        testRepository.update(tmpCity);
        assertEquals(testRepository.delete(99999).getId(),99999);
    }
}