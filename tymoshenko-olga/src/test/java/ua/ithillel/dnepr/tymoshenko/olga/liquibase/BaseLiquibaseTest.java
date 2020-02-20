package ua.ithillel.dnepr.tymoshenko.olga.liquibase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseLiquibaseTest {

    private final Connection connection;

    public BaseLiquibaseTest(Connection connection) {
        this.connection = connection;
    }

    public int getcountPersonsPost(String post) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) FROM PERSON INNER JOIN POSITION " +
                "ON Person.POSTID =POSITION.IDPOST WHERE POSITION.POST = ").append("?");
        int record = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            preparedStatement.setString(1, post);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                record = resultSet.getInt(1);
            }
        }
        return record;
    }

    public int getCountTableRecord(String nameTable) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) FROM ").append(nameTable);
        int record = 0;
        try (Statement stat = connection.createStatement()) {
            ResultSet resultSet = stat.executeQuery(query.toString());
            while (resultSet.next()) {
                record = resultSet.getInt(1);
            }
        }
        return record;
    }

    public String getColumnType(String nameTable, String nameColumn) throws SQLException {
        Objects.requireNonNull(nameTable, "Name table is undefined");
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(nameTable);
        String type = null;
        try (Statement stat = connection.createStatement()) {
            ResultSet resultSet = stat.executeQuery(query.toString());
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                if (resultSet.getMetaData().getColumnName(i + 1).equalsIgnoreCase(nameColumn)) {
                    type = resultSet.getMetaData().getColumnTypeName(i + 1);
                }
            }
        }
        return type;
    }


    public int getCountEqualsColumns(String nameTable, List<String> listColumn) throws SQLException {
        int count = 0;
        List<String> actualColumn = new ArrayList<>();
        Objects.requireNonNull(nameTable, "Name table is undefined");
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(nameTable);
        ResultSet resultSet;
        try (Statement stat = connection.createStatement()) {
            resultSet = stat.executeQuery(query.toString());
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                actualColumn.add(resultSet.getMetaData().getColumnName(i + 1));
            }
        }
        for (String col : listColumn) {
            for (int i = 0; i < actualColumn.size(); i++) {
                if (col.equalsIgnoreCase(actualColumn.get(i))) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }


    public boolean isTableExist(String nameTable) throws SQLException {
        Objects.requireNonNull(nameTable, "Name table is undefined");
        List<String> listTable = getListTable();
        if (!listTable.isEmpty()) {
            for (String table : listTable) {
                if (table.equalsIgnoreCase(nameTable)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getListTable() throws SQLException {
        String query = "Select TABLE_NAME from information_schema.tables WHERE TABLE_TYPE='TABLE'";
        ResultSet resultSet;
        List<String> listTable = new ArrayList<>();
        try (Statement stat = connection.createStatement()) {
            resultSet = stat.executeQuery(query);
            while (resultSet.next()) {
                listTable.add(resultSet.getString(1));
            }
        }
        return listTable;
    }
}

