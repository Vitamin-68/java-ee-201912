package ua.ithillel.dnepr.dml.rmi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.common.utils.RmiUtils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

@Slf4j
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class EngCalcImplTest {

    private static final int PORT = NetUtils.getFreePort();
    private static final String HOST = NetUtils.getHostName();
    private static final String REMOTE_OBJ_NAME = EngCalc.class.getSimpleName();
    private EngCalc stub;

    @BeforeAll
    void setUp() {
        try {
            EngCalc srvCalc = new EngCalcImpl();
            RmiUtils.startServer(PORT, HOST, REMOTE_OBJ_NAME,srvCalc);
        }catch (Exception e){
            log.error("RMI server:",e);
        }
    }

    @BeforeEach
    void setUpClient() {

        try {
            stub = RmiUtils.startClient(PORT, HOST, REMOTE_OBJ_NAME);
        } catch (Exception e) {
            log.error("RMI client",e);
        }
    }

    @Test
    public void fullTest() throws RemoteException {
        Assertions.assertEquals(stub.add(10,0.5),10.5);
        Assertions.assertEquals(stub.sub(10,5.5),4.5);
        Assertions.assertEquals(stub.div(10,5),2);
        Assertions.assertEquals(stub.mul(2,2),4.0);
        Assertions.assertEquals(stub.pow(10,2),100.0);
    }

}