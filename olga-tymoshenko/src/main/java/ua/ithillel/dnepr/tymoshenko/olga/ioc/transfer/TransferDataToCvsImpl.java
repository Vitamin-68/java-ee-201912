package ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Csv;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransferDataToCvsImpl<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements TransferData {

    private Connection connection;
    private String dest;

    @Override
    public void createConnection(String source, String dest) {
        this.dest = dest;
        UtilTransfer utilTransfer = new UtilTransfer();
        connection = utilTransfer.getBaseConnection(source);
    }

    @Override
    public void dataSending() {
        UtilTransfer utilTransfer = new UtilTransfer();
        Class<? extends AbstractEntity> clazz = utilTransfer.getClass(dest);
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(clazz.getSimpleName());
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query.toString());
            Csv csv = new Csv();
            csv.write(dest, resultSet, String.valueOf(UTF_8));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalStateException(e);
        } finally {
            utilTransfer.closeConnection(connection);
        }
    }
}

