package ua.hillel.jdbcRepo;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcConnection;
import ua.hillel.entity.City;
import ua.hillel.utils.CsvToDBLoader;
import ua.hillel.utils.TriggerUtils;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class JdbcRepoCity implements IndexedCrudRepository<City, Integer> {

    private String tableName;
    private int PORT;
    private String DBNAME;
    private H2Server server;
    private Connection connection;

    public void fillCity(char delimeter, String pathToCsv) {
        CsvToDBLoader loader = new CsvToDBLoader(delimeter, server, connection);
        loader.addFromCsv(pathToCsv, "STUDY.CITY");
    }

    @Override
    public Optional<List<City>> findAll() {
        String sql = String.format("select * from %s", tableName);
        List<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cities.add(new City(rs.getInt(1), rs.getInt(2),
                        rs.getInt(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            log.warn("Warning {}", e.getMessage());
        }
        return Optional.of(cities);
    }

    @Override
    public Optional<City> findById(Integer id) {
        String sql = String.format("select * from %s where city_id = ?", tableName);
        City city = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            city = new City(rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getString(4));
            rs.close();
        } catch (SQLException e) {
            log.warn("can't find {}", e.getMessage());
        }
        return Optional.of(city);
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        String sql = String.format("select * from %s where %s = ?", tableName, fieldName);
        List<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (fieldName.equals("name")) {
                stmt.setString(1, value.toString());
            } else {
                stmt.setInt(1, Integer.parseInt(value.toString()));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(new City(rs.getInt(1), rs.getInt(2),
                        rs.getInt(3), rs.getString(4)));
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("can't find {}", e.getMessage());
        }
        return Optional.of(cities);
    }

    @Override
    public void addIndex(String field) {
        String sql = String.format("CREATE INDEX id_idx ON %s(%s)", tableName, field);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("index error {}", e.getMessage());
        }
    }

    @Override
    public void addIndexes(List<String> fields) {
        for (String field : fields) {
            String sql = String.format("CREATE INDEX id_idx ON %s(%s)", tableName, field);
            try (H2Server server = new H2Server(PORT);
                 Connection connection = server.getConnection(DBNAME);
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                log.info("index error {}", e.getMessage());
            }
        }
    }

    @Override
    public City create(City entity) {
        String sql = String.format("insert into %s values (?,?,?,?)", tableName);
        TriggerUtils.setRecordToLog(String.format("id - %s name - %s %s created", tableName, entity.getCountryId(), entity.getName()), connection);
        return getCity(entity, sql);
    }

    @Override
    public City update(City entity) {
        String sql = String.format("update %s " +
                "set country_id = ?," +
                "region_id = ?," +
                "name =? " +
                "where city_id = ?", tableName);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(4, entity.getCityId());
            stmt.setInt(1, entity.getCountryId());
            stmt.setInt(2, entity.getRegionId());
            stmt.setString(3, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("Error executing {}", e.getMessage());
        }
        TriggerUtils.setRecordToLog(String.format("id - %s name - %s %s updated", tableName, entity.getCountryId(), entity.getName()), connection);
        return entity;
    }

    @Override
    public City delete(Integer id) {
        String sql = String.format("delete from %s where city_id = ?", tableName);
        City city = findById(id).get();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("warning {}", e.getMessage());
        }
        TriggerUtils.setRecordToLog(String.format("id - %s %s updated", tableName, id), connection);
        return city;
    }

    private City getCity(City entity, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entity.getCityId());
            stmt.setInt(2, entity.getCountryId());
            stmt.setInt(3, entity.getRegionId());
            stmt.setString(4, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("Error executing {}", e.getMessage());
        }
        return entity;
    }
}