package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.util.EntityWorker;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class JdbcImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        extends JdbcBase<EntityType, IdType> implements ImmutableRepository<EntityType, IdType> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public JdbcImmutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) throws SQLException {
        super(connection, clazz);
        this.clazz = clazz;
        this.connection = connection;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        List<EntityType> listEntity;
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(clazz.getSimpleName());
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query.toString());
            listEntity = getResultQuery(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find all entity", e);
        }
        return listEntity.isEmpty() ? Optional.empty() : Optional.of(listEntity);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return getEntityById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        Objects.requireNonNull(fieldName, "Field name is undefined");
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("Field name is empty");
        }
        List<EntityType> listEntity;
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ")
                .append(clazz.getSimpleName())
                .append(" WHERE ").append(fieldName).append(" = ").append("?");
        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            listEntity = getResultQuery(resultSet);

        } catch (SQLException e) {
            log.error("Failed find entity by field", e);
            throw new IllegalArgumentException("Failed find entity by field");
        }
        return listEntity.isEmpty() ? Optional.empty() : Optional.of(listEntity);
    }

    private List<EntityType> getResultQuery(ResultSet resultSet) throws SQLException {
        final LinkedHashMap<String, Object> fieldValue = new LinkedHashMap<>();
        final List<EntityType> listEntity = new ArrayList<>();
        ResultSetMetaData resultSetMetaData;
        resultSetMetaData = resultSet.getMetaData();
        while (resultSet.next()) {
            if (fieldValue.size() != 0) {
                fieldValue.clear();
            }
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                fieldValue.put(resultSetMetaData.getColumnName(i + 1), resultSet.getObject(i + 1));
            }
            EntityWorker entityWorker = new EntityWorker(creatEntity());
            listEntity.add((EntityType) entityWorker.setFields(fieldValue));
        }
        return listEntity;
    }
}

