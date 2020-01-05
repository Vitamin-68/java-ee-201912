package ua.ithillel.dnepr.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public final class NetUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);
    public static final String LOCAL_HOST_NAME = "localhost";
    public static final String LOCAL_IP = "127.0.0.1";

    public static String getHostName() {
        String result = LOCAL_HOST_NAME;
        try {
            result = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            LOGGER.error("Hostname can not be resolved", ex);
        }
        return StringUtils.defaultIfBlank(result, LOCAL_IP);
    }

    public static boolean isPortAvailable(int checkPort) {
        boolean result = false;
        try (final ServerSocket socket = new ServerSocket(checkPort)) {
            socket.getLocalPort();
            result = true;
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return result;
    }

    public static int getFreePort() {
        int result = -1;
        try (final ServerSocket socket = new ServerSocket(0)) {
            result = socket.getLocalPort();
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return result;
    }

    public static List<Integer> getFreePorts(int from, int to, int count) {
        final List<Integer> result = new ArrayList<>();
        for (int i = from; i <= to && result.size() < count; i++) {
            if (isPortAvailable(i)) {
                result.add(i);
            }
        }
        return result;
    }

    private NetUtils() {
    }
}
