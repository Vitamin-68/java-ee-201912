package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.SneakyThrows;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JdbcCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {
    protected final static String QUERY_SELECT_BY_ID = "SELECT * FROM %s WHERE id = %s";

    private final Connection connection;
    private final Class<? extends EntityType> clazz;
    protected final String tableName;

    public JdbcCrudRepository(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        this.tableName = clazz.getSimpleName();
        Objects.requireNonNull(connection, "Connection is undefined");
        createTable(createEntity());
    }

    private void createTable(EntityType entity) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" CREATE TABLE IF NOT EXISTS ");
        stringBuilder.append(entity.getClass().getSimpleName());
        stringBuilder.append(" ( ");
        List<Field> fields = getAllFields();

        for (Field field : fields) {
            stringBuilder.append(field.getName())
                    .append(" ")
                    .append(H2TypeUtils.toH2Type(field.getType()));
        }
        stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
        stringBuilder.append(" ) ");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(stringBuilder.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table ", e);
        }
    }

    private List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    @SneakyThrows
    private EntityType createEntity() {
        return clazz.getConstructor().newInstance();
    }

    protected EntityType getEntityById(IdType id) {
        EntityType result = null;
        final String query = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            final Field field_id = getFieldByName("id");
            Objects.requireNonNull(field_id, "Id field is undefined");
            if (H2TypeUtils.toH2Type(field_id.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                statement.setObject(1, id, Types.JAVA_OBJECT);
            } else {
                statement.setObject(1, id);
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = createEntity();
                mapFiend(result, resultSet);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @SneakyThrows
    private void mapFiend(EntityType result, ResultSet resultSet) {
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            final String columnName = resultSetMetaData.getColumnName(i);
            final Field field = clazz.getDeclaredField(columnName);
            Objects.requireNonNull(field, "Field is undefined");
            Object fieldValue = resultSet.getObject(i, field.getType());
            field.set(result, fieldValue);

        }
    }

    private Field getFieldByName(String fieldName) {
        Field result = null;
        for (Field field : getAllFields()) {
            if (fieldName.equalsIgnoreCase(field.getName())) {
                result = field;
            }
        }
        return result;
    }

    private String getTableName() {
        return this.tableName;
    }
}
