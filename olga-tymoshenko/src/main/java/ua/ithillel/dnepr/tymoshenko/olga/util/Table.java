package ua.ithillel.dnepr.tymoshenko.olga.util;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Table<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {
    private final Connection connection;

    public Table(Connection connection) {
        this.connection = connection;
    }

    public void addTable(EntityType entity) {
        EntityWorker entityWorker = new EntityWorker(entity);
        String name = entity.getClass().getSimpleName();
        LinkedHashMap<String, String> columns = entityWorker.getFieldAndType();
        creatTable(name, columns);
    }


    public void addTable(String name, LinkedHashMap<String, String> columns) {

        creatTable(name, columns);
    }

    private String creatColumn(Map.Entry entry, String size) {
        final StringBuilder query = new StringBuilder();
        query.append((String) entry.getKey())
                .append(" ")
                .append((String) entry.getValue())
                .append("(")
                .append(size)
                .append(")")
                .append(" , ");
        return query.toString();
    }

    private void creatTable(String name, LinkedHashMap<String, String> columns) {
        final String SIZE_VARCHAR_FIELD = "100";
        final String SIZE_DATE_FIELD = "15";
        final StringBuilder query = new StringBuilder();
        query.append(" CREATE TABLE IF NOT EXISTS ").
                append(name).append(" ( ");
        for (Map.Entry entry : columns.entrySet()) {
            if (entry.getValue().equals(H2TypeUtils.H2Types.VARCHAR.toString())) {
                query.append(creatColumn(entry, SIZE_VARCHAR_FIELD));

            } else if (entry.getValue().equals(H2TypeUtils.H2Types.DATE.toString())) {
                query.append(creatColumn(entry, SIZE_DATE_FIELD));

            } else {
                query.append((String) entry.getKey())
                        .append(" ")
                        .append((String) entry.getValue())
                        .append(" , ");
            }
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(")");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            log.error("Failed to create table", e);
        }
    }
}

