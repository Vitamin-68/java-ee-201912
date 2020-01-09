package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcIndexedCrudRepository<EntityType extends AbstractEntity<IdType>, IdType>
        implements IndexedCrudRepository<EntityType, IdType> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public JdbcIndexedCrudRepository(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        final List<EntityType> result = new ArrayList<>();
        String entityName = clazz.getName();
        try (final Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("select * from " + entityName);
            while (resultSet.next()) {
                System.out.println(resultSet.getRow() + ". " + resultSet.getString("firstName") + "\t" + resultSet.getString("lastName"));
                //result.add(...)
            }
        } catch (SQLException e) {
            log.error("Failed to find all elements", e);
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