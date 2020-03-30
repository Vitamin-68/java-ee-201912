package ua.ithillel.alex.tsiba.repository.repositories.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.annotations.Table;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class AbstractJdbcRepository<EntityType extends AbstractEntity<Integer>> {
    protected Connection connection;
    protected Class<EntityType> objectClass;
    protected List<String> columns = new ArrayList<>();
    protected String table;

    public AbstractJdbcRepository(Connection connection, Class<EntityType> objectClass) {
        this.connection = connection;
        this.objectClass = objectClass;

        getTableName();
        getColumn();
        checkDb();
    }

    protected void getColumn() {
        for (Field field : getAllFields(new ArrayList<Field>(), objectClass)) {
            Column anatation = field.getAnnotation(Column.class);
            if (Objects.nonNull(anatation)) {
                columns.add(anatation.name());
            }
        }
    }

    protected void getTableName() {
        final Table tableAnnotation = objectClass.getDeclaredAnnotation(Table.class);
        table = tableAnnotation.table();

        if ("".equals(table)) {
            throw new IllegalStateException("Object don't has table annotation;");
        }
    }

    protected List<Field> getAllFields(List fields, Class objectClass) {
        for (Field field : objectClass.getDeclaredFields()) {
            if (!fields.contains(field)) {
                fields.add(field);
            }
        }
        if (objectClass.getSuperclass() != null) {
            getAllFields(fields, objectClass.getSuperclass());
        }
        return fields;
    }

    protected void checkDb() {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ");
        query.append(table);

        List<String> columns = new ArrayList<>();
        for (Field field : getAllFields(new ArrayList<Field>(), objectClass)) {
            Column anatation = field.getAnnotation(Column.class);
            if (Objects.nonNull(anatation)) {
                columns.add(new StringBuffer().append(anatation.name()).append(' ').append(H2TypeUtils.toH2Type(field.getType())).toString());
            }
        }

        query.append("( ").append(StringUtils.join(columns, " ,")).append(" )");

        try (Statement statement = connection.createStatement()) {
            statement.execute(query.toString());
        } catch (SQLException e) {
            log.error("Failed to create table!", e);
            throw new IllegalStateException("Failed to create table!", e);
        }
    }
}
