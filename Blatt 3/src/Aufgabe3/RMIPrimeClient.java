package Aufgabe3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIPrimeClient extends Remote{
    void setResult(int isPrime) throws RemoteException;
}
