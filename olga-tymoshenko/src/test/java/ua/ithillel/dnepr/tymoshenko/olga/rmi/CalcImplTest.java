package ua.ithillel.dnepr.tymoshenko.olga.rmi;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.common.utils.RmiUtils;
import java.rmi.RemoteException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalcImplTest {
    private final int RMI_PORT = NetUtils.getFreePort();
    private final String RMI_HOST = NetUtils.getHostName();
    private final String OBJECT_NAME = CalcRemote.class.getSimpleName();
    CalcRemote stub;

    @BeforeAll
    void startTest() {
        CalcRemote test = null;
        try {
            test = new CalcImpl();
            RmiUtils.startServer(RMI_PORT, RMI_HOST, OBJECT_NAME, test);
            log.info("Server started");

        } catch (Exception e) {
            log.error("Server did not start " + e);
        }

        try {
            stub = RmiUtils.startClient(RMI_PORT, RMI_HOST, OBJECT_NAME);
            log.info("Client started");
        } catch (Exception e) {
            log.error("Client did not start " + e);
        }
    }

    @Test
    void add() throws RemoteException {
        double x = 2.5;
        double y = 10;
        double actual = stub.add(x, y);
        assertEquals(actual, 12.5);
    }

    @Test
    void multiply() throws RemoteException {
        double x = 25;
        double y = 10;
        double actual = stub.multiply(x, y);
        assertEquals(actual, 250);

    }

    @Test
    void subtract() throws RemoteException {
        double x = 35;
        double y = 10;
        double actual = stub.subtract(x, y);
        assertEquals(actual, 25);
    }

    @Test
    void division() throws RemoteException {
        double x = 35;
        double y = 10;
        double actual = stub.division(x, y);
        assertEquals(actual, 3.5);
    }
}