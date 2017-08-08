package ThermometerServer.interfaces;

import de.thm.smarthome.global.beans.ActionModeBean;
import de.thm.smarthome.global.beans.ManufacturerBean;
import de.thm.smarthome.global.beans.MeasureBean;
import de.thm.smarthome.global.beans.ModelVariantBean;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tim on 07.04.2017.
 */
public interface ThermometerServerInterface extends Remote {


    void setGenericName(String new_genericName) throws RemoteException;

    MeasureBean getTemperature() throws RemoteException;
    ModelVariantBean getModelVariant() throws RemoteException;
    ManufacturerBean getManufacturer() throws RemoteException;
    ActionModeBean getActionMode() throws RemoteException;
    String getGenericName() throws RemoteException;
    String getSerialNumber() throws RemoteException;

    void attach(Object observer) throws RemoteException;
}
