package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TemporaryFolder;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class BaseTest<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {

    private Connection connection;
    private H2Server server;
    private TemporaryFolder folder;

    public Connection initTest(TemporaryFolder temporaryFolder) throws SQLException, IOException {
        int port;
        String databaseName;
        final String BASENAME = "test";
        folder = temporaryFolder;
        port = NetUtils.getFreePort();
        server = new H2Server(port);
        server.start();
        log.info("Server started host:" + NetUtils.getHostName() + " port:" + port);
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        folder.create();
        databaseName = folder.newFile(BASENAME).toString();

        return connection = DriverManager.getConnection("jdbc:h2:tcp://" + NetUtils.getHostName() + ':' + port + '/' + databaseName, "sa", "");
    }

    public void endTest() throws SQLException {
        connection.close();
        server.stop();
        log.info("Server stop");
        folder.delete();
    }


}

