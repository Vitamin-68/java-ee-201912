package ua.hillel;

import ua.hillel.utils.CsvToDBLoader;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (H2Server server = new H2Server(9092); Connection connection = server.getConnection("~/test")) {
            CsvToDBLoader loader = new CsvToDBLoader(';', server, connection);
            loader.createSchema();
            loader.createTables();
            loader.init("city.csv","STUDY.CITY");
            loader.init("country.csv","STUDY.COUNTRY");
            loader.init("region.csv","STUDY.REGION");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
