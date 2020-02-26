package ua.ithillel.dnepr.tymoshenko.olga.trigger;
import org.h2.api.Trigger;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;
import ua.ithillel.dnepr.tymoshenko.olga.util.Table;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

public class BaseTrigger<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {
    private final Connection connection;
    private final Class<? extends EntityType> clazz;

    public BaseTrigger(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
    }

    public void creatTrigger(String name, boolean before, int type) throws SQLException {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        Table<EntityType, IdType> table = new Table<>(connection);
        columns.put("tblentity", H2TypeUtils.H2Types.VARCHAR.toString());
        columns.put("column", H2TypeUtils.H2Types.VARCHAR.toString());
        columns.put("date", H2TypeUtils.H2Types.DATE.toString());
        columns.put("oldRow", H2TypeUtils.H2Types.VARCHAR.toString());
        columns.put("newRow", H2TypeUtils.H2Types.VARCHAR.toString());
        table.addTable("SERVICE", columns);
        String eventTrigger;
        if (before) {
            eventTrigger = " BEFORE ";
        } else {
            eventTrigger = " AFTER ";
        }
        if ((type & Trigger.INSERT) != 0) {
            creatTriggerInsert(name, eventTrigger);
        }
        if ((type & Trigger.DELETE) != 0) {
            creatTriggerDelete(name, eventTrigger);
        }
        if ((type & Trigger.UPDATE) != 0) {
            creatTriggerUpdate(name, eventTrigger);
        }
    }

    private void creatTriggerDelete(String nameTrigger, String eventTriger) throws SQLException {
        String name;
        String typeTrigger;
        typeTrigger = "DELETE";
        name = "DEL_";
        if (nameTrigger == null) {
            name = name + clazz.getSimpleName();
        } else {
            name = nameTrigger;
        }
        executeTrigger(name, eventTriger, typeTrigger);
    }

    private void creatTriggerUpdate(String nameTrigger, String eventTriger) throws SQLException {
        String name;
        String typeTrigger;
        typeTrigger = "UPDATE";
        name = "UPD_";
        if (nameTrigger == null) {
            name = name + clazz.getSimpleName();
        } else {
            name = nameTrigger;
        }
        executeTrigger(name, eventTriger, typeTrigger);
    }

    private void creatTriggerInsert(String nameTrigger, String eventTriger) throws SQLException {
        String name;
        String typeTrigger;
        typeTrigger = "INSERT";
        name = "INS_";
        if (nameTrigger == null) {
            name = name + clazz.getSimpleName();
        } else {
            name = nameTrigger;
        }
        executeTrigger(name, eventTriger, typeTrigger);
    }

    private void executeTrigger(String name, String event, String typeTriger) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TRIGGER IF NOT EXISTS ")
                .append(name)
                .append(event)
                .append(typeTriger)
                .append(" ON ")
                .append(clazz.getSimpleName())
                .append(" FOR EACH ROW ")
                .append("CALL \"ua.ithillel.dnepr.tymoshenko.olga.trigger.ModifyingTrigger\" ");
        Statement stat = connection.createStatement();
        stat.execute(query.toString());
    }
}

