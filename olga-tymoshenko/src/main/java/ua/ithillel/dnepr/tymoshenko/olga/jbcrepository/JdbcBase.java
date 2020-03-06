package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import lombok.extern.slf4j.Slf4j;
import org.h2.api.Trigger;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;
import ua.ithillel.dnepr.tymoshenko.olga.trigger.BaseTrigger;
import ua.ithillel.dnepr.tymoshenko.olga.util.EntityWorker;
import ua.ithillel.dnepr.tymoshenko.olga.util.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Optional;

@Slf4j
public class JdbcBase<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {
    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public JdbcBase(Connection connection, Class<? extends EntityType> clazz) throws SQLException {
        this.connection = connection;
        this.clazz = clazz;
        Table<EntityType, IdType> table = new Table<>(connection);
        table.addTable(creatEntity());
        BaseTrigger baseTrigger = new BaseTrigger(connection, clazz);
        baseTrigger.creatTrigger(null, false, Trigger.INSERT | Trigger.UPDATE | Trigger.DELETE);
    }

    public EntityType creatEntity() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Failed to create entity instance", e);
        }
    }

    public Optional<EntityType> getEntityById(IdType id) {
        StringBuilder query = new StringBuilder();

        EntityWorker entityWorker = new EntityWorker(creatEntity());
        final LinkedHashMap<String, Object> fieldValue = new LinkedHashMap<>();
        EntityType result = null;
        query.append("SELECT * FROM ").append(clazz.getSimpleName()).append(" ").append("WHERE ID = ?");
        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            Field field = entityWorker.getFieldByName("id");
            if (H2TypeUtils.toH2Type(field.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                preparedStatement.setObject(1, id, Types.JAVA_OBJECT);
            } else {
                preparedStatement.setObject(1, id);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    fieldValue.put(resultSetMetaData.getColumnName(i + 1), resultSet.getObject(i + 1));
                }
            }
            result = (EntityType) entityWorker.setFields(fieldValue);
        } catch (SQLException e) {
            log.error("Failed to get entity by Id", e);
        }
        return result == null ? Optional.empty() : Optional.of(result);
    }

    public boolean isEntityExist(EntityType entity) {
        Optional<EntityType> result = getEntityById(entity.getId());
        if (result.isEmpty()) {
            log.error("Failed to check entity. Entity is empty");
            return false;
        }
        return true;
    }
}

