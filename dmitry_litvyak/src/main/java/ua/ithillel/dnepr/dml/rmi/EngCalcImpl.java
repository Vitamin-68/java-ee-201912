package ua.ithillel.dnepr.dml.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EngCalcImpl extends UnicastRemoteObject implements EngCalc {

    public EngCalcImpl() throws RemoteException {
    }

    @Override
    public double add(double a, double b) {
        return a + b;
    }

    @Override
    public double pow(double a, double b) throws RemoteException {
        return Math.pow(a , b);
    }

    @Override
    public double sub(double a, double b) {
        return a - b;
    }

    @Override
    public double mul(double a, double b) {
        return a * b;
    }

    @Override
    public double div(double a, double b) {
        return a / b;
    }
}

