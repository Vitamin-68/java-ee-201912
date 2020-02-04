package ua.hillel.jdbcRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hillel.entity.Country;
import ua.hillel.utils.CsvToDBLoader;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class JdbcRepoCountry implements IndexedCrudRepository<Country, Integer> {

    private String tableName;
    private int port;
    private String DbName;
    private H2Server server;
    private Connection connection;

    public void fillCountry(char delimeter) {
        CsvToDBLoader loader = new CsvToDBLoader(delimeter, server, connection);
        loader.addFromCsv("./src/main/resources/country.csv", "STUDY.COUNTRY");
    }

    @Override
    public Optional<List<Country>> findAll() {
        String sql = String.format("select * from %s ", tableName);
        List<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                countries.add(new Country(rs.getInt(1), rs.getInt(2),
                        rs.getString(3)));
            }
        } catch (SQLException e) {
            log.error("Empty {}", e.getMessage());
        }
        return Optional.of(countries);
    }

    @Override
    public Optional<Country> findById(Integer id) {
        String sql = String.format("select * from %s where Country_id = ?", tableName);
        Country country = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            country = new Country(rs.getInt(1), rs.getInt(2),
                    rs.getString(3));
            rs.close();
        } catch (SQLException e) {
            log.warn("can't find {}", e.getMessage());
        }
        return Optional.of(country);
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        String sql = String.format("select * from %s where %s = ?", tableName, fieldName);
        List<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (fieldName.equals("name")) {
                stmt.setString(1, value.toString());
            } else {
                stmt.setInt(1, Integer.parseInt(value.toString()));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                countries.add(new Country(rs.getInt(1), rs.getInt(2),
                        rs.getString(3)));
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("can't find {}", e.getMessage());
        }
        return Optional.of(countries);
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
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                log.warn("index error {}", e.getMessage());
            }
        }
    }

    @Override
    public Country create(Country entity) {
        String sql = String.format("insert into %s values (?,?,?)", tableName);
        return getCountry(entity, sql);
    }

    @Override
    public Country update(Country entity) {
        String sql = String.format("update %s " +
                "set city_id = ?," +
                "name =? " +
                "where Country_id = ?", tableName);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(3, entity.getCountryId());
            stmt.setInt(1, entity.getCityId());
            stmt.setString(2, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("Error executing {}", e.getMessage());
        }
        return entity;
    }

    @Override
    public Country delete(Integer id) {
        String sql = String.format("delete from %s where Country_id = ?", tableName);
        Country country = findById(id).get();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("Warning {}", e.getMessage());
        }
        return country;
    }

    private Country getCountry(Country entity, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entity.getCountryId());
            stmt.setInt(2, entity.getCountryId());
            stmt.setString(3, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("Error executing {}", e.getMessage());
        }
        return entity;
    }
}