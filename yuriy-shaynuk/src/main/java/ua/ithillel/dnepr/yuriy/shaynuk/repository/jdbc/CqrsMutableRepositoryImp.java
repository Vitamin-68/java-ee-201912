package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedRepository;
import ua.ithillel.dnepr.common.repository.cqrs.CqrsMutableRepository;
import ua.ithillel.dnepr.common.repository.cqrs.Observer;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class CqrsMutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseCqrsRepository<EntityType, IdType>
        implements CqrsMutableRepository<EntityType, IdType>, IndexedRepository {

    private final List<Observer<EntityType, IdType>> observers = new ArrayList<>();
    private PreparedStatement insertStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;

    public CqrsMutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
        super(connection, clazz);
        try {
            insertStatement = connection.prepareStatement(getSQLInsertString());
            updateStatement = connection.prepareStatement(getSQLUpdateString());
            deleteStatement = connection.prepareStatement(getSQLDeleteString());
        } catch (SQLException e) {
            log.error("prepare statement error",e);
        }
        addIndex("uuid");
    }

    @Override
    public EntityType create(EntityType entity) {
        Map<String, Object> entityKeyValue;
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        try {
            insertStatement.setObject(1,entity.getId());
            insertStatement.setObject(2,entity.getUuid());
            entityKeyValue = getEntityKeyValue(entity);
            int index = 3;
            for (Field field : fields) {
                insertStatement.setObject(index, entityKeyValue.get(field.getName()));
                index++;
            }

            insertStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            log.error("create error",e);
        }
        sendNotification(entity);
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        Map<String, Object> entityKeyValue;
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        try {
            entityKeyValue = getEntityKeyValue(entity);
            int index = 1;
            for (Field field : fields){
                updateStatement.setObject(index, entityKeyValue.get(field.getName()));
                index++;
            }
            updateStatement.setObject(index, entity.getId());
            updateStatement.executeUpdate();
        } catch (IllegalAccessException | SQLException e) {
            log.error("update error",e);
            e.printStackTrace();
        }
        sendNotification(entity);
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        EntityType entity = null;
        try {
            entity = getEntityById(id);
        } catch (NoSuchFieldException | IllegalAccessException | SQLException
                | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            log.error("get entity before delete error",e);
        }
        try {
            deleteStatement.setObject(1, id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("deleteStatement error",e);
        }
        sendNotification(entity);
        return entity;
    }

    @Override
    public void addIndex(String field) {
        try (Statement statement = connection.createStatement()) {
            String sqlInsertIndex = "CREATE INDEX IF NOT EXISTS "+field+" ON "+getTableName()+ " ("+field+")";
            statement.executeUpdate(sqlInsertIndex);
        } catch (SQLException e) {
            log.error("addIndex error",e);
            e.printStackTrace();
        }
    }

    @Override
    public void addIndexes(List<String> fields) {
        for (String field: fields) {
            addIndex(field);
        }
    }

    @Override
    public void addListener(Observer<EntityType, IdType> observer) { observers.add(observer); }

    private String getSQLInsertString(){
        List<Field> fields = getAllEntityFields();
        StringBuilder query = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();

        query.append("INSERT or REPLACE INTO ");
        query.append(getTableName());
        query.append(" (");
        for (Field field : fields){
            keys.append(field.getName());
            values.append("?");
            if(field != fields.get(fields.size()-1)){
                keys.append(",");
                values.append(",");
            }
        }
        query.append(keys);
        query.append(") VALUES(");
        query.append(values);
        query.append(")");
        return query.toString();
    }

    private String getSQLUpdateString() {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        StringBuilder query = new StringBuilder();

        query.append(" UPDATE ");
        query.append(getTableName());
        query.append(" SET ");
        for (Field field : fields){
            query.append(field.getName());
            query.append("=");
            query.append("?");
            if(field != fields.get(fields.size()-1)){
                query.append(",");
            }
        }
        query.append(" WHERE id= ?");
        return query.toString();
    }

    private String getSQLDeleteString() {
        return " DELETE FROM " +
                getTableName() +
                " WHERE id= ?";
    }

    private void sendNotification(EntityType entity) {
        observers.forEach(observer -> {
            observer.update(entity);
        });
    }
}
