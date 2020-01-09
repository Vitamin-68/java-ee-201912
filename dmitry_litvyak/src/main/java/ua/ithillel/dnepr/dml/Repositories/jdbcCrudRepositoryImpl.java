package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class jdbcCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType>
        implements IndexedCrudRepository<EntityType, IdType> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public jdbcCrudRepositoryImpl(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        final List<EntityType> result = new ArrayList<>();
        String entityName = clazz.getSimpleName();
        try (final Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("select * from public." + entityName);
            while (resultSet.next()) {
                EntityType entity = clazz.getDeclaredConstructor().newInstance();
                Stream<Method> methodStream = Stream.of(clazz.getDeclaredMethods());
                methodStream.filter(method->method.getName().startsWith("set")).forEach(method -> {
                    Object value = null;
                    try {
                        value = resultSet.getObject(method.getName().replaceFirst("set",""));
                    } catch (SQLException e) {
                        log.error("SQL error while get field value",e);
                    }
                    Class<?>[] types = method.getParameterTypes();
                    if(types.length>0){
                        //types[0].cast()
                        try {
                            method.invoke(entity,types[0].cast(value));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                           log.error("Unecpected filed",e);
                        }
                    }
                });
                result.add(entity);
            }
        } catch (SQLException e) {
            log.error("Failed to find all elements", e);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("wrong POJO",e);
        }
        return Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public void addIndex(String field) {

    }

    @Override
    public void addIndexes(List<String> fields) {

    }

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
