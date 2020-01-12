package ua.ithillel.dnepr.common.utils;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class H2Server implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2Server.class);
    private final int port;
    private Server embeddedH2Server;

    public H2Server(int port) {
        this.port = port;
        if (port < 1021 || port > 65535) {
            throw new IllegalArgumentException("Connection port must be 1 - 65535");
        }
    }

    public void start() throws SQLException {
        if (embeddedH2Server == null) {
            embeddedH2Server = Server.createTcpServer(
                    "-tcp",
                    "-tcpAllowOthers",
                    "-webAllowOthers",
                    "-ifNotExists",
                    "-tcpPort",
                    String.valueOf(this.port));
        }
        if (embeddedH2Server.isRunning(true)) {
            LOGGER.info("H2 server is already started. Port: {}", this.port);
        } else {
            embeddedH2Server.start();
            if (embeddedH2Server.isRunning(true)) {
                LOGGER.info("H2 server is successfully started. Port: {}", this.port);
            } else {
                throw new SQLException("Could not start H2 server.");
            }
        }
    }

    public void stop() {
        //Server.shutdownTcpServer("tcp://localhost:" + this.port, StringUtils.EMPTY, true, true);
        if (embeddedH2Server == null) {
            throw new IllegalStateException("H2 server is not started");
        }
        if (embeddedH2Server.isRunning(true)) {
            embeddedH2Server.stop();
        }
    }

    @Override
    public void close() throws SQLException {
        this.stop();
    }
}
