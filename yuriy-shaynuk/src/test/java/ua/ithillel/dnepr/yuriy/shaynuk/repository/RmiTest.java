package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.common.utils.RmiUtils;
import ua.ithillel.dnepr.yuriy.shaynuk.rmi.CalcRemote;
import ua.ithillel.dnepr.yuriy.shaynuk.rmi.CalcRemoteImp;

import java.rmi.RemoteException;

@Slf4j
public class RmiTest {
    private static final int RMI_PORT = NetUtils.getFreePort();
    private static final String RMI_HOST = NetUtils.getHostName();
    private static final String OBJECT_NAME = CalcRemote.class.getSimpleName();
    private CalcRemote stub;

    @BeforeAll
    static void setupServer(){
        CalcRemote test = null;
        try {
            test = new CalcRemoteImp();
            RmiUtils.startServer(RMI_PORT, RMI_HOST, OBJECT_NAME, test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setupClient(){
        try {
            stub = RmiUtils.startClient(RMI_PORT, RMI_HOST, OBJECT_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void add() throws RemoteException {
        double result = stub.add(35, 5);
        Assertions.assertEquals(result, 40.0);
    }

    @Test
    void subtract() throws RemoteException {
        double result = stub.subtract(35, 5);
        Assertions.assertEquals(result, 30.0);
    }

    @Test
    void multiply() throws RemoteException {
        double result = stub.multiply(35, 5);
        Assertions.assertEquals(result, 175.0);
    }

    @Test
    void divide() throws RemoteException {
        double result = stub.divide(35, 5);
        Assertions.assertEquals(result, 7.0);
    }
}
