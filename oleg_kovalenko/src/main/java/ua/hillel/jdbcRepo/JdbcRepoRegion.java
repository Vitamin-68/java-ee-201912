package ua.hillel.jdbcRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hillel.entity.Region;
import ua.hillel.utils.CsvToDBLoader;
import ua.hillel.utils.TriggerUtils;
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
public class JdbcRepoRegion implements IndexedCrudRepository<Region, Integer> {

    private String tableName;
    private int PORT;
    private String DBNAME;
    private H2Server server;
    private Connection connection;

    public void fillRegion(char delimeter) {
        CsvToDBLoader loader = new CsvToDBLoader(delimeter, null, null);
        loader.addFromCsv("./src/main/resources/region.csv", "STUDY.REGION");
    }

    @Override
    public Optional<List<Region>> findAll() {
        String sql = "select * from STUDY.REGION";
        List<Region> regions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                regions.add(new Region(rs.getInt(1), rs.getInt(2),
                        rs.getInt(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(regions);
    }

    @Override
    public Optional<Region> findById(Integer id) {
        String sql = String.format("select * from %s where region_id = ?", tableName);
        Region region = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            region = new Region(rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getString(4));
            rs.close();
        } catch (SQLException e) {
            log.warn("can't find {}", e.getMessage());
        }
        return Optional.of(region);
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        String sql = String.format("select * from STUDY.REGION where %s = ?", fieldName);
        List<Region> regions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (fieldName.equals("name")) {
                stmt.setString(1, value.toString());
            } else {
                stmt.setInt(1, Integer.parseInt(value.toString()));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                regions.add(new Region(rs.getInt(1), rs.getInt(2),
                        rs.getInt(3), rs.getString(4)));
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("can't find {}", e.getMessage());
        }
        return Optional.of(regions);
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
    public Region create(Region entity) {
        String sql = String.format("insert into %s values (?,?,?,?)", tableName);
        TriggerUtils.setRecordToLog(String.format("id - %s name - %s %s create", tableName, entity.getCountryId(), entity.getName()), connection);
        return getRegion(entity, sql);
    }

    @Override
    public Region update(Region entity) {
        String sql = String.format("update %s " +
                "set country_id = ?," +
                "region_id = ?," +
                "name =? " +
                "where region_id = ?", tableName);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(4, entity.getRegionId());
            stmt.setInt(1, entity.getCountryId());
            stmt.setInt(2, entity.getRegionId());
            stmt.setString(3, entity.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.info("Error executing {}", e.getMessage());
        }
        TriggerUtils.setRecordToLog(String.format("id - %s name - %s %s updated", tableName, entity.getCountryId(), entity.getName()), connection);
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        String sql = String.format("delete from %s where region_id = ?", tableName);
        Region region = findById(id).get();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("warning {}", e.getMessage());
        }

        TriggerUtils.setRecordToLog(String.format("id - %s  %s updated", tableName, id), connection);
        return region;
    }

    private Region getRegion(Region entity, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entity.getRegionId());
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