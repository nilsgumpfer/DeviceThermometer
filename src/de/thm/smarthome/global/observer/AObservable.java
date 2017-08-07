package de.thm.smarthome.global.observer;

import de.thm.smarthome.global.beans.MeasureBean;
import de.thm.smarthome.global.enumeration.EUnitOfMeasurement;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class AObservable {
    private List<Object> attachedObservers = new ArrayList<>();
    public boolean changed = true;

    public void attach(Object observer){
        attachedObservers.add((IObserver)observer);
    }

    public void detach(Object observer){
        attachedObservers.remove(observer);
    }

    public void notifyObservers(Object change)  { //TODO: Ab hier nachverfolgen, ob Updates überhaupt beim SHManager ankommen




        for (Object element : attachedObservers) {
            try {
                IObserver myO = (IObserver) element;
                myO.update(this, change);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

}


