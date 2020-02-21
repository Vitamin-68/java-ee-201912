package ua.ithillel.dnepr.roman.gizatulin.rmi;

import lombok.experimental.UtilityClass;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

@UtilityClass
public class RmiUtils {
    public static void startServer(int port, String host, Remote remoteObject) throws Exception {
        String rmiUri = String.format(
                "rmi://%s:%s/%s",
                host,
                port,
                remoteObject.getClass().getSimpleName());

        LocateRegistry.createRegistry(port);
        Naming.rebind(rmiUri, remoteObject);
    }

    @SuppressWarnings("unchecked")
    public <T extends Remote> T startClient(int port, String host, Class<T> remoteObject) throws Exception {
        String rmiUri = String.format(
                "rmi://%s:%s/%s",
                host,
                port,
                remoteObject.getSimpleName());
        // lookup method to find reference of remote object
        return (T) Naming.lookup(rmiUri);
    }
}
