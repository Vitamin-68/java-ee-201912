package ua.hillel.utils;

import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TriggerUtils {
    private static final int PORT = 9092;
    private static final String DBNAME = "~/test";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String getDate() {
        return dtf.format(LocalDateTime.now());
    }

    public static void setRecordToLog(String message, Connection connection) {
        String sql = "insert into STUDY.LOGGER(EVENT_DATE, EVENT_MESSAGE) values (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, getDate());
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("insertion error {}", e.getMessage());
        }
    }
}
