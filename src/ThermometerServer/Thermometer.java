package ThermometerServer;

import ThermometerServer.interfaces.ThermometerClientInterface;
import ThermometerServer.interfaces.ThermometerServerInterface;
import ThermometerServer.observer.AObservable;
import ThermometerServer.observer.IObserver;
import de.thm.smarthome.global.beans.ActionModeBean;
import de.thm.smarthome.global.beans.ManufacturerBean;
import de.thm.smarthome.global.beans.ModelVariantBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Tim on 07.04.2017.
 */



public class Thermometer extends AObservable implements IObserver, ThermometerServerInterface {

    public String serverstatus = null;
    public int serverport = 1099;
    public Registry rmiRegistry;


    private Double temperature = 20.00;
    private ModelVariantBean modelVariant;
    private ManufacturerBean manufacturer;
    private ActionModeBean actionMode;
    public String genericName;
    private String serialNumber;


    public StringProperty ThermometerTemperature = new SimpleStringProperty(String.valueOf(temperature) + "°C");

    public Thermometer() {

    }

    /*public double getTemperatureSrv(){
        return temperature;
    }*/

   /*public void setTemperatureSrv (double new_temp){
        temperature = new_temp;
        ThermometerTemperature.set(String.valueOf(temperature)+ " °C");
        notifyObservers(this.temperature);
   }*/

    /*public void setTemperature (double new_temp, ThermometerClientInterface c){
        temperature = new_temp;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ThermometerTemperature.set(String.valueOf(temperature) + " °C");
            }
        });

        notifyObservers(this.temperature);
    }*/

    public String startServer(String thermometername) throws RemoteException {
        ThermometerServerInterface stub = (ThermometerServerInterface) UnicastRemoteObject.exportObject(this, 0);
        rmiRegistry = LocateRegistry.createRegistry(serverport);
        try {
            /*if (System.getSecurityManager() == null) {
                System.setProperty("java.security.policy", "file:C:\\Users\\Tim\\IdeaProjects\\HeizungServer\\out\\production\\HeizungServer\\HeizungServer\\server.policy");
                System.setSecurityManager(new SecurityManager());

            }*/
            /*Aktiviert und definiert das Logging des Servers*/
            RemoteServer.setLog(System.out);
            //System.out.println(srvlog.toString());
            /*Bindet den Server an die folgende Adresse*/
            Naming.rebind("//127.0.0.1/"+thermometername, this);
            this.genericName = thermometername;
            this.serverstatus = "Gestartet";
            return "Server ist gestartet!";


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Fehler beim Starten des Servers!";
        }

    }

    @Override
    public void update(AObservable o, Object change) {

    }
    public String getServerIP() {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();

        } catch (UnknownHostException e) {

            e.printStackTrace();
            return null;
        }
    }

    public String stopServer(){
        try {

            //Registry rmiRegistry = LocateRegistry.getRegistry("127.0.0.1", serverport);
            //HeizungServerInterface myService = (HeizungServerInterface) rmiRegistry.lookup(heizungname);

            rmiRegistry.unbind(genericName);

            //UnicastRemoteObject.unexportObject(myService, true);
            UnicastRemoteObject.unexportObject(rmiRegistry, true);
            this.serverstatus = "Gestoppt";
            return "Server ist gestoppt!";

        } catch (NoSuchObjectException e)
        {
            e.printStackTrace();
            return "Fehler beim Stoppen des Servers!";
        } catch (NotBoundException e)
        {
            e.printStackTrace();
            return "Fehler beim Stoppen des Servers!";
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Fehler beim Stoppen des Servers!";
        }

    }

    @Override
    public double getTemperature() throws RemoteException {
        return temperature;
    }

    @Override
    public ModelVariantBean getModelVariant() throws RemoteException {
        return modelVariant;
    }

    @Override
    public ManufacturerBean getManufacturer() throws RemoteException {
        return manufacturer;
    }

    @Override
    public ActionModeBean getActionMode() throws RemoteException {
        return actionMode;
    }

    @Override
    public String getGenericName() throws RemoteException {
        return genericName;
    }

    @Override
    public String getSerialNumber() throws RemoteException {
        return serialNumber;
    }

    @Override
    public void setGenericName(String new_genericName) throws RemoteException {
        genericName = new_genericName;
    }
}
