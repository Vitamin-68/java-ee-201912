package ua.ithillel.dnepr.tymoshenko.olga.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyQuery {
    public static String queryInsert(String table, List<String> columns) {
        final StringBuilder query = new StringBuilder();
        final StringBuilder param = new StringBuilder();
        final StringBuilder value = new StringBuilder();

        query.append("INSERT INTO ").append(table).append(" (");
        for (String column : columns) {
            param.append(column).append(", ");
            value.append("?").append(", ");
        }
        param.delete(param.lastIndexOf(","), param.length());
        value.delete(value.lastIndexOf(","), value.length());
        query.append(param).append(")").append(" VALUES").append(" (").append(value).append(")");
        return query.toString();
    }

    public static String queryUpdate(String table, LinkedHashMap<String, Object> map) {
        final StringBuilder query = new StringBuilder();
        final StringBuilder param = new StringBuilder();
        query.append("UPDATE ")
                .append(table)
                .append(" SET ");
        for (Map.Entry entry : map.entrySet()) {
            param.append(entry.getKey()).append("=").append("'").append(entry.getValue()).append("'").append(",");
        }
        param.delete(param.lastIndexOf(","), param.length());
        query.append(param).append(" WHERE id = ? ");

        return query.toString();
    }

    public static String queryDelete(String table) {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ")
                .append(table)
                .append(" WHERE id = ? ");
        return query.toString();
    }

    public static List<String> getColumnsTable(Connection connection, String table) throws SQLException {
        final List<String> columns = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("select column_name from information_schema.columns " +
                "where information_schema.columns.table_name=").append("?");
        ResultSet resultSet;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            preparedStatement.setObject(1, table);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                columns.add(resultSet.getString(1));
            }
        }
        return columns;
    }
}
