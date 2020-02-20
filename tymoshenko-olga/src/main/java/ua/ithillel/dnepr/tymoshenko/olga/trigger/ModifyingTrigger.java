package ua.ithillel.dnepr.tymoshenko.olga.trigger;
import lombok.extern.slf4j.Slf4j;
import org.h2.api.Trigger;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.util.MyQuery;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModifyingTrigger<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> implements Trigger {

    private Connection connection;
    private final String TABLE = "SERVICE";
    private String nameEntyty;
    private List<String> columnServiseTable;
    private List<String> columnsTable = new ArrayList<>();

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        this.connection = connection;
        nameEntyty = s2.substring(0, 1) + s2.substring(1).toLowerCase();
        columnServiseTable = MyQuery.getColumnsTable(connection, TABLE);
        columnsTable = MyQuery.getColumnsTable(connection, s2);
    }

    @Override
    public void fire(Connection connection, Object[] objects, Object[] objects1) throws SQLException {

        if (objects != null && objects1 != null) {
            if (hasChanged(objects, objects1, columnsTable.size())) {
                update(objects, objects1);
            }
        } else if (objects == null && objects1 != null) {
            insert(objects1);
        } else if (objects != null) {
            delete(objects);
        }
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {
    }

    private static boolean hasChanged(Object[] oldRow, Object[] newRow, int countColumn) {
        for (int i = 0; i < countColumn; i++) {
            Object o = oldRow[i];
            Object n = newRow[i];
            if (o == null) {
                if (n != null) {
                    return true;
                }
            } else if (!o.equals(n)) {
                return true;
            }
        }
        return false;
    }

    private void update(Object[] objects, Object[] objects1) {
        String query = MyQuery.queryInsert(TABLE, columnServiseTable);

        for (int i = 0; i < columnsTable.size(); i++) {
            if (!objects[i].equals(objects1[i])) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setObject(1, nameEntyty);
                    preparedStatement.setObject(2, columnsTable.get(i));
                    preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    preparedStatement.setObject(4, objects[i]);
                    if (objects[i].getClass().getSimpleName().equals("byte[]")) {
                        preparedStatement.setObject(5, deserialize((byte[]) objects[i]).toString());
                    } else {
                        preparedStatement.setObject(5, objects[i]);
                    }
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new IllegalStateException("Failed trigger for query update", e);
                }
            }
        }
    }

    private void delete(Object[] objects) {
        String query = MyQuery.queryInsert(TABLE, columnServiseTable);
        for (int i = 0; i < objects.length; i++) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, nameEntyty);
                preparedStatement.setObject(2, columnsTable.get(i));
                preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.setObject(4, objects[i]);
                preparedStatement.setString(5, null);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Failed trigger for query delete", e);
            }
        }
    }

    private void insert(Object[] objects) {
        String query = MyQuery.queryInsert(TABLE, columnServiseTable);
        for (int i = 0; i < objects.length; i++) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, nameEntyty);
                preparedStatement.setObject(2, columnsTable.get(i));
                preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.setString(4, null);
                if (objects[i].getClass().getSimpleName().equals("byte[]")) {
                    preparedStatement.setObject(5, deserialize((byte[]) objects[i]).toString());
                } else {
                    preparedStatement.setObject(5, objects[i]);
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Failed trigger for query insert", e);
            }
        }
    }

    private IdType deserialize(byte[] entity) {
        IdType result;
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(entity);
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            result = (IdType) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            log.error("Failed to read entity", e);
            throw new IllegalArgumentException("Failed to read entity", e);
        }
        return result;
    }
}

