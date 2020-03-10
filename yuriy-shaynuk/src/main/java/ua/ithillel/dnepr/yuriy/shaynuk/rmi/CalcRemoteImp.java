package ua.ithillel.dnepr.yuriy.shaynuk.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalcRemoteImp extends UnicastRemoteObject implements CalcRemote {

    public CalcRemoteImp() throws RemoteException {
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
