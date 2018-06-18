package Aufgabe2;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 30;
    private static final String CONCURRENCY_MODE = "FIXED_POOL";//FULL / FIXED_POOL / DYNAMIC_POOL
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static int receivePort;

    private static final Object monitor = new Object();

    private static Result myResult= null;//null falls berechnung nich fertig
    private static ArrayList<Long> pStatistic = new ArrayList();
    private static ArrayList<Long> wStatistic = new ArrayList();
    private static ArrayList<Long> cStatistic = new ArrayList();
    private static long totalRequestTime;

    private Component communication;
    String hostname;
    int port;
    long initialValue, count;
    String mode;


    public PrimeClient(String hostname, int port, long initialValue, long count) {
        this.hostname = hostname;
        this.port = port;
        this.initialValue = initialValue;
        this.count = count;
        this.mode = mode;
    }

    public void run() {
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            try {
                processNumber(i);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {

        long requestTime = System.currentTimeMillis();
        communication.send(new Message(hostname, receivePort, value), port, false);

        Result result =  (Result) communication.receive(receivePort, true, true).getContent();
        long responseTime = System.currentTimeMillis();
        totalRequestTime = responseTime -  requestTime;
        synchronized (monitor) {
            myResult = result;
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;
        String con_mode = CONCURRENCY_MODE;
        receivePort = (int) (Math.random() * 5000) + 2000;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port[" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

            System.out.print("Reicieve on Port [" + receivePort+ "] > ");
            input = reader.readLine();
            if (!input.equals("")) receivePort = Integer.parseInt(input);

            System.out.print("Concurrency mode [" + con_mode+ "] > ");
            input = reader.readLine();
            if (!input.equals("")) con_mode = input;

            PrimeClient calculator = new PrimeClient(hostname, port, initialValue, count);
            calculator.start();
            int i = 0;
            int last_i = -1;


            while (i < count) {
                if (!(last_i == i)) {
                    System.out.print(initialValue + i + ": ");
                }
                last_i = i;

                if (myResult == null) {
                    System.out.print(".");
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    updateStatistics(i);
                    long avgP = avg(pStatistic);
                    long avgW = avg(wStatistic);
                    long avgC= avg(cStatistic);
                    String output = (myResult.isPrime() == true ? "prime" : "not prime");
                    output += " | "+" p: "+myResult.getP()+"("+avgP+") ms";
                    output += " | "+" w: "+myResult.getW()+"("+avgW+") ms";
                    output += " | "+" c: "+(totalRequestTime - myResult.getP() - myResult.getW())+"("+avgC+") ms";
                    System.out.println(" ->" + output);
                    myResult = null;
                    i++;
                    synchronized (monitor) {
                        monitor.notifyAll();
                    }

                }
            }

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")){
                doExit = true;
            }else{
                receivePort = (int) (Math.random() * 5000) + 2000;
            }
        }
    }
    private static void updateStatistics(int i){
        pStatistic.add(myResult.getP());
        wStatistic.add(myResult.getW());
        long c = totalRequestTime - myResult.getP() - myResult.getW();
        cStatistic.add(c);
    }

    private static long avg(ArrayList<Long> list){
        long sum = 0;
        for(long a : list){
            sum += a;
        }
        return (sum / list.size());

    }
}
	
