package ua.ithillel.dnepr.dml.Repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.dml.domain.City;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class jdbcCrudRepositoryTest {

    private jdbcCrudRepositoryImpl testRepository;
    H2Server dbserver;
    private Connection conn;

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
        testRepository = new jdbcCrudRepositoryImpl(conn, City.class);
    }

    @Test
    void findAll() {
        assertNotNull(testRepository.findAll());
    }

    @Test
    void findById() {
    }

    @Test
    void findByField() {
    }

    @Test
    void addIndex() {
    }

    @Test
    void addIndexes() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}