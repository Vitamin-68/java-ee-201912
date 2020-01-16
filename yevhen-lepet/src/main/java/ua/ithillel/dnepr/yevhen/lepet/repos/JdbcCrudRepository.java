package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.SneakyThrows;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JdbcCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public JdbcCrudRepository(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
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
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(stringBuilder.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table " , e);
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
}
