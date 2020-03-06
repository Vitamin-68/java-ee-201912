package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;
import ua.ithillel.dnepr.tymoshenko.olga.trigger.ModifyingTrigger;
import ua.ithillel.dnepr.tymoshenko.olga.util.EntityWorker;
import ua.ithillel.dnepr.tymoshenko.olga.util.MyQuery;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class JdbcMutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        extends JdbcBase<EntityType, IdType> implements MutableRepository<EntityType, IdType> {
    private final Connection connection;

    public JdbcMutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) throws SQLException {
        super(connection, clazz);
        this.connection = connection;
        ModifyingTrigger trigger = new ModifyingTrigger();
        trigger.init(connection, null, null, clazz.getSimpleName(), true, 0);
    }

    @Override
    public EntityType create(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        if (isEntityExist(entity)) {
            throw new IllegalArgumentException("Entity  already exists");
        }
        try {
            creatEntity(entity);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create entity", e);
        }
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        try {
            if (!isEntityExist(entity)) {
                creatEntity(entity);
            } else {
                updateEntity(entity);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update entity", e);
        }
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        Optional<EntityType> entity = getEntityById(id);
        EntityType result;
        if (entity.isEmpty()) {
            throw new IllegalArgumentException("Entity is not exists");
        }
        result = entity.get();
        String query = MyQuery.queryDelete(result.getClass().getSimpleName());
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete entity", e);
        }
        return result;
    }

    private void creatEntity(EntityType entity) throws SQLException {
        EntityWorker entityWorker = new EntityWorker(entity);
        List<Field> listField = entityWorker.getFields();
        List<Object> list = entityWorker.getValueField();
        List<String> columns = new ArrayList<>();
        for (Field field : listField) {
            columns.add(field.getName());
        }
        String query = MyQuery.queryInsert(entity.getClass().getSimpleName(), columns);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < listField.size(); i++) {
                if (H2TypeUtils.toH2Type(listField.get(i).getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                    preparedStatement.setObject(i + 1, list.get(i), Types.JAVA_OBJECT);
                } else {
                    preparedStatement.setObject(i + 1, list.get(i));
                }
            }
            preparedStatement.executeUpdate();
        }
    }

    private void updateEntity(EntityType entity) throws SQLException {
        EntityWorker entityWorker = new EntityWorker(entity);
        LinkedHashMap<String, Object> map = entityWorker.getFieldAndValue();
        String query = MyQuery.queryUpdate(entity.getClass().getSimpleName(), map);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.executeUpdate();
        }
    }
}

