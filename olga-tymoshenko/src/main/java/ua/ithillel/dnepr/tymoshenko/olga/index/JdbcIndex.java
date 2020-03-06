package ua.ithillel.dnepr.tymoshenko.olga.index;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.util.MyQuery;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JdbcIndex<EntityType extends AbstractEntity<IdType>, IdType> implements IndexedRepository {

    final private Connection connection;
    final private Class<? extends EntityType> clazz;
    private List<String> listColumns;

    public JdbcIndex(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        listColumns = new ArrayList<>();
        try {
            listColumns = MyQuery.getColumnsTable(connection, clazz.getSimpleName().toUpperCase());
        } catch (SQLException e) {
            log.error("Failed to get table columns");
        }
    }

    @Override
    public void addIndex(String indexName) {
        Objects.requireNonNull(indexName, "Index name is undefined");
        if (indexName.isEmpty()) {
            throw new IllegalArgumentException("Index name is empty");
        }
        final StringBuilder query = new StringBuilder();
        if (listColumns.isEmpty()) {
            throw new IllegalStateException("Columns list of this table is empty");
        }
        for (String columns : listColumns) {
            if (columns.equalsIgnoreCase(indexName)) {
                query.append("CREATE INDEX ")
                        .append(creatNameIndex())
                        .append(" ON ")
                        .append(clazz.getSimpleName()).
                        append(" ( ").append(indexName).append(" )");
            }
        }
        if (query.length() == 0) {
            throw new IllegalArgumentException("This column is not in this table");
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(query.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Fialed add index" + indexName, e);
        }
    }

    @Override
    public void addIndexes(List<String> fields) {
        Objects.requireNonNull(fields, "Fields list is undefined");
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("Fields list is empty");
        }

        if (listColumns.isEmpty()) {
            throw new IllegalStateException("Columns list of this table is empty");
        }
        StringBuilder query = new StringBuilder();
        StringBuilder index = new StringBuilder();
        query.append("CREATE INDEX ")
                .append(creatNameIndex())
                .append(" ON ")
                .append(clazz.getSimpleName()).append(" (");

        for (String columns : listColumns) {
            for (int i = 0; i < fields.size(); i++) {
                if (columns.equalsIgnoreCase(fields.get(i))) {
                    index.append(fields).append(",");
                }
            }
        }
        index.delete(index.lastIndexOf(" ,"), index.length());
        query.append(index).append(" )");
        try (Statement statement = connection.createStatement()) {
            statement.execute(query.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Fieled add list index", e);
        }
    }

    private String creatNameIndex() {
        final StringBuilder nameindex = new StringBuilder();
        return nameindex.append("ind_").append(clazz.getSimpleName()).toString();
    }
}
