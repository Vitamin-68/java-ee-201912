package vitaly.mosin.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        extends BaseJdbcRepository<EntityType, IdType>
        implements ImmutableRepository<EntityType, IdType> {
    private static final String QUERY_SELECT_ALL = "SELECT * FROM %s";
    private static final String QUERY_SELECT_BY_FIELD = "SELECT * FROM %s WHERE %s = %s";
    private static final String QUERRY_DATA_TYPE = "SELECT data_type FROM information_schema.columns " +
            "WHERE table_name = '%s' AND column_name = '%s'";


    public JdbcImmutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
        super(connection, clazz);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        final List<EntityType> result = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_SELECT_ALL, getTableName()));
            while (resultSet.next()) {
                EntityType entity = createEntity();
                mapField(entity, resultSet);
                result.add(entity);
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.ofNullable(getEntityById(id));
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        Optional<List<EntityType>> result;
        try (Statement statement = connection.createStatement()) {
            final List<EntityType> entities = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(String.format(
                    QUERRY_DATA_TYPE, getTableName(), fieldName));
            try {
                while (resultSet.next()) {
                    //            if (resultSet.equals(H2TypeUtils.H2Types.VARCHAR.getH2Type())) {
                    value = "'" + value + "'";
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
//        }
            resultSet = statement.executeQuery(String.format(
                    QUERY_SELECT_BY_FIELD,
                    getTableName(),
                    fieldName,
                    value));
            while (resultSet.next()) {
                EntityType entity = createEntity();
                mapField(entity, resultSet);
                entities.add(entity);
            }
            result = Optional.of(entities);
        } catch (SQLException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }


}
