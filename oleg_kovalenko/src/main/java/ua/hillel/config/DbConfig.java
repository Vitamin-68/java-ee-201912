package ua.hillel.config;

import ua.hillel.entity.City;
import ua.hillel.repository.CrudRepositoryCity;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.*;

public class DbConfig {

    public static Connection getConnectJdbc() {
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}