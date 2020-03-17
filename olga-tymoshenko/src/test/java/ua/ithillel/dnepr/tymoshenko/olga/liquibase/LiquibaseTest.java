package ua.ithillel.dnepr.tymoshenko.olga.liquibase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LiquibaseTest {
    private static final String DB_NAME = "main";
    private Connection connection;
    BaseLiquibaseTest test;

    @BeforeEach
    void setup() throws SQLException {
        String pathDB = Paths.get("./target/classes/dev/db/", DB_NAME).toAbsolutePath().toString();
        StringBuilder urlBase = new StringBuilder();
        urlBase.append("jdbc:h2:").append(pathDB);
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        connection = DriverManager.getConnection(urlBase.toString(), "sa", "");
        test = new BaseLiquibaseTest(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void checkCreatTablePerson() throws SQLException {
        assertTrue(test.isTableExist("Person"));
    }

    @Test
    void checkCreatTableCities() throws SQLException {
        assertTrue(test.isTableExist("Cities"));
    }

    @Test
    void checkCreatTablePosition() throws SQLException {
        assertTrue(test.isTableExist("Position"));
    }

    @Test
    void checkColumnTable() throws SQLException {
        String nameTable = "Position";
        final List<String> listColumn = new ArrayList<>();
        listColumn.add("IDPost");
        listColumn.add("Post");
        listColumn.add("Salary");
        assertEquals(listColumn.size(), test.getCountEqualsColumns(nameTable, listColumn));

        nameTable = "PERSON";
        listColumn.clear();
        listColumn.add("IDPERSON");
        listColumn.add("FIRSTNAME");
        listColumn.add("BIRTHDAY");
        listColumn.add("CITY");
        listColumn.add("POSTID");
        assertEquals(listColumn.size(), test.getCountEqualsColumns(nameTable, listColumn));

        nameTable = "CITIES";
        listColumn.clear();
        listColumn.add("CITYID");
        listColumn.add("REGIONID");
        listColumn.add("NAME");
        assertEquals(listColumn.size(), test.getCountEqualsColumns(nameTable, listColumn));
    }

    @Test
    void checkChangeColumnType() throws SQLException {
        String nameTable = "CITIES";
        String nameColumn = "REGIONID";
        String type = "VARCHAR";
        String actualType = test.getColumnType(nameTable, nameColumn);
        assertTrue(actualType.equalsIgnoreCase(type));
    }

    @Test
    void checkColumnTypePerson() throws SQLException {
        String nameTable = "PERSON";
        List<String> typeColumn = new ArrayList<>();
        List<String> actualType = new ArrayList<>();
        List<String> listColumn = new ArrayList<>();

        typeColumn.add("INTEGER");
        typeColumn.add("VARCHAR");
        typeColumn.add("DATE");
        typeColumn.add("VARCHAR");
        typeColumn.add("INTEGER");

        listColumn.add("IDPERSON");
        listColumn.add("FIRSTNAME");
        listColumn.add("BIRTHDAY");
        listColumn.add("CITY");
        listColumn.add("POSTID");
        for (String column : listColumn) {
            actualType.add(test.getColumnType(nameTable, column));
        }
        for (int i = 0; i < typeColumn.size(); i++) {
            assertTrue(typeColumn.get(i).equalsIgnoreCase(actualType.get(i)));
        }
    }

    @Test
    void checkColumnTypePosition() throws SQLException {
        String nameTable = "POSITION";
        List<String> typeColumn = new ArrayList<>();
        List<String> actualType = new ArrayList<>();
        List<String> listColumn = new ArrayList<>();

        typeColumn.add("INTEGER");
        typeColumn.add("VARCHAR");
        typeColumn.add("INTEGER");

        listColumn.add("IDPost");
        listColumn.add("Post");
        listColumn.add("Salary");
        for (String column : listColumn) {
            actualType.add(test.getColumnType(nameTable, column));
        }
        for (int i = 0; i < typeColumn.size(); i++) {
            assertTrue(typeColumn.get(i).equalsIgnoreCase(actualType.get(i)));
        }
    }

    @Test
    void checkColumnTypeCities() throws SQLException {
        String nameTable = "Cities";
        List<String> typeColumn = new ArrayList<>();
        List<String> actualType = new ArrayList<>();
        List<String> listColumn = new ArrayList<>();

        typeColumn.add("INTEGER");
        typeColumn.add("VARCHAR");
        typeColumn.add("VARCHAR");

        listColumn.add("CITYID");
        listColumn.add("REGIONID");
        listColumn.add("NAME");
        for (String column : listColumn) {
            actualType.add(test.getColumnType(nameTable, column));
        }
        for (int i = 0; i < typeColumn.size(); i++) {
            assertTrue(typeColumn.get(i).equalsIgnoreCase(actualType.get(i)));
        }
    }

    @Test
    void getListSaler() throws SQLException {
        String post = "Saler";
        assertTrue(test.getcountPersonsPost(post) > 0);
    }

    @Test
    void getListManager() throws SQLException {
        String post = "Manager";
        assertTrue(test.getcountPersonsPost(post) > 0);
    }

    @Test
    void getListDriver() throws SQLException {
        String post = "Driver";
        assertTrue(test.getcountPersonsPost(post) > 0);
    }

    @Test
    void checkTableRecordCities() throws SQLException {
        String nameTable = "CITIES";
        assertTrue(test.getCountTableRecord(nameTable) > 0);
    }

    @Test
    void checkTableRecordPerson() throws SQLException {
        String nameTable = "PERSON";
        assertTrue(test.getCountTableRecord(nameTable) > 0);
    }

    @Test
    void checkTableRecordPosition() throws SQLException {
        String nameTable = "PERSON";
        assertTrue(test.getCountTableRecord(nameTable) > 0);
    }
}

