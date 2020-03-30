package vitaly.mosin.rmi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.common.utils.RmiUtils;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RmiCalcTest {

    private static final int RMI_PORT = NetUtils.getFreePort();
    private static final String RMI_HOST = NetUtils.getHostName();
    private static final String OBJECT_NAME = RmiCalc.class.getSimpleName();
    private static RmiCalc stub;

    @BeforeAll
    static void setUp() {

        try {
            RmiCalc rmiCalc = new RmiCalcImpl();
            RmiUtils.startServer(RMI_PORT, RMI_HOST, OBJECT_NAME, rmiCalc);
            stub = RmiUtils.startClient(RMI_PORT, RMI_HOST, OBJECT_NAME);
        } catch (Exception e) {
            log.error("Error start server/client.", e);
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add() {
        try {
            assertEquals(75.8, stub.add(50.3, 25.5));
        } catch (RemoteException e) {
            log.error("Error add operation.", e);
        }
    }

    @Test
    void subtract() {
        try {
            assertEquals(25, stub.subtract(50, 25));
        } catch (RemoteException e) {
            log.error("Error subtract operation.", e);
        }
    }

    @Test
    void multiply() {
        try {
            assertEquals(1250, stub.multiply(50, 25));
        } catch (RemoteException e) {
            log.error("Error multiply operation.", e);
        }
    }

    @Test
    void divide() {
        try {
            assertEquals(2, stub.divide(50, 25));
        } catch (RemoteException e) {
            log.error("Error divide operation.", e);
        }
    }
}