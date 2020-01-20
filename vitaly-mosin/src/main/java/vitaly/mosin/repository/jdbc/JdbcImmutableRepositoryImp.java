package vitaly.mosin.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        extends BaseJdbcRepository<EntityType, IdType>
        implements ImmutableRepository<EntityType, IdType> {
    private static final String QUERY_SELECT_ALL = "SELECT * FROM %s";
    private static final String QUERY_SELECT_BY_FIELD = "SELECT * FROM %s WHERE %s = %s";
    private static final String QUERY_DATA_TYPE = "SELECT data_type FROM information_schema.columns " +
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
                System.out.println(entity);
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
        final Optional<List<EntityType>> result;
        final String query = String.format(QUERY_SELECT_BY_FIELD, getTableName(), fieldName, changeValueForStringColumn(fieldName, value));

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            final List<EntityType> entities = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
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

    Object changeValueForStringColumn(String fieldName, Object value) {
        final String query = String.format(QUERY_DATA_TYPE, getTableName().toUpperCase(), fieldName.toUpperCase()); //так проходит и сделано в коде
        final String query1 = String.format(QUERY_DATA_TYPE, "CITY", "NAME"); // так тоже проходит
        final String query2 = String.format(QUERY_DATA_TYPE, "City", "name"); //так не проходит
        final String query3 = String.format(QUERY_DATA_TYPE, getTableName(), fieldName); //и так не проходит

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getObject(1).equals(Types.VARCHAR)) {
                value = "'" + value + "'";
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return value;
    }
}
