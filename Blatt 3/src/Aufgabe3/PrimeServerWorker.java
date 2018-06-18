package Aufgabe3;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrimeServerWorker extends Thread {
    private String returnTo;
    private long value;
    private int port;

    private static int threadCount = 0;

    public PrimeServerWorker(long value , String returnTo, int port) {
        updateThreadCount(1);
        this.value = value;
        this.returnTo = returnTo;
        this.port = port;
    }


    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public void run() {
        boolean isPrime = primeService(value);
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",this.port);
            RMIPrimeClient client = (RMIPrimeClient) registry.lookup(returnTo);
            client.setResult(isPrime ? 1 : 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        updateThreadCount(-1);
    }

    private static synchronized void updateThreadCount(int amount) {
        threadCount += amount;
        //System.out.println("ThreadCount ist aktuell: " + threadCount);
    }

}
