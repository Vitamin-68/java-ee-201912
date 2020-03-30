package ua.ithillel.dnepr.tymoshenko.olga.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalcRemote extends Remote {
    double add(double x, double y) throws RemoteException;
    double multiply(double x, double y) throws RemoteException;
    double subtract(double x, double y) throws RemoteException;
    double division(double x, double y) throws RemoteException;
}
