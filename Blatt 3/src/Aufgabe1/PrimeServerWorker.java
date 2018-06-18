package Aufgabe1;
import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class PrimeServerWorker extends Thread {
    private Component communication;
    private int newPort;
    private long value;
    private long timeReceived;

    private static int threadCount=0;

    public PrimeServerWorker(long value, int newPort, long timeReceived){
        updateThreadCount(1);
        this.newPort = newPort;
        this.communication = new Component();
        this.value = value;
        this.timeReceived = timeReceived;
    }


    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
    public void run(){
        try {
            long time0 = System.currentTimeMillis();
            boolean result = primeService(value);
            long p = System.currentTimeMillis() - time0;
            long w = time0 - this.timeReceived;
            communication.send(new Message("localhost", 0, new Result(result,p, w)), newPort, true);
            communication.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            updateThreadCount(-1);
        }
    }

    private static synchronized void updateThreadCount(int amount){
        threadCount+=amount;
        //System.out.println("ThreadCount ist aktuell: "+threadCount);
    }

}
