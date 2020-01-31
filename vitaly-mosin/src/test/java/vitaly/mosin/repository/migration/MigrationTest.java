package vitaly.mosin.repository.migration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.utils.H2Server;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.entity.Country;
import vitaly.mosin.repository.entity.Region;
import vitaly.mosin.repository.jdbc.JdbcIndexedCrudRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class MigrationTest {
    private static final String PATH_TEST_DB = "./target/classes/dev/db/";
    private static final String DB_NAME = "mainRepoVM";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM CITY";
    private static final int NUMBER_OF_RECORDS_CITY_CSV = 10969;
    private static final int NUMBER_OF_RECORDS_COUNTRY_CSV = 106;
    private static final int NUMBER_OF_RECORDS_REGION_CSV = 922;
    private static final int KUBAN_PART_OF_UKRAINE = 9908;
    private static final int REGION_KRASNODARSKY_KRAI = 4052;
    private Connection connection;
    private static H2Server h2Server = new H2Server();
    private JdbcIndexedCrudRepository crudRepository;


    @Test
    private void migrationResult() {
        try {
            String repoRootPath = PATH_TEST_DB + DB_NAME;
            connection = h2Server.getConnection(repoRootPath);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        crudRepository = new JdbcIndexedCrudRepository(connection, Country.class);
        Optional<List<Country>> resultCountry = crudRepository.findAll();
        assertEquals(NUMBER_OF_RECORDS_COUNTRY_CSV, resultCountry.get().size());

        crudRepository = new JdbcIndexedCrudRepository(connection, Region.class);
        Optional<List<Region>> resultRegion = crudRepository.findAll();
        assertEquals(NUMBER_OF_RECORDS_REGION_CSV, resultRegion.get().size());

        crudRepository = new JdbcIndexedCrudRepository(connection, City.class);
        Optional<List<City>> resultCity = crudRepository.findAll();
        assertEquals(NUMBER_OF_RECORDS_CITY_CSV, resultCity.get().size());

        //Проверка changeSet id="return_Kuban_to_Ukraine_table_city"
        //замена ID страны
        resultCity = crudRepository.findByField("region_id", REGION_KRASNODARSKY_KRAI);
        for (City city : resultCity.get()) {
            assertEquals(KUBAN_PART_OF_UKRAINE, city.getCountryId());
        }

        //Проверка changeSet id="add_new_column_to_table_city"
        int columnCountTableCity, newColumn;
        String newColumnName = "new_id";
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            columnCountTableCity = resultSet.getMetaData().getColumnCount();

            // количество столбцов увеличилось до 5
            assertEquals(5, columnCountTableCity);
            newColumn = resultSet.findColumn(newColumnName);

            //новый столбец автоинкрементируемый
            assertTrue(resultSet.getMetaData().isAutoIncrement(newColumn));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
