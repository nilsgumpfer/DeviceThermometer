package ThermometerServer.interfaces;

import ThermometerServer.observer.AObservable;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tim on 07.04.2017.
 */
public interface ThermometerServerInterface extends Remote {

    public String getName(ThermometerClientInterface c) throws RemoteException;
    public double getTemperature(ThermometerClientInterface c) throws RemoteException;
    //public void update(AObservable o, Object change, ThermometerClientInterface c);
}
