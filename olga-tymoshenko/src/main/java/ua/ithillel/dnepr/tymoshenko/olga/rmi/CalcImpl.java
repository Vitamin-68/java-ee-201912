package ua.ithillel.dnepr.tymoshenko.olga.rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalcImpl extends UnicastRemoteObject implements CalcRemote {
    public CalcImpl() throws RemoteException {}
    @Override
    public double add(double x, double y) {
        return x + y;
    }
    @Override
    public double multiply(double x, double y) {
        return x * y;
    }
    @Override
    public double subtract(double x, double y) {
        return x - y;
    }
    @Override
    public double division(double x, double y) {
        return x / y;
    }
}
