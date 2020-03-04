package ua.ithillel.dnepr.dml.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EngCalc extends Remote {
    double add(double a, double b) throws RemoteException;

    double sub(double a, double b) throws RemoteException;

    double mul(double a, double b) throws RemoteException;

    double div(double a, double b) throws RemoteException;

    double pow(double a, double b) throws RemoteException;
}

