package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class jdbcCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType>
        implements IndexedCrudRepository<EntityType, IdType> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public jdbcCrudRepositoryImpl(Connection connection, Class<? extends EntityType> clazz) {
        Objects.requireNonNull(connection, "Connection is undefined");
        this.connection = connection;
        this.clazz = clazz;

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
            if(query.indexOf(field.getName()) == -1) {
                query.append(field.getName()).append(' ').append(H2TypeUtils.H2Types.toH2Type(field.getType())).append(",");
            }
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(" ) ");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table", e);
        }

        try {
            PreparedStatement stmt = connection.prepareStatement("DROP TRIGGER IF EXISTS "+clazz.getSimpleName()+"_INSERT; CREATE TRIGGER "+clazz.getSimpleName()+"_INSERT AFTER UPDATE ON "+clazz.getSimpleName()+" FOR EACH ROW CALL \"ua.ithillel.dnepr.dml.service.LoggerTrigger\" ");
            stmt.execute();
        } catch (SQLException e) {
            log.error("No connection",e);
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
        final List<Field> declaredFields = getEntityMethods();
        Stream<Field> fieldStream = declaredFields.stream();
        fieldStream.forEach(field -> {
            field.setAccessible(true);
            try {
                Class <?> castType = H2TypeUtils.H2Types.toJavaType(resultSet.getMetaData().getColumnTypeName(resultSet.findColumn(field.getName())));
                field.set(entity,castType.cast(resultSet.getObject(field.getName())));
            } catch (SQLException e) {
                log.error("SQL error while get field value", e);
            } catch (IllegalAccessException e) {
                log.error("Value cast error",e);
            }
        });
        return entity;
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        Optional<EntityType> result = Optional.empty();
        String entityName = clazz.getSimpleName();
        try (final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM "+entityName+" WHERE Id=?")) {
            stmt.setObject(1, id);
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
        try (final Statement stmt = connection.createStatement()) {
            StringBuilder insertQueryStart = new StringBuilder("INSERT INTO " + clazz.getSimpleName() + " (");
            StringBuilder insertQueryEnd = new StringBuilder(" VALUES ( ");
            final List<Field> declaredFields = getEntityMethods();
            Stream<Field> methodStream = declaredFields.stream();
            methodStream.forEach(field -> {
                String fieldName;
                fieldName = field.getName();
                field.setAccessible(true);
                if (insertQueryStart.indexOf(fieldName) == -1) {
                    insertQueryStart.append(fieldName).append(',');
                    try {
                        insertQueryEnd.append('\'').append(field.get(entity).toString()).append('\'').append(',');
                    } catch (IllegalAccessException e) {
                        log.error("Create invoke problem", e);
                    }
                }
            });
            insertQueryEnd.deleteCharAt(insertQueryEnd.length() - 1).append(')');
            insertQueryStart.deleteCharAt(insertQueryStart.length() - 1).append(')').append(insertQueryEnd.toString());
            stmt.execute(insertQueryStart.toString());
        } catch (Exception e) {
            log.error("Create operation failed", e);
        }
        return entity;
    }

    private List<Field> getEntityMethods() {
        final List<Field> declaredMethods = new ArrayList<>();
        Class<?> iterateType = clazz;
        while (true) {
            declaredMethods.addAll(Arrays.asList(iterateType.getDeclaredFields()));
            if (iterateType == AbstractEntity.class) {
                break;
            } else {
                iterateType = iterateType.getSuperclass();
            }
        }
        return declaredMethods;
    }

    @Override
    public EntityType update(EntityType entity) {
        Optional<EntityType> DBentity = findById(entity.getId());
        if (DBentity.isPresent()) {
            StringBuilder query = new StringBuilder("UPDATE ");
            query.append(clazz.getSimpleName()).append(" SET ");
            Stream<Method> methodStream = Stream.of(clazz.getDeclaredMethods());
            methodStream.filter(method -> method.getName().startsWith("get")).forEach(method -> {
                String fieldName;
                fieldName = method.getName().replaceFirst("get", "");
                if (query.indexOf(fieldName) == -1) {
                    query.append(fieldName).append('=');
                    try {
                        query.append('\'').append(method.invoke(entity).toString()).append('\'').append(',');
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("Create invoke problem", e);
                    }
                }
            });
            query.deleteCharAt(query.length() - 1).append(" WHERE ID=").append(entity.getId());
            try (final Statement stmt = connection.createStatement()) {
                stmt.execute(query.toString());
            } catch (Exception e) {
                log.error("Update failed", e);
            }
        } else {
            create(entity);
        }
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        Optional<EntityType> entity = findById(id);
        if (entity.isPresent()) {
            try (final Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM " + clazz.getSimpleName() + " WHERE ID=" + id);
            } catch (Exception e) {
                log.error("DELETE failed", e);
            }
        }
        if (entity.isEmpty()) {
            try {
                entity = Optional.of(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                log.error("New Entity", e);
            }
        }
        return entity.get();
    }
}
