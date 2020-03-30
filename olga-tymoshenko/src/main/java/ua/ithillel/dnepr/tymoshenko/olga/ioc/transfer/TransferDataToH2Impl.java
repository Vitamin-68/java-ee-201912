package ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransferDataToH2Impl<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements TransferData {

    private Connection connection;
    private String source;

    @Override
    public void createConnection(String source, String dest) {
        this.source = source;
        UtilTransfer utilTransfer = new UtilTransfer();
        connection = utilTransfer.getBaseConnection(dest);
    }

    @Override
    public void dataSending() {
        UtilTransfer utilTransfer = new UtilTransfer();
        Class<? extends AbstractEntity> clazz = utilTransfer.getClass(source);
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
                    .append(StringUtils.removeEnd(new File(source).getName(), ".csv"))
                    .append(" (")
                    .append(headers.toString())
                    .append(") ")
                    .append(" AS SELECT * FROM CSVREAD ")
                    .append("( \'")
                    .append(source).append("\'").append(", ").append("null").append(", ").append("\'charset=UTF-8 fieldSeparator=;\'").append(" )");
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query.toString());
            } catch (SQLException e) {
                log.error(e.getSQLState());
                throw new IllegalStateException(e);
            } finally {
                utilTransfer.closeConnection(connection);
            }
        }
    }
}
