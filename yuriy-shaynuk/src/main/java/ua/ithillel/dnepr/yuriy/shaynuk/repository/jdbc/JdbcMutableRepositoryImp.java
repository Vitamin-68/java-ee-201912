package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class JdbcMutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseJdbcRepository<EntityType, IdType>
        implements MutableRepository<EntityType, IdType> {

    private PreparedStatement insertStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;

    public JdbcMutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
        super(connection, clazz);
        try {
            insertStatement = connection.prepareStatement(getSQLInsertString());
            updateStatement = connection.prepareStatement(getSQLUpdateString());
            deleteStatement = connection.prepareStatement(getSQLDeleteString());
        } catch (SQLException e) {
            log.error("prepare statement error",e);
        }
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
            e.printStackTrace();
        }

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
        return entity;
    }

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
}
