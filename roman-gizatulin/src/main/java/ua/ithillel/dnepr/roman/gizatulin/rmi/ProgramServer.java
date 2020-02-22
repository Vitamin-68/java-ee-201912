package ua.ithillel.dnepr.roman.gizatulin.rmi;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.utils.NetUtils;

@Slf4j
public class ProgramServer {
    private static final int RMI_PORT = NetUtils.getFreePort();
    private static final String RMI_HOST = NetUtils.getHostName();
    private static final String OBJECT_NAME = CalcRemote.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        CalcRemote test = new CalcImpl();

        RmiUtils.startServer(RMI_PORT, RMI_HOST, OBJECT_NAME, test);

        CalcRemote stub = RmiUtils.startClient(RMI_PORT, RMI_HOST, OBJECT_NAME);
        for (int i = 1; i < 50; i++) {
            log.info(String.valueOf(stub.add(i + 34, i + 4)));
            log.info(String.valueOf(stub.subtract(i + 34, i + 4)));
            log.info(String.valueOf(stub.multiply(i + 34, i + 4)));
            log.info(String.valueOf(stub.divide(i + 34, i + 4)));
        }
    }
}
