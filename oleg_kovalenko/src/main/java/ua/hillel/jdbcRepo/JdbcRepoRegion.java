package ua.hillel.jdbcRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hillel.config.DbConfig;
import ua.hillel.entity.Region;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;

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

    @Override
    public Optional<List<Region>> findAll() {
        String sql = "select * from STUDY.REGION";
        List<Region> regions = new ArrayList<>();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql);
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
        String sql = "select * from STUDY.REGION where region_id = ?";
        Region region = null;
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            region = new Region(rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getString(4));
            rs.close();
        } catch (SQLException e) {
            log.info("can't find {}", e.getMessage());
        }
        return Optional.of(region);
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        String sql = String.format("select * from STUDY.REGION where %s = ?", fieldName);
        List<Region> regions = new ArrayList<>();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
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
            log.info("can't find {}", e.getMessage());
        }
        return Optional.of(regions);
    }

    @Override
    public void addIndex(String field) {
        String sql = String.format("CREATE INDEX id_idx ON STUDY.REGION(%s)", field);
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("index error {}", e.getMessage());
        }
    }

    @Override
    public void addIndexes(List<String> fields) {
        for (String field : fields) {
            String sql = String.format("CREATE INDEX id_idx ON STUDY.REGION(%s)", field);
            try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                log.info("index error {}", e.getMessage());
            }
        }
    }

    @Override
    public Region create(Region entity) {
        String sql = "insert into STUDY.REGION values (?,?,?,?)";
        return getRegion(entity, sql);
    }

    @Override
    public Region update(Region entity) {
        String sql = "update STUDY.REGION " +
                "set country_id = ?," +
                "region_id = ?," +
                "name =? " +
                "where region_id = ?";

        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(4, entity.getRegionId());
            stmt.setInt(1, entity.getCountryId());
            stmt.setInt(2, entity.getRegionId());
            stmt.setString(3, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("Error executing {}", e.getMessage());
        }
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        String sql = "delete from STUDY.REGION where region_id = ?";
        Region region = findById(id).get();
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return region;
    }

    private Region getRegion(Region entity, String sql) {
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setInt(1, entity.getRegionId());
            stmt.setInt(2, entity.getCountryId());
            stmt.setInt(3, entity.getRegionId());
            stmt.setString(4, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("Error executing {}", e.getMessage());
        }
        return entity;
    }
}