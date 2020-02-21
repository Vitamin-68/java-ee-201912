package ua.ithillel.dnepr.roman.gizatulin.rmi;

import lombok.extern.slf4j.Slf4j;

import java.rmi.Naming;

@Slf4j
public class ProgramClient {
    public static void main(String[] args) {
        try {
            CalcRemote stub = (CalcRemote) Naming.lookup("rmi://localhost:5000/calc");
            log.info(String.valueOf(stub.add(34, 4)));
            log.info(String.valueOf(stub.subtract(34, 4)));
            log.info(String.valueOf(stub.multiply(34, 4)));
            log.info(String.valueOf(stub.divide(34, 4)));
        } catch (Exception e) {
            log.error("Failed to start RMI client", e);
        }
    }
}
