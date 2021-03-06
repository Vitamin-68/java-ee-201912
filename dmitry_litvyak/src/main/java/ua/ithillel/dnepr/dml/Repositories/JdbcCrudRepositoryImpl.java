package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class JdbcCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements IndexedCrudRepository<EntityType, IdType> {

    private Connection connection;
    private Class<? extends EntityType> clazz;

    public JdbcCrudRepositoryImpl(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        init();
    }

    public JdbcCrudRepositoryImpl() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        if(this.clazz != null){
            init();
        }
    }

    public void setClazz(Class<? extends EntityType> clazz) {
        this.clazz = clazz;
        if(this.connection != null){
            init();
        }
    }

    private void init(){
        Objects.requireNonNull(connection, "Connection is undefined");

        StringBuilder query = new StringBuilder();
        query.append(" CREATE TABLE IF NOT EXISTS ");
        query.append(this.clazz.getSimpleName());
        query.append(" ( ");
        final List<Field> declaredFields = new ArrayList<>();
        Class<?> iterateType = clazz;
        while (true) {
            declaredFields.addAll(Arrays.asList(iterateType.getDeclaredFields()));
            if (iterateType == AbstractEntity.class) {
                break;
            } else {
                iterateType = iterateType.getSuperclass();
            }
        }
        for (Field field : declaredFields) {
            query.append(field.getName()).append(' ').append(H2TypeUtils.toH2Type(field.getType())).append(",");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(" ) ");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table", e);
        }

        try (PreparedStatement stmt = connection.prepareStatement("DROP TRIGGER IF EXISTS " + clazz.getSimpleName() + "_INSERT; CREATE TRIGGER " + clazz.getSimpleName() + "_INSERT AFTER UPDATE ON " + clazz.getSimpleName() + " FOR EACH ROW CALL \"ua.ithillel.dnepr.dml.service.LoggerTrigger\" ")) {
            stmt.execute();
        } catch (SQLException e) {
            log.error("No connection", e);
        }
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        String findAllQuery = "select * from " + clazz.getSimpleName();
        return execListQuery(findAllQuery);
    }

    private Optional<List<EntityType>> execListQuery(String queryString) {
        Optional<List<EntityType>> result = Optional.empty();
        final List<EntityType> entityList = new ArrayList<>();
        try (final Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(queryString);
            while (resultSet.next()) {
                EntityType entity = getEntityFromDBResulSet(resultSet);
                entityList.add(entity);
            }
            if (!entityList.isEmpty()) {
                result = Optional.of(entityList);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("Failed to find all elements", e);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("wrong POJO", e);
        }
        return result;
    }

    private EntityType getEntityFromDBResulSet(ResultSet resultSet) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        EntityType entity = clazz.getDeclaredConstructor().newInstance();
        final List<Field> declaredFields = getEntityFields();
        Stream<Field> fieldStream = declaredFields.stream();
        fieldStream.forEach(field -> {
            field.setAccessible(true);
            try {
                Class<?> castType = H2TypeUtils.toJavaType(resultSet.getMetaData().getColumnTypeName(resultSet.findColumn(field.getName())));
                if (castType.isPrimitive()) {
                    field.set(entity, resultSet.getObject(field.getName()));
                } else {
                    field.set(entity, castType.cast(resultSet.getObject(field.getName())));
                }
            } catch (SQLException e) {
                log.error("SQL error while get field value", e);
            } catch (IllegalAccessException e) {
                log.error("Value cast error", e);
            }
        });
        return entity;
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        Optional<EntityType> result = Optional.empty();
        String entityName = clazz.getSimpleName();
        try (final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + entityName + " WHERE Id=?")) {
            stmt.setObject(1,id, Types.JAVA_OBJECT);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(getEntityFromDBResulSet(resultSet));
            }
            resultSet.close();
        } catch (Exception e) {
            log.error("Failed to find element by Id", e);
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        StringBuilder findByFieldQuery = new StringBuilder();
        findByFieldQuery.append("select * from public.").append(clazz.getSimpleName()).append(" WHERE ").append(fieldName).append("=\'").append(value).append("\'");
        return execListQuery(findByFieldQuery.toString());
    }

    @Override
    public void addIndex(String field) {
        List<String> fields = new ArrayList<>();
        fields.add(field);
        addIndexes(fields);
    }

    @Override
    public void addIndexes(List<String> fields) {
        StringBuilder query = new StringBuilder();
        for (String field : fields) {
            query.append("DROP INDEX IF EXISTS idx_").append(field).append(" ON ").append(clazz.getSimpleName()).append(';')
                    .append(" CREATE INDEX idx_").append(field).append(" ON ")
                    .append(clazz.getSimpleName())
                    .append('(').append(field).append(')')
                    .append(';');
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query.toString());
        } catch (Exception e) {
            log.error("Add index failure", e);
        }
    }

    @Override
    public EntityType create(EntityType entity) {
        try {
            StringBuilder insertQueryStart = new StringBuilder("INSERT INTO " + clazz.getSimpleName() + " (");
            StringBuilder insertQueryEnd = new StringBuilder(" VALUES ( ");
            final List<Field> declaredFields = getEntityFields();
            Stream<Field> methodStream = declaredFields.stream();
            methodStream.forEach(field -> {
                String fieldName;
                fieldName = field.getName();
                insertQueryStart.append(fieldName).append(',');
                insertQueryEnd.append(" ?, ");
            });
            insertQueryEnd.deleteCharAt(insertQueryEnd.lastIndexOf(",")).append(')');
            insertQueryStart.deleteCharAt(insertQueryStart.lastIndexOf(",")).append(')').append(insertQueryEnd.toString());
            PreparedStatement stmt = connection.prepareStatement(insertQueryStart.toString());
            methodStream = declaredFields.stream();
            methodStream.forEach(field -> {
                String fieldName;
                fieldName = field.getName();
                field.setAccessible(true);
                try {
                    if (H2TypeUtils.toH2Type(field.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                        stmt.setObject(declaredFields.indexOf(field) + 1, field.get(entity), Types.JAVA_OBJECT);
                    } else if (field.get(entity) == null) {
                        stmt.setObject(declaredFields.indexOf(field) + 1, "");
                    } else {
                        stmt.setObject(declaredFields.indexOf(field) + 1, field.get(entity));
                    }
                } catch (Exception e) {
                    log.error("Fail when getObject()", e);
                }
            });
            stmt.execute();
        } catch (Exception e) {
            log.error("Create operation failed", e);
        }
        return entity;
    }

    private List<Field> getEntityFields() {
        final List<Field> declaredFields = new ArrayList<>();
        Class<?> iterateType = clazz;
        while (true) {
            declaredFields.addAll(Arrays.asList(iterateType.getDeclaredFields()));
            if (iterateType == AbstractEntity.class) {
                break;
            } else {
                iterateType = iterateType.getSuperclass();
            }
        }
        return declaredFields;
    }

    @Override
    public EntityType update(EntityType entity) {
        Optional<EntityType> dbEntity = findById(entity.getId());
        if (dbEntity.isPresent()) {
            StringBuilder query = new StringBuilder("UPDATE ");
            query.append(clazz.getSimpleName()).append(" SET ");
            final List<Field> declaredFields = getEntityFields();
            Stream<Field> fieldStream = declaredFields.stream();
            fieldStream.forEach(field -> {
                String fieldName = field.getName();
                field.setAccessible(true);
                query.append(fieldName).append('=');
                try {
                    query.append('\'').append(field.get(entity).toString()).append('\'').append(',');
                } catch (IllegalAccessException e) {
                    log.error("Create invoke problem", e);
                }
            });
            query.deleteCharAt(query.length() - 1).append(" WHERE ID=?");
            try (final PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                stmt.setObject(1,entity.getId(), Types.JAVA_OBJECT);
                stmt.execute();
            } catch (Exception e) {
                log.error("Update failed", e);
            }
        } else {
            create(entity);
        }
        return entity;
    }

    @Override
    public EntityType delete(IdType id)  {
        EntityType result = null;
        Optional<EntityType> entity = findById(id);
        if (entity.isPresent()) {
            try (final PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + clazz.getSimpleName() + " WHERE ID=?")) {
                stmt.setObject(1,id, Types.JAVA_OBJECT);
                stmt.execute();
            } catch (Exception e) {
                log.error("DELETE failed", e);
            }
        }
        if(entity.isPresent()){
            result = entity.get();
        }
        return result;
    }
}
