package vitaly.mosin.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiCalcImpl extends UnicastRemoteObject implements RmiCalc {

    public RmiCalcImpl() throws RemoteException {
    }

    @Override
    public double add(double a, double b) {
        return a + b;
    }

    @Override
    public double subtract(double a, double b) {
        return a - b;
    }

    @Override
    public double multiply(double a, double b) {
        return a * b;
    }

    @Override
    public double divide(double a, double b) {
        return a / b;
    }
}
