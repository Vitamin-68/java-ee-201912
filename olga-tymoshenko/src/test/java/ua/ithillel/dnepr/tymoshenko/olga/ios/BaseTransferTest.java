package ua.ithillel.dnepr.tymoshenko.olga.ios;
import org.apache.commons.lang3.StringUtils;
import org.h2.tools.DeleteDbFiles;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.UtilTransfer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BaseTransferTest {

    public void clear(String file, String base) throws IOException {
        DeleteDbFiles.execute(StringUtils.removeEnd(base, new File(base).getName()), new File(base).getName(), true);
        if (Files.exists(new File(file).toPath())) {
            Files.delete(new File(file).toPath());
        }
    }

    public boolean isTableExist(String nameTable) throws SQLException {
        Objects.requireNonNull(nameTable, "Name table is undefined");
        List<String> listTable = getListTable();
        if (!getListTable().isEmpty()) {
            for (String table : listTable) {
                if (table.equalsIgnoreCase(nameTable)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createBase(String path) throws SQLException {
        Connection connection = getConnection();
        UtilTransfer utilTransfer = new UtilTransfer();
        Class<? extends AbstractEntity> clazz = utilTransfer.getClass(path);
        Optional<List<Field>> listField = utilTransfer.getListField(clazz);
        if (listField.isPresent()) {
            final StringBuilder headers = new StringBuilder();
            for (int i = 0; i < listField.get().size() - 3; i++) {
                headers.append(listField.get().get(i).getName()).append(" INTEGER, ");
            }
            headers.append(listField.get().get(listField.get().size() - 3).getName())
                    .append(" varchar(50)");
            final StringBuilder query = new StringBuilder();
            query.append("CREATE TABLE IF NOT EXISTS ")
                    .append("Region")
                    .append(" (")
                    .append(headers.toString())
                    .append(") ")
                    .append(" AS SELECT * FROM CSVREAD ")
                    .append("( \'")
                    .append(path).append("\'").append(", ").append("null").append(", ").append("\'charset=UTF-8 fieldSeparator=;\'").append(" )");
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query.toString());
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            connection.close();
        }
    }

    private List<String> getListTable() throws SQLException {
        Connection connection = getConnection();
        String query = "Select TABLE_NAME from information_schema.tables WHERE TABLE_TYPE='TABLE'";
        ResultSet resultSet;
        List<String> listTable = new ArrayList<>();
        try (Statement stat = connection.createStatement()) {
            resultSet = stat.executeQuery(query);
            while (resultSet.next()) {
                listTable.add(resultSet.getString(1));
            }
        }
        connection.close();
        return listTable;
    }

    private Connection getConnection() throws SQLException {
        final String DB_NAME = "testios";
        String pathDB;
        pathDB = Paths.get("./target/classes/dev/db/", DB_NAME).toAbsolutePath().toString();
        StringBuilder urlBase = new StringBuilder();
        urlBase.append("jdbc:h2:").append(pathDB);
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        return DriverManager.getConnection(urlBase.toString());
    }
}
