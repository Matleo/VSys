package Aufgabe3;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class PrimeServerWorker extends Thread {
    private Component communication;
    private int newPort;
    private long value;

    private static int threadCount=0;

    public PrimeServerWorker(long value, int newPort){
        updateThreadCount(1);
        this.newPort = newPort;
        this.communication = new Component();
        this.value = value;
    }


    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
    public void run(){
        try {
            boolean result = primeService(value);
            communication.send(new Message("localhost", 0, result), newPort, true);
            communication.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            updateThreadCount(-1);
        }
    }

    private static synchronized void updateThreadCount(int amount){
        threadCount+=amount;
        System.out.println("ThreadCount ist aktuell: "+threadCount);
    }

}
