package ua.hillel.jdbcRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.Country;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;

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

    @Override
    public Optional<List<Country>> findAll() {
        String sql = "select * from STUDY.COUNTRY";
        List<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                countries.add(new Country(rs.getInt(1), rs.getInt(2),
                        rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(countries);
    }

    @Override
    public Optional<Country> findById(Integer id) {
        String sql = "select * from STUDY.COUNTRY where Country_id = ?";
        Country country = null;
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            country = new Country(rs.getInt(1), rs.getInt(2),
                    rs.getString(3));
            rs.close();
        } catch (SQLException e) {
            log.info("can't find {}", e.getMessage());
        }
        return Optional.of(country);
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        String sql = String.format("select * from STUDY.COUNTRY where %s = ?", fieldName);
        List<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
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
            log.info("can't find {}", e.getMessage());
        }
        return Optional.of(countries);
    }

    @Override
    public void addIndex(String field) {
        String sql = String.format("CREATE INDEX id_idx ON STUDY.COUNTRY(%s)", field);
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("index error {}", e.getMessage());
        }
    }

    @Override
    public void addIndexes(List<String> fields) {
        for (String field : fields) {
            String sql = String.format("CREATE INDEX id_idx ON STUDY.COUNTRY(%s)", field);
            try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                log.info("index error {}", e.getMessage());
            }
        }
    }

    @Override
    public Country create(Country entity) {
        String sql = "insert into STUDY.COUNTRY values (?,?,?)";
        return getCountry(entity, sql);
    }

    @Override
    public Country update(Country entity) {
        String sql = "update STUDY.COUNTRY " +
                "set region_id = ?," +
                "name =? " +
                "where Country_id = ?";

        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(2, entity.getCountryId());
            stmt.setInt(1, entity.getCountryId());
            stmt.setString(3, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("Error executing {}", e.getMessage());
        }
        return entity;
    }

    @Override
    public Country delete(Integer id) {
        String sql = "delete from STUDY.COUNTRY where Country_id = ?";
        Country country = findById(id).get();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return country;
    }

    private Country getCountry(Country entity, String sql) {
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, entity.getCountryId());
            stmt.setInt(2, entity.getCountryId());
            stmt.setString(3, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("Error executing {}", e.getMessage());
        }
        return entity;
    }
}
