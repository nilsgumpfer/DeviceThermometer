package ThermometerServer;

import ThermometerServer.interfaces.ThermometerClientInterface;
import ThermometerServer.interfaces.ThermometerServerInterface;
import de.thm.smarthome.global.beans.ActionModeBean;
import de.thm.smarthome.global.beans.MeasureBean;
import de.thm.smarthome.global.enumeration.EUnitOfMeasurement;
import de.thm.smarthome.global.observer.AObservable;
import de.thm.smarthome.global.observer.IObserver;
import de.thm.smarthome.global.beans.ManufacturerBean;
import de.thm.smarthome.global.beans.ModelVariantBean;
import javafx.application.Platform;
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

    /*Attribute/Beans*/
    private MeasureBean temperature = new MeasureBean(0.0, EUnitOfMeasurement.TEMPERATURE_DEGREESCELSIUS);
    private ModelVariantBean modelVariant;
    private ManufacturerBean manufacturer;
    private ActionModeBean actionMode;
    public String genericName = "SmartHomeAPI";
    private String serialNumber;

    /*Variable*/
    public String serverstatus = null;
    public int serverport = 1099;
    public Registry rmiRegistry;
    private String serverIP;
    private ThermometerServerInterface stub = null;


    public StringProperty ThermometerTemperature = new SimpleStringProperty(String.valueOf(temperature.getMeasure_Double()) + " " + temperature.getUnitOfMeasurement_String());

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



    public String startServer() throws RemoteException {

        serverIP = getServerIP();
        System.setProperty("java.rmi.server.hostname", serverIP);

        if(stub == null) {
            stub = (ThermometerServerInterface) UnicastRemoteObject.exportObject(this, 0);
        }
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
            Naming.rebind("//127.0.0.1/"+genericName, this);
            this.serverstatus = "Gestartet";
            return "Server ist gestartet!";


        } catch (MalformedURLException e) {
            System.out.print(e.toString());
            return "Fehler beim Starten des Servers!";
        }

    }

    @Override
    public void update(Object o, Object change) {

    }
    public String getServerIP() {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();

        } catch (UnknownHostException e) {

            System.out.print(e.toString());
            return "0.0.0.0";
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
            System.out.print(e.toString());
            return "Fehler beim Stoppen des Servers!";
        } catch (NotBoundException e)
        {
            System.out.print(e.toString());
            return "Fehler beim Stoppen des Servers!";
        } catch (RemoteException e) {
            System.out.print(e.toString());
            return "Fehler beim Stoppen des Servers!";
        }

    }

    //SETTER//

    public void setTemperature (MeasureBean new_temp){
        temperature = new_temp;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ThermometerTemperature.set(String.valueOf(temperature.getMeasure_Double()) + " " + temperature.getUnitOfMeasurement_String());
            }
        });

        notifyObservers(this.temperature);
    }


    @Override
    public void setGenericName(String new_genericName) throws RemoteException {
        genericName = new_genericName;
    }

    // GETTER//
    @Override
    public MeasureBean getTemperature() throws RemoteException {
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

}
