package ua.hillel.jdbcRepo;

import lombok.extern.slf4j.Slf4j;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.City;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcRepoCity implements IndexedCrudRepository<City, Integer> {

    public static void main(String[] args) {
        JdbcRepoCity repoCity = new JdbcRepoCity();

        try (H2Server h2Server = new H2Server(9092);) {
            h2Server.start();
            System.out.println(repoCity.findByField("city_id", 6));
            h2Server.stop();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<List<City>> findAll() {
        String sql = "select * from STUDY.CITY";
        List<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cities.add(new City(rs.getInt(1), rs.getInt(2),
                        rs.getInt(3), rs.getString(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(cities);
    }

    @Override
    public Optional<City> findById(Integer id) {
        String sql = "select * from STUDY.CITY where city_id = ?";
        City city = null;
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            city = new City(rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getString(4));
            rs.close();
        } catch (SQLException e) {
            log.info("can't find {}", e.getMessage());
        }
        return Optional.of(city);
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        String sql = String.format("select * from STUDY.City where %s = ?", fieldName);
        List<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
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
            log.info("can't find {}", e.getMessage());
        }
        return Optional.of(cities);
    }

    @Override
    public void addIndex(String field) {

    }

    @Override
    public void addIndexes(List<String> fields) {

    }

    @Override
    public City create(City entity) {
        String sql = "insert into STUDY.CITY values (?,?,?,?)";
        return getCity(entity, sql);
    }

    @Override
    public City update(City entity) {
        String sql = "update STUDY.CITY " +
                "set country_id = ?," +
                "region_id = ?," +
                "name =? " +
                "where city_id = ?";
        return getCity(entity, sql);
    }

    private City getCity(City entity, String sql) {
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, entity.getCityId());
            stmt.setInt(2, entity.getCountryId());
            stmt.setInt(3, entity.getRegionId());
            stmt.setString(4, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("Error executing {}", e.getMessage());
        }
        return entity;
    }

    @Override
    public City delete(Integer id) {
        String sql = "delete from STUDY.CITY where city_id = ?";
        City city = findById(id).get();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }
}