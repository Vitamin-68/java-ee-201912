package ua.ithillel.dnepr.dml.service;

import lombok.extern.slf4j.Slf4j;
import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class LoggerTrigger implements Trigger {
    private String tableName;
    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        tableName = s2;
    }

    @Override
    public void fire(Connection connection, Object[] oldObjects, Object[] newObjects) throws SQLException {
        String diff = "";
        if(oldObjects != null ) {
            if(newObjects != null) {
                int maxIndex = Math.max(oldObjects.length, newObjects.length);
                int index = Math.min(oldObjects.length, newObjects.length);
                for (int i = 0; i < index; i++) {
                    if (!Objects.equals(oldObjects[i], newObjects[i])) {
                        diff += ("old=" + oldObjects[i] + " new=" + newObjects[i]);
                    }
                }
            }else{
                diff = "new object appear!";
            }
        }
        try(PreparedStatement pstmt = connection.prepareStatement("INSERT INTO log (eventTime,objTable,diff) VALUES (?,?,?)")){
            pstmt.setObject(1, LocalDateTime.now());
            pstmt.setObject(2,tableName);
            pstmt.setObject(3,diff);
            pstmt.execute();
        }catch (Exception e){
            log.error("Trigger insert failuse");
        };
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
