package ua.hillel.utils;

import lombok.extern.slf4j.Slf4j;
import ua.hillel.config.DbConfig;
import ua.ithillel.dnepr.common.utils.H2Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TriggerUtils {
    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void setRecordToLog(String message) {
        String sql = "insert into STUDY.LOGGER values (?,?)";
        try (PreparedStatement stmt = DbConfig.getConnectJdbc().prepareStatement(sql)) {
            stmt.setString(1, getDate());
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("insertion error {}", e.getMessage());
        }
    }
}
