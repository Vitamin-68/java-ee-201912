package ua.ithillel.dnepr.roman.gizatulin.rmi;

import lombok.extern.slf4j.Slf4j;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Slf4j
public class ProgramServer {
    private static final int RMI_PORT = 5000;
    private static final String RMI_HOST = "localhost";

    public interface Test extends Remote {
        String getString(String test) throws RemoteException;
    }

    public static class TestImpl extends UnicastRemoteObject implements Test {
        protected TestImpl() throws RemoteException {
            super();
        }

        @Override
        public String getString(String test) {
            return test + "_result";
        }
    }

    public static void main(String[] args) throws Exception {
        CalcRemote test = new CalcImpl();

        RmiUtils.startServer(RMI_PORT, RMI_HOST, test);

        CalcRemote stub = RmiUtils.startClient(RMI_PORT, RMI_HOST, test.getClass());
        for (int i = 1; i < 50; i++) {
            log.info(String.valueOf(stub.add(i + 34, i + 4)));
            log.info(String.valueOf(stub.subtract(i + 34, i + 4)));
            log.info(String.valueOf(stub.multiply(i + 34, i + 4)));
            log.info(String.valueOf(stub.divide(i + 34, i + 4)));
        }
    }
}
