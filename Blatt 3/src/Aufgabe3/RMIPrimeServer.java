package Aufgabe3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIPrimeServer extends Remote {
    void calculatePrime(long candidate, String registeredName) throws RemoteException;
}
